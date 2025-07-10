package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SiteSectionEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteSectionRequest;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.mapper.SiteSectionMapper;
import com.portalasig.ms.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;

/**
 * Service class that handles creation, update, and deletion of site sections.
 * <p>
 * Encapsulates the business logic for modifying sections within a given {@link SiteEntity},
 * ensuring consistency and mapping between DTOs and entities.
 *
 * @see Site
 * @see SiteSectionRequest
 * @see SiteEntity
 * @see SiteSectionEntity
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SiteSectionService {

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;
    private final SiteSectionMapper siteSectionMapper;

    /**
     * Creates or updates a section in a given site.
     * <p>
     * If the request does not include a {@code sectionId}, a new section is created.
     * If a {@code sectionId} is provided, the corresponding section is updated.
     *
     * @param siteId  the ID of the site to which the section belongs
     * @param request the request containing section data
     * @return the updated {@link Site} after the section is created or updated
     * @throws ResourceNotFoundException if the site or section is not found
     */
    public Site upsertSection(Integer siteId, SiteSectionRequest request) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        if (request.getSectionId() == null) {
            createSection(siteEntity, request);
        } else {
            updateSection(siteEntity, request);
        }

        siteEntity = siteRepository.save(siteEntity);
        log.info("Site section has been upserted in site_id={}", siteId);
        return siteMapper.toDto(siteEntity);
    }

    /**
     * Creates a new section and associates it with the given site.
     *
     * @param siteEntity the site entity to which the section will be added
     * @param request    the request containing section creation data
     */
    private void createSection(SiteEntity siteEntity, SiteSectionRequest request) {
        SiteSectionEntity newSection = siteSectionMapper.toEntityFromRequest(request);
        newSection.setSite(siteEntity);

        if (siteEntity.getSections() == null) {
            siteEntity.setSections(new HashSet<>());
        }

        siteEntity.getSections().add(newSection);
    }

    /**
     * Updates an existing section within the given site.
     *
     * @param siteEntity the site containing the section to update
     * @param request    the request with updated section data
     * @throws ResourceNotFoundException if the section to update does not exist
     */
    private void updateSection(SiteEntity siteEntity, SiteSectionRequest request) {
        SiteSectionEntity existingSection = siteEntity
                .getSections()
                .stream()
                .filter(section -> Objects.equals(
                        section.getSectionId(), request.getSectionId()
                ))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("section_id=%d to edit not found", request.getSectionId()))
                );

        siteSectionMapper.toEntityFromExisting(existingSection, request);
    }

    /**
     * Deletes a section from a site by its ID.
     *
     * @param siteId    the ID of the site from which the section will be removed
     * @param sectionId the ID of the section to delete
     * @return the updated {@link Site} after the section is removed
     * @throws ResourceNotFoundException if the site or section does not exist
     */
    public Site deleteSection(Integer siteId, Integer sectionId) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        SiteSectionEntity section = siteEntity
                .getSections()
                .stream()
                .filter(s -> s.getSectionId().equals(sectionId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("section_id=%d not found", sectionId)
                ));

        siteEntity.getSections().remove(section);
        siteRepository.save(siteEntity);

        log.info("Site section_id={} has been deleted from site_id={}", sectionId, siteId);
        return siteMapper.toDto(siteEntity);
    }
}