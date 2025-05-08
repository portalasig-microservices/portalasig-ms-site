package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.course.Course;
import com.portalasig.ms.site.dto.course.SiteObjectiveRequest;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.service.SiteObjectiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SiteRestConstant.SiteObjective.Path.BASE)
@RequiredArgsConstructor
@Api(value = "Site Objective Controller", tags = "Course Management")
public class SiteObjectiveController {

    private final SiteObjectiveService siteObjectiveService;

    @ApiOperation(value = "Upsert site objective", response = Course.class)
    @PostMapping
    // TODO SPECIAL AUTHORIZATION FOR THIS OPERATION
    public Site upsertSiteObjective(
            @Valid
            @RequestBody
            @ApiParam(value = "Course objective request", required = true) SiteObjectiveRequest request
    ) {
        return siteObjectiveService.upsertObjective(request);
    }
}
