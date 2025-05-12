package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ConflictException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.commons.rest.exception.SystemErrorException;
import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.SemesterEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteRequest;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.repository.CourseRepository;
import com.portalasig.ms.site.repository.SemesterRepository;
import com.portalasig.ms.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SiteService {

    private final CourseRepository courseRepository;

    private final SiteRepository siteRepository;
    private final SemesterRepository semesterRepository;
    private final SiteMapper siteMapper;

    public Site createSite(SiteRequest request) {
        // TODO: STICK TO THE ENUM TYPE FOR ALL IN THE FUTURE (use FIRST, SECOND, etc.. over '1', '2,'U', etc)
        AcademicPeriodType academicPeriodType = AcademicPeriodType.valueOf(request.getPeriodType());
        Optional<SiteEntity> siteOptional = siteRepository.findSite(
                request.getCourseCode(),
                academicPeriodType.getCode(),
                request.getPeriodYear()
        );
        if (siteOptional.isPresent()) {
            throw new ConflictException("Course already exists");
        }

        CourseEntity course = courseRepository.findByCode(request.getCourseCode())
                .orElseThrow(ResourceNotFoundException::new);

        SemesterEntity semester = semesterRepository.findByAcademicPeriod(
                academicPeriodType.getCode(),
                request.getPeriodYear()
        ).orElseThrow(() -> new SystemErrorException(
                String.format(
                        "Semester with academic_period=%s-%s not found",
                        academicPeriodType.getCode(),
                        request.getPeriodYear()
                )));

        SiteEntity siteEntity = SiteEntity
                .builder()
                .course(course)
                .semester(semester)
                .build();
        siteEntity = siteRepository.save(siteEntity);
        log.info("Site with site_id={} was successfully created", siteEntity.getSiteId());
        log.debug("DEBUG -- Full object: {}", siteEntity);

        return siteMapper.toDto(siteEntity);
    }

    public Site findSite(String courseCode, String periodType, Integer periodYear) {
        // TODO: STICK TO THE ENUM TYPE FOR ALL IN THE FUTURE (use FIRST, SECOND, etc.. over '1', '2,'U', etc)
        AcademicPeriodType academicPeriodType = AcademicPeriodType.valueOf(periodType);
        Optional<SiteEntity> siteOptional = siteRepository.findSite(
                courseCode,
                academicPeriodType.getCode(),
                periodYear
        );
        SiteEntity siteEntity = siteOptional.orElseThrow(ResourceNotFoundException::new);
        return siteMapper.toDto(siteEntity);
    }
}
