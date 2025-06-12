package com.portalasig.ms.site.operations;

import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteCourseTopicRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * REST operations for managing site course topics.
 */
@HttpExchange(SiteRestConstant.SiteCourseTopic.Path.BASE)
public interface SiteCourseTopicOperations {

    /**
     * Upserts (creates or updates) a site course topic for the specified site.
     *
     * @param request the site course topic request data
     * @param siteId  the ID of the site
     * @return the updated Site object
     */
    @ApiOperation(value = "Upsert a site course topic", response = Site.class)
    @PostExchange
    Site upsertSiteCourseTopic(
            @RequestBody
            @ApiParam(value = "Site course topic request", required = true)
            SiteCourseTopicRequest request,
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId
    );

    /**
     * Deletes a site course topic for the specified site and course topic ID.
     *
     * @param siteId            the ID of the site
     * @param siteCourseTopicId the ID of the course topic to delete
     * @return the updated Site object
     */
    @ApiOperation(value = "Delete a site course topic", response = Site.class)
    @DeleteExchange(SiteRestConstant.SiteCourseTopic.Path.ELEMENT)
    Site deleteSiteCourseTopic(
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId,
            @PathVariable @ApiParam(value = "Course objective id", required = true) Integer siteCourseTopicId
    );
}
