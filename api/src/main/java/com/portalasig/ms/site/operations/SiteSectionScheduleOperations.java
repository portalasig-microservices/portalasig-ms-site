package com.portalasig.ms.site.operations;

import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteScheduleRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Operations for managing site section schedules.
 * <p>
 * Provides endpoints to upsert and delete schedules for site sections.
 * </p>
 */
@HttpExchange(SiteRestConstant.SiteSectionSchedule.Path.BASE)
public interface SiteSectionScheduleOperations {

    /**
     * Upserts a schedule for a site section.
     *
     * @param siteId    the ID of the site
     * @param sectionId the ID of the section
     * @param request   the schedule request details
     * @return the updated Site object
     */
    @ApiOperation(value = "Upsert site schedule", response = Site.class)
    @PostExchange
    Site upsertSchedule(
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId,
            @PathVariable @ApiParam(value = "Section id", required = true) Integer sectionId,
            @RequestBody
            @ApiParam(value = "Site section request", required = true)
            SiteScheduleRequest request
    );

    /**
     * Deletes a schedule from a site section.
     *
     * @param siteId     the ID of the site
     * @param sectionId  the ID of the site section
     * @param scheduleId the ID of the schedule to delete
     * @return the updated Site object
     */
    @ApiOperation(value = "Delete schedule", response = Site.class)
    @DeleteExchange(SiteRestConstant.SiteSectionSchedule.Path.ELEMENT)
    Site deleteSchedule(
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId,
            @PathVariable @ApiParam(value = "Site section id", required = true) Integer sectionId,
            @PathVariable @ApiParam(value = "Site schedule id", required = true) Integer scheduleId
    );
}
