package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SiteSectionEntity;
import com.portalasig.ms.site.domain.entity.site.SiteSectionScheduleEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteScheduleRequest;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.mapper.SiteSectionScheduleMapper;
import com.portalasig.ms.site.repository.SiteRepository;
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

    /**
     * Creates or updates a schedule for a given section within a site.
     * <p>
     * If {@code scheduleId} is not provided in the request, a new schedule is created.
     * Otherwise, the schedule with the given ID is updated.
     *
     * @param siteId    the ID of the site
     * @param sectionId the ID of the section the schedule belongs to
     * @param request   the request data containing schedule details
     * @return the updated {@link Site} DTO with the modified schedule
     * @throws ResourceNotFoundException if the site or section is not found
     */
    public Site upsertSchedule(Integer siteId, Integer sectionId, SiteScheduleRequest request) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        SiteSectionEntity sectionEntity = siteEntity
                .getSections()
                .stream()
                .filter(section -> section.getSectionId().equals(sectionId))
                .findFirst()
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("section_id=%s of site_id=%s not found", sectionId, siteId)
                        )
                );

        if (request.getScheduleId() == null) {
            createSchedule(sectionEntity, request);
        } else {
            updateSchedule(sectionEntity, request);
        }

        siteEntity = siteRepository.save(siteEntity);
        log.info("Site schedule has been upserted in site_id={} for section_id={}", siteId, sectionId);
        return siteMapper.toDto(siteEntity);
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
     * @param sectionEntity the section entity that contains the schedule
     * @param request       the request containing updated schedule data
     * @throws ResourceNotFoundException if the schedule to update is not found
     */
    private void updateSchedule(SiteSectionEntity sectionEntity, SiteScheduleRequest request) {
        SiteSectionScheduleEntity existingSection = sectionEntity
                .getSchedules()
                .stream()
                .filter(schedule -> Objects.equals(
                        schedule.getScheduleId(), request.getScheduleId()
                ))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("schedule_id=%d to edit not found", request.getScheduleId()))
                );

        siteSectionScheduleMapper.toEntityFromExisting(existingSection, request);
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
