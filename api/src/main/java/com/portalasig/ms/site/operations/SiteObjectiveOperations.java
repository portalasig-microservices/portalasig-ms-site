package com.portalasig.ms.site.operations;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteObjectiveRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * REST operations for managing course objectives.
 */
@HttpExchange(SiteRestConstant.SiteObjective.Path.BASE)
public interface SiteObjectiveOperations {

    /**
     * Creates or updates a course objective.
     */
    @ApiOperation(value = "Upsert site objective", response = Site.class)
    @PostExchange
    Site upsertSiteObjective(
            @RequestBody @ApiParam(value = "Course objective request", required = true) SiteObjectiveRequest request,
            @RequestParam("period_type") @ApiParam(value = "period_type") AcademicPeriodType periodType,
            @RequestParam("period_year") @ApiParam(value = "period_year") Integer periodYear,
            @RequestParam("course_code") @ApiParam(value = "course_code") String courseCode
    );

    /**
     * Deletes a course objective by ID.
     */
    @ApiOperation(value = "Delete objective", response = Site.class)
    @DeleteExchange(SiteRestConstant.SiteObjective.Path.ELEMENT)
    Site deleteObjectiveById(
            @PathVariable @ApiParam(value = "Course objective id", required = true) Integer courseObjectiveId,
            @RequestParam("period_type") @ApiParam(value = "period_type") AcademicPeriodType periodType,
            @RequestParam("period_year") @ApiParam(value = "period_year") Integer periodYear,
            @RequestParam("course_code") @ApiParam(value = "course_code") String courseCode
    );
}
