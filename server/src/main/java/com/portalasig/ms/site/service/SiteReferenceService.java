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
import java.util.Optional;
import java.util.Set;

/**
 * Service for managing site references. Supports creating, updating, and deleting references on a course site.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SiteReferenceService {

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;
    private final ReferenceMapper referenceMapper;

    /**
     * Creates or updates a reference on the specified course site.
     * If referenceId is null, a new reference is created.
     *
     * @param request    the reference data
     * @param periodType the academic period type
     * @param periodYear the academic period year
     * @param courseCode the course code
     * @return the updated site with the reference
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
            addNewSiteReference(siteEntity, request);
        } else {
            var existingReference = siteEntity
                    .getReferences()
                    .stream()
                    .filter(reference -> Objects.equals(
                            reference.getReferenceId(), request.getReferenceId()
                    ))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Reference to edit not found"));
            referenceMapper.toEntityFromExisting(existingReference, request);
        }
        siteEntity = siteRepository.save(siteEntity);

        String academicPeriod = String.format("%s-%s", periodType, periodYear);
        log.info(
                "Site reference={} upserted in site with academic_period={}",
                request.getDescription(),
                academicPeriod
        );
        return siteMapper.toDto(siteEntity);
    }

    /**
     * Adds a new reference entity to the given site.
     *
     * @param siteEntity the site entity
     * @param request    the reference request
     */
    private void addNewSiteReference(SiteEntity siteEntity, ReferenceRequest request) {
        ReferenceEntity newReference = referenceMapper.toEntityFromRequest(request);
        newReference.setSites(Set.of(siteEntity));
        if (siteEntity.getReferences() == null) {
            siteEntity.setReferences(new HashSet<>());
        }
        siteEntity.getReferences().add(newReference);
    }

    /**
     * Deletes a reference from the specified course site.
     *
     * @param referenceId the ID of the reference to delete
     * @param periodType  the academic period type
     * @param periodYear  the academic period year
     * @param courseCode  the course code
     * @return the updated site without the reference
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
        Optional<ReferenceEntity> maybeReference = siteEntity
                .getReferences()
                .stream()
                .filter(objective ->
                        objective.getReferenceId().equals(referenceId)
                )
                .findAny();

        if (maybeReference.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("reference_id=%s not found", referenceId)
            );
        }
        var reference = maybeReference.get();
        siteEntity.getReferences().remove(reference);
        siteRepository.save(siteEntity);
        return siteMapper.toDto(siteEntity);
    }
}
