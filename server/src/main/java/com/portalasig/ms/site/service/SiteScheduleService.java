package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SitePartyEntity;
import com.portalasig.ms.site.domain.entity.site.SiteSectionEntity;
import com.portalasig.ms.site.domain.entity.site.SiteSectionScheduleEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteScheduleRequest;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.mapper.SiteSectionScheduleMapper;
import com.portalasig.ms.site.repository.SiteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;

/**
 * Service class responsible for managing section schedules within a site.
 * <p>
 * Provides business logic for creating, updating, and deleting {@link SiteSectionScheduleEntity}
 * instances associated with a {@link SiteSectionEntity}.
 *
 * @see Site
 * @see SiteScheduleRequest
 * @see SiteSectionScheduleEntity
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SiteScheduleService {

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;
    private final SiteSectionScheduleMapper siteSectionScheduleMapper;
    private final SiteSectionService siteSectionService;

    /**
     * Creates or updates a schedule for a given section within a site.
     * <p>
     * If {@code scheduleId} is not provided in the request, a new schedule is created.
     * Otherwise, the schedule with the given ID is updated.
     *
     * @param siteId  the ID of the site
     * @param request the request data containing schedule details
     * @return the updated {@link Site} DTO with the modified schedule
     * @throws ResourceNotFoundException if the site or section is not found
     */
    @Transactional
    public Site upsertSchedule(Integer siteId, SiteScheduleRequest request) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        SiteSectionEntity targetSection = resolveOrCreateTargetSection(siteEntity, request);

        if (request.getScheduleId() == null) {
            createSchedule(targetSection, request);
        } else {
            updateSchedule(siteEntity, targetSection, request);
        }

        siteEntity = siteRepository.save(siteEntity);
        log.info(
                "Site schedule has been upserted in site_id={} for section_id={}",
                siteId,
                request.getSection().getSectionId()
        );
        return siteMapper.toDto(siteEntity);
    }

    private SiteSectionEntity resolveOrCreateTargetSection(
            SiteEntity siteEntity,
            SiteScheduleRequest request
    ) {
        Integer sectionId = request.getSection().getSectionId();
        if (sectionId != null || request.getSection().getCode() != null) {
            for (SiteSectionEntity section : siteEntity.getSections()) {
                if (Objects.equals(section.getSectionId(), sectionId) ||
                        Objects.equals(section.getCode(), request.getSection().getCode())
                ) {
                    return section;
                }
            }
            log.warn("Target section_id={} not found in site_id={}, creating a new section", sectionId, siteEntity.getSiteId());
        }

        return siteSectionService.createSection(siteEntity, request.getSection());
    }

    /**
     * Creates a new schedule and associates it with the given section.
     *
     * @param sectionEntity the section entity to which the schedule will be added
     * @param request       the request containing schedule creation data
     */
    private void createSchedule(SiteSectionEntity sectionEntity, SiteScheduleRequest request) {
        SiteSectionScheduleEntity newSchedule = siteSectionScheduleMapper.toEntityFromRequest(request);
        newSchedule.setSection(sectionEntity);

        if (sectionEntity.getSchedules() == null) {
            sectionEntity.setSchedules(new HashSet<>());
        }

        sectionEntity.getSchedules().add(newSchedule);
    }

    /**
     * Updates an existing schedule associated with a given section.
     *
     * @param siteEntity    the site entity
     * @param sectionEntity the section entity that contains the schedule
     * @param request       the request containing updated schedule data
     * @throws ResourceNotFoundException if the schedule to update is not found
     */
    private void updateSchedule(SiteEntity siteEntity, SiteSectionEntity sectionEntity, SiteScheduleRequest request) {
        SiteSectionEntity currentSection = null;
        SiteSectionScheduleEntity scheduleEntity = null;
        // Find existing section containing the schedule if any
        for (SiteSectionEntity sec : siteEntity.getSections()) {
            if (sec.getSchedules() == null) {
                continue;
            }
            for (SiteSectionScheduleEntity sc : sec.getSchedules()) {
                if (Objects.equals(sc.getScheduleId(), request.getScheduleId())) {
                    currentSection = sec;
                    scheduleEntity = sc;
                    break;
                }
            }
            if (scheduleEntity != null) {
                break;
            }
        }
        if (scheduleEntity == null) {
            throw new ResourceNotFoundException(
                    String.format("schedule_id=%d to edit not found", request.getScheduleId())
            );
        }

        // If schedule was moved to another section, then readjust
        if (!Objects.equals(currentSection.getSectionId(), sectionEntity.getSectionId())) {
            currentSection.getSchedules().remove(scheduleEntity);
            if (currentSection.getSchedules().isEmpty()) {
                siteEntity.getSections().remove(currentSection);
            }
            scheduleEntity.setSection(sectionEntity);
            if (sectionEntity.getSchedules() == null) {
                sectionEntity.setSchedules(new HashSet<>());
            }
            sectionEntity.getSchedules().add(scheduleEntity);
        } else {
            if (sectionEntity.getSchedules() == null) {
                sectionEntity.setSchedules(new HashSet<>());
            }
            sectionEntity.getSchedules().add(scheduleEntity);
            scheduleEntity.setSection(sectionEntity);
        }

        siteSectionScheduleMapper.toEntityFromExisting(scheduleEntity, request);
        updateInstructor(scheduleEntity, request);
    }

    private void updateInstructor(SiteSectionScheduleEntity scheduleEntity, SiteScheduleRequest request) {
        if (request.getInstructor() == null || request.getInstructor().getPartyId() == null) {
            return;
        }

        final Integer newPartyId = request.getInstructor().getPartyId();
        final Integer currentPartyId = (scheduleEntity.getInstructor() != null)
                ? scheduleEntity.getInstructor().getPartyId()
                : null;

        if (Objects.equals(currentPartyId, newPartyId)) {
            return;
        }


        SitePartyEntity newInstructor = SitePartyEntity
                .builder()
                .partyId(request.getInstructor().getPartyId())
                .identity(request.getInstructor().getIdentity())
                .email(request.getInstructor().getEmail())
                .firstName(request.getInstructor().getFirstName())
                .lastName(request.getInstructor().getLastName())
                .partyRole(request.getInstructor().getPartyRole())
                .partySiteTitle(request.getInstructor().getPartySiteTitle())
                .build();
        scheduleEntity.setInstructor(newInstructor);
    }

    /**
     * Deletes a schedule from a section within a site.
     *
     * @param siteId     the ID of the site
     * @param sectionId  the ID of the section containing the schedule
     * @param scheduleId the ID of the schedule to delete
     * @return the updated {@link Site} DTO after the schedule has been removed
     * @throws ResourceNotFoundException if the site, section, or schedule is not found
     */
    public Site deleteSchedule(Integer siteId, Integer sectionId, Integer scheduleId) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        SiteSectionEntity sectionEntity = siteEntity
                .getSections()
                .stream()
                .filter(s -> s.getSectionId().equals(sectionId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("section_id=%d not found", sectionId)
                ));

        SiteSectionScheduleEntity scheduleEntity = sectionEntity
                .getSchedules()
                .stream()
                .filter(sc -> sc.getScheduleId().equals(scheduleId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("schedule_id=%d not found", scheduleId)
                ));

        sectionEntity.getSchedules().remove(scheduleEntity);
        siteRepository.save(siteEntity);

        log.info(
                "Site schedule_id={} has been deleted from section_id={} of site_id={}",
                scheduleId,
                sectionId,
                siteId
        );
        return siteMapper.toDto(siteEntity);
    }
}
