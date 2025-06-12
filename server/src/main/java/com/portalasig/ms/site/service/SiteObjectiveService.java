package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.course.CourseObjectiveEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteObjectiveRequest;
import com.portalasig.ms.site.mapper.CourseObjectiveMapper;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Service for managing course objectives in a site.
 * Supports create, update, and delete operations.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SiteObjectiveService {

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;
    private final CourseObjectiveMapper courseObjectiveMapper;

    /**
     * Creates or updates a course objective in a site.
     *
     * @param request    the objective request data
     * @param periodType the academic period type
     * @param periodYear the academic period year
     * @param courseCode the course code
     * @return the updated site
     */
    public Site upsertObjective(
            SiteObjectiveRequest request,
            AcademicPeriodType periodType,
            Integer periodYear,
            String courseCode
    ) {
        SiteEntity siteEntity = siteRepository.findSite(
                courseCode,
                periodType,
                periodYear
        ).orElseThrow(() -> new ResourceNotFoundException("Site not found"));

        if (request.getCourseObjectiveId() == null) {
            createCourseObjective(siteEntity, request);
        } else {
            updateCourseObjective(siteEntity, request);
        }
        siteEntity = siteRepository.save(siteEntity);

        log.info("Course objective has been upserted in site_id={}", siteEntity.getSiteId());
        return siteMapper.toDto(siteEntity);
    }

    /**
     * Adds a new course objective to the site.
     *
     * @param siteEntity the site entity
     * @param request    the request with objective data
     */
    private void createCourseObjective(SiteEntity siteEntity, SiteObjectiveRequest request) {
        CourseObjectiveEntity newObjective = courseObjectiveMapper.toEntityFromRequest(request);
        newObjective.setSites(Set.of(siteEntity));
        if (siteEntity.getObjectives() == null) {
            siteEntity.setObjectives(new HashSet<>());
        }
        siteEntity.getObjectives().add(newObjective);
    }

    private void updateCourseObjective(SiteEntity siteEntity, SiteObjectiveRequest request) {
        var existingObjective = siteEntity
                .getObjectives()
                .stream()
                .filter(obj -> Objects.equals(
                        obj.getCourseObjectiveId(), request.getCourseObjectiveId()
                ))
                .findFirst()
                .orElseThrow(() -> {
                    String errorMessage = String.format(
                            "course_objective=%s not found",
                            request.getCourseObjectiveId()
                    );
                    return new ResourceNotFoundException(errorMessage);
                });
        courseObjectiveMapper.toEntityFromExisting(existingObjective, request);
    }

    /**
     * Deletes a course objective from the site.
     *
     * @param courseObjectiveId the ID of the objective to delete
     * @param periodType        the academic period type
     * @param periodYear        the academic period year
     * @param courseCode        the course code
     * @return the updated site
     */
    public Site deleteObjective(
            Integer courseObjectiveId,
            AcademicPeriodType periodType,
            Integer periodYear,
            String courseCode
    ) {
        SiteEntity siteEntity = siteRepository.findSite(
                courseCode,
                periodType,
                periodYear
        ).orElseThrow(() -> new ResourceNotFoundException("Site not found"));
        CourseObjectiveEntity objective = siteEntity
                .getObjectives()
                .stream()
                .filter(obj ->
                        obj.getCourseObjectiveId().equals(courseObjectiveId)
                )
                .findAny()
                .orElseThrow(() -> {
                    String errorMessage = String.format(
                            "course_objective_id=%s not found",
                            courseObjectiveId
                    );
                    return new ResourceNotFoundException(errorMessage);
                });
        siteEntity.getObjectives().remove(objective);
        siteRepository.save(siteEntity);
        return siteMapper.toDto(siteEntity);
    }
}
