package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
import com.portalasig.ms.site.dto.site.SiteRequest;
import com.portalasig.ms.site.operations.SiteOperations;
import com.portalasig.ms.site.service.SiteService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing course sites.
 * Implements SiteOperations.
 */
@RestController
@RequiredArgsConstructor
@Api(value = "Site Management Controller", tags = "Site Management")
@Slf4j
public class SiteController implements SiteOperations {

    private final SiteService siteService;

    @Override
    public Site createSite(@Valid SiteRequest request) {
        log.info("Creating site for course {}", request.getCourseCode());
        return siteService.createSite(request);
    }

    @Override
    public Site findSite(String courseCode, AcademicPeriodType periodType, int periodYear) {
        return siteService.findSite(courseCode, periodType, periodYear);
    }

    @Override
    public Site bulkPatchParties(Integer siteId, SitePartyRequest request) {
        return siteService.bulkPatchParties(siteId, request);
    }
}
