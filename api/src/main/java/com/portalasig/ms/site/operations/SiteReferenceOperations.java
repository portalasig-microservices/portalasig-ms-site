package com.portalasig.ms.site.operations;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.ReferenceRequest;
import com.portalasig.ms.site.dto.site.Site;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * REST operations for managing site references.
 */
@HttpExchange(SiteRestConstant.SiteReference.Path.BASE)
public interface SiteReferenceOperations {

    /**
     * Creates or updates a site reference.
     */
    @ApiOperation(value = "Upsert site reference", response = Site.class)
    @PostExchange
    Site upsertSiteReference(
            @RequestBody @ApiParam(value = "Site reference request", required = true) ReferenceRequest request,
            @RequestParam("period_type") @ApiParam(value = "period_type") AcademicPeriodType periodType,
            @RequestParam("period_year") @ApiParam(value = "period_year") Integer periodYear,
            @RequestParam("course_code") @ApiParam(value = "course_code") String courseCode
    );

    /**
     * Deletes a site reference by ID.
     */
    @ApiOperation(value = "Delete site reference", response = Site.class)
    @DeleteExchange(SiteRestConstant.SiteReference.Path.ELEMENT)
    Site deleteSiteReference(
            @PathVariable @ApiParam(value = "Reference id", required = true) Integer referenceId,
            @RequestParam("period_type") @ApiParam(value = "period_type") AcademicPeriodType periodType,
            @RequestParam("period_year") @ApiParam(value = "period_year") Integer periodYear,
            @RequestParam("course_code") @ApiParam(value = "course_code") String courseCode
    );
}
