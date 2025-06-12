package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.ReferenceEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.ReferenceRequest;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.mapper.ReferenceMapper;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Service class responsible for managing bibliographic references in course sites.
 * Provides functionality to create, update, and delete references associated with a specific site.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SiteReferenceService {

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;
    private final ReferenceMapper referenceMapper;

    /**
     * Creates or updates a reference on a course site. If the request does not contain an ID,
     * a new reference is created; otherwise, the existing reference is updated.
     *
     * @param request    the reference request payload
     * @param periodType the academic period type (e.g., SEMESTER)
     * @param periodYear the academic period year (e.g., 2025)
     * @param courseCode the code of the course to which the site belongs
     * @return the updated Site DTO with the reference upserted
     */
    public Site upsertReference(
            ReferenceRequest request,
            AcademicPeriodType periodType,
            Integer periodYear,
            String courseCode
    ) {
        request.validateUrl();

        SiteEntity siteEntity = siteRepository.findSite(
                courseCode,
                periodType,
                periodYear
        ).orElseThrow(() -> new ResourceNotFoundException("Site not found"));

        if (request.getReferenceId() == null) {
            createReference(siteEntity, request);
        } else {
            updateReference(siteEntity, request);
        }

        siteEntity = siteRepository.save(siteEntity);

        log.info("Reference upserted in site_id={}", siteEntity.getSiteId());
        return siteMapper.toDto(siteEntity);
    }

    /**
     * Adds a new reference to the given site.
     *
     * @param siteEntity the site entity to which the reference will be added
     * @param request    the reference request data
     */
    private void createReference(SiteEntity siteEntity, ReferenceRequest request) {
        ReferenceEntity newReference = referenceMapper.toEntityFromRequest(request);
        newReference.setSites(Set.of(siteEntity));

        if (siteEntity.getReferences() == null) {
            siteEntity.setReferences(new HashSet<>());
        }

        siteEntity.getReferences().add(newReference);
    }

    /**
     * Updates an existing reference in the given site.
     *
     * @param siteEntity the site entity containing the reference
     * @param request    the request with updated reference data
     * @throws ResourceNotFoundException if the reference ID does not exist in the site
     */
    private void updateReference(SiteEntity siteEntity, ReferenceRequest request) {
        ReferenceEntity existingReference = siteEntity
                .getReferences()
                .stream()
                .filter(ref -> Objects.equals(ref.getReferenceId(), request.getReferenceId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("reference_id=%d not found", request.getReferenceId()))
                );

        referenceMapper.toEntityFromExisting(existingReference, request);
    }

    /**
     * Deletes a reference from a course site.
     *
     * @param referenceId the ID of the reference to delete
     * @param periodType  the academic period type
     * @param periodYear  the academic period year
     * @param courseCode  the course code
     * @return the updated Site DTO without the deleted reference
     * @throws ResourceNotFoundException if the site or reference does not exist
     */
    public Site deleteReference(
            Integer referenceId,
            AcademicPeriodType periodType,
            Integer periodYear,
            String courseCode
    ) {
        SiteEntity siteEntity = siteRepository.findSite(
                courseCode,
                periodType,
                periodYear
        ).orElseThrow(() -> new ResourceNotFoundException("Site not found"));

        ReferenceEntity reference = siteEntity
                .getReferences()
                .stream()
                .filter(ref -> ref.getReferenceId().equals(referenceId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("reference_id=%d not found", referenceId)
                ));

        siteEntity.getReferences().remove(reference);
        siteRepository.save(siteEntity);

        log.info("Reference with id={} deleted from site_id={}", referenceId, siteEntity.getSiteId());
        return siteMapper.toDto(siteEntity);
    }
}
