package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteRequest;
import com.portalasig.ms.site.service.SiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(SiteRestConstant.Site.Path.BASE)
@RequiredArgsConstructor
@Api(value = "Course site controller", tags = "Site Management")
public class SiteController {

    private final SiteService siteService;

    @ApiOperation(value = "Create new site by course code", response = Site.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Site has been created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping()
    public Site createSite(
            @RequestBody @Valid @ApiParam(value = "Semester Request", required = true) SiteRequest request
    ) {
        return siteService.createSite(request);
    }

    @ApiOperation(value = "Find site by course code, period type and period year")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Site retrieved successfully"),
            @ApiResponse(code = 404, message = "Site not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping
    public Site findSite(
            @RequestParam(value = "course_code") @ApiParam(value = "Course code") String courseCode,
            @RequestParam(value = "period_type") @ApiParam(value = "period type") AcademicPeriodType periodType,
            @RequestParam(value = "period_year") @ApiParam(value = "period year") int periodYear
    ) {
        return siteService.findSite(courseCode, periodType, periodYear);
    }
}
