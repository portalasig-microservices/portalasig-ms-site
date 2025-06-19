package com.portalasig.ms.site.operations;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * REST operations for managing academic sites.
 */
@HttpExchange(SiteRestConstant.Site.Path.BASE)
public interface SiteOperations {

    /**
     * Creates a new site for a given course code.
     */
    @ApiOperation(value = "Create new site by course code", response = Site.class)
    @PostExchange
    Site createSite(
            @RequestBody @Valid @ApiParam(value = "Site Request", required = true) SiteRequest request
    );

    /**
     * Retrieves a site by course code, period type, and year.
     */
    @ApiOperation(value = "Find site by course code, period type and period year", response = Site.class)
    @GetExchange
    Site findSite(
            @RequestParam("course_code") @ApiParam(value = "Course code") String courseCode,
            @RequestParam("period_type") @ApiParam(value = "Period type") AcademicPeriodType periodType,
            @RequestParam("period_year") @ApiParam(value = "Period year") int periodYear
    );

    /**
     * Deletes a site by its ID.
     *
     * @param siteId the ID of the site to delete
     */
    @DeleteExchange(SiteRestConstant.Site.Path.ELEMENT)
    void deleteSite(@PathVariable @ApiParam(value = "Site ID", required = true) Integer siteId);
}
