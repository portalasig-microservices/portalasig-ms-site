package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.dto.site.ReferenceRequest;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.operations.SiteReferenceOperations;
import com.portalasig.ms.site.service.SiteReferenceService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling site reference operations such as create, update, and delete.
 * Implements the SiteReferenceOperations interface.
 */
@RestController
@RequiredArgsConstructor
@Api(value = "Site Reference Management Controller", tags = "Site Management")
@Slf4j
public class SiteReferenceController implements SiteReferenceOperations {

    private final SiteReferenceService siteReferenceService;

    @Override
    public Site upsertSiteReference(ReferenceRequest request, AcademicPeriodType periodType, Integer periodYear, String courseCode) {
        log.info("Upserting reference for course {}", courseCode);
        return siteReferenceService.upsertReference(request, periodType, periodYear, courseCode);
    }

    @Override
    public Site deleteSiteReference(Integer referenceId, AcademicPeriodType periodType, Integer periodYear, String courseCode) {
        log.info("Deleting reference {} for course {}", referenceId, courseCode);
        return siteReferenceService.deleteReference(referenceId, periodType, periodYear, courseCode);
    }
}
