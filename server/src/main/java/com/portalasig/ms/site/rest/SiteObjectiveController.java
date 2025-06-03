package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteObjectiveRequest;
import com.portalasig.ms.site.operations.SiteObjectiveOperations;
import com.portalasig.ms.site.service.SiteObjectiveService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling course site objectives.
 * Implements SiteObjectiveOperations.
 */
@RestController
@RequiredArgsConstructor
@Api(value = "Site Objective Management Controller", tags = "Site Management")
@Slf4j
public class SiteObjectiveController implements SiteObjectiveOperations {

    private final SiteObjectiveService siteObjectiveService;

    @Override
    public Site upsertSiteObjective(SiteObjectiveRequest request, AcademicPeriodType periodType, Integer periodYear, String courseCode) {
        log.info("Upserting objective for course {}", courseCode);
        return siteObjectiveService.upsertObjective(request, periodType, periodYear, courseCode);
    }

    @Override
    public Site deleteObjectiveById(Integer courseObjectiveId, AcademicPeriodType periodType, Integer periodYear, String courseCode) {
        log.info("Deleting objective {} for course {}", courseObjectiveId, courseCode);
        return siteObjectiveService.deleteObjective(courseObjectiveId, periodType, periodYear, courseCode);
    }
}
