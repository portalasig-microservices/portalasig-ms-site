package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.ReferenceRequest;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.service.SiteReferenceService;
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
@RequestMapping(SiteRestConstant.SiteReference.Path.BASE)
@RequiredArgsConstructor
@Api(value = "Site Reference Controller", tags = "Course Management")
public class SiteReferenceController {

    private final SiteReferenceService siteReferenceService;

    @ApiOperation(value = "Upsert site reference", response = Site.class)
    @PostMapping
    // TODO SPECIAL AUTHORIZATION FOR THIS OPERATION
    public Site upsertSiteReference(
            @Valid
            @RequestBody
            @ApiParam(value = "Site reference request", required = true) ReferenceRequest request,
            @RequestParam(value = "period_type")
            @ApiParam(value = "period_type") AcademicPeriodType periodType,
            @RequestParam(value = "period_year")
            @ApiParam(value = "period_year") Integer periodYear,
            @RequestParam(value = "course_code")
            @ApiParam(value = "course_code") String courseCode
    ) {
        return siteReferenceService.upsertReference(request, periodType, periodYear, courseCode);
    }

    @ApiOperation(value = "Delete site reference")
    @DeleteMapping(SiteRestConstant.SiteReference.Path.ELEMENT)
    public Site deleteSiteReference(
            @PathVariable @ApiParam(value = "Reference id", required = true) Integer referenceId,
            @RequestParam(value = "period_type")
            @ApiParam(value = "period_type") AcademicPeriodType periodType,
            @RequestParam(value = "period_year")
            @ApiParam(value = "period_year") Integer periodYear,
            @RequestParam(value = "course_code")
            @ApiParam(value = "course_code") String courseCode
    ) {
        return siteReferenceService.deleteReference(referenceId, periodType, periodYear, courseCode);
    }
}
