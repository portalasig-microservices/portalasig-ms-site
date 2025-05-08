package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.course.CourseObjectiveEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.course.SiteObjectiveRequest;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.mapper.CourseObjectiveMapper;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class SiteObjectiveService {


    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;
    private final CourseObjectiveMapper courseObjectiveMapper;

    public Site upsertObjective(SiteObjectiveRequest request) {
        // TODO: FIX ENUM AND DB EXPECTED VALUE SO WE STOP DOING THIS TRANSFORMATION
        AcademicPeriodType academicPeriodType = AcademicPeriodType.valueOf(request.getPeriodType());
        SiteEntity siteEntity = siteRepository.findSite(
                request.getCourseCode(),
                academicPeriodType.getCode(),
                request.getPeriodYear()
        ).orElseThrow(() -> new ResourceNotFoundException("Site not found"));

        var maybeObjective = siteEntity
                .getObjectives()
                .stream()
                .filter(obj -> Objects.equals(
                        obj.getCourseObjectiveId(), request.getCourseObjectiveId()
                ))
                .findFirst();
        if (maybeObjective.isEmpty()) {
            addNewCourseObjective(siteEntity, request);
        } else {
            courseObjectiveMapper.toEntityFromExisting(maybeObjective.get(), request);
        }
        siteEntity = siteRepository.save(siteEntity);

        String academicPeriod = String.format("%s-%s", academicPeriodType.getCode(), request.getPeriodYear());
        log.info("Course objective={} upserted in site with academic_period={}", request.getDescription(), academicPeriod);
        return siteMapper.toDto(siteEntity);
    }

    private void addNewCourseObjective(SiteEntity siteEntity, SiteObjectiveRequest request) {
        CourseObjectiveEntity newObjective = courseObjectiveMapper.toEntityFromRequest(request);
        newObjective.setSites(Set.of(siteEntity));
        if (siteEntity.getObjectives() == null) {
            siteEntity.setObjectives(new HashSet<>());
        }
        siteEntity.getObjectives().add(newObjective);
    }

}
