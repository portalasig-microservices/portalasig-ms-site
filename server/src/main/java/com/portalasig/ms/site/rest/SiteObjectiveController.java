package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteObjectiveRequest;
import com.portalasig.ms.site.service.SiteObjectiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SiteRestConstant.SiteObjective.Path.BASE)
@RequiredArgsConstructor
@Api(value = "Site Objective Controller", tags = "Course Management")
public class SiteObjectiveController {

    private final SiteObjectiveService siteObjectiveService;

    @ApiOperation(value = "Upsert site objective", response = Site.class)
    @PostMapping
    // TODO SPECIAL AUTHORIZATION FOR THIS OPERATION
    public Site upsertSiteObjective(
            @Valid
            @RequestBody
            @ApiParam(value = "Course objective request", required = true) SiteObjectiveRequest request,
            @RequestParam(value = "period_type")
            @ApiParam(value = "period_type") AcademicPeriodType periodType,
            @RequestParam(value = "period_year")
            @ApiParam(value = "period_year") Integer periodYear,
            @RequestParam(value = "course_code")
            @ApiParam(value = "course_code") String courseCode
    ) {
        return siteObjectiveService.upsertObjective(request, periodType, periodYear, courseCode);
    }

    @ApiOperation(value = "Delete Objective")
    @DeleteMapping(SiteRestConstant.SiteObjective.Path.ELEMENT)
    public Site deleteObjectiveById(
            @PathVariable @ApiParam(value = "Course objective id", required = true) Integer courseObjectiveId,
            @RequestParam(value = "period_type")
            @ApiParam(value = "period_type") AcademicPeriodType periodType,
            @RequestParam(value = "period_year")
            @ApiParam(value = "period_year") Integer periodYear,
            @RequestParam(value = "course_code")
            @ApiParam(value = "course_code") String courseCode
    ) {
        return siteObjectiveService.deleteObjective(courseObjectiveId, periodType, periodYear, courseCode);
    }
}
