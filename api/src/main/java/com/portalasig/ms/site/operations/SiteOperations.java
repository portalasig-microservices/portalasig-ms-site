package com.portalasig.ms.site.operations;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
import com.portalasig.ms.site.dto.site.SiteRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(SiteRestConstant.Site.Path.BASE)
public interface SiteOperations {

    @ApiOperation(value = "Create new site by course code", response = Site.class)
    @PostExchange
    Site createSite(
            @RequestBody @Valid @ApiParam(value = "Site Request", required = true) SiteRequest request
    );

    @ApiOperation(value = "Find site by course code, period type and period year", response = Site.class)
    @GetExchange
    Site findSite(
            @RequestParam("course_code") @ApiParam(value = "Course code") String courseCode,
            @RequestParam("period_type") @ApiParam(value = "Period type") AcademicPeriodType periodType,
            @RequestParam("period_year") @ApiParam(value = "Period year") int periodYear
    );

    @PatchExchange(SiteRestConstant.Site.Path.ELEMENT)
    Site bulkPatchParties(
            @PathVariable @ApiParam(value = "Semester ID", required = true) Integer siteId,
            @RequestBody @Valid @ApiParam(value = "Site Party Request", required = true) SitePartyRequest request
    );
}
