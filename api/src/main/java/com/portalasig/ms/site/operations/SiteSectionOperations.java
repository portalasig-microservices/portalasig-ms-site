package com.portalasig.ms.site.operations;

import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteSectionRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * REST client interface for managing site sections within a site.
 * <p>
 * This interface defines operations for creating/updating and deleting site sections
 * using HTTP exchanges, typically implemented via Spring WebClient.
 *
 * @see Site
 * @see SiteSectionRequest
 */
@HttpExchange(SiteRestConstant.SiteSection.Path.BASE)
public interface SiteSectionOperations {

    /**
     * Creates or updates a site section associated with a given site.
     * <p>
     * This operation performs an upsert: if the section exists, it will be updated; otherwise, a new section will be created.
     *
     * @param siteId  the ID of the site to which the section belongs
     * @param request the details of the site section to be created or updated
     * @return the updated {@link Site} object with the new or modified section
     */
    @ApiOperation(value = "Upsert site section", response = Site.class)
    @PostExchange
    Site upsertSiteSection(
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId,
            @RequestBody
            @ApiParam(value = "Site section request", required = true)
            SiteSectionRequest request
    );

    /**
     * Deletes a site section by its ID from a given site.
     *
     * @param siteId    the ID of the site from which the section will be deleted
     * @param sectionId the ID of the section to delete
     * @return the updated {@link Site} object after the section has been removed
     */
    @ApiOperation(value = "Delete section by id", response = Site.class)
    @DeleteExchange(SiteRestConstant.SiteSection.Path.ELEMENT)
    Site deleteSiteSection(
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId,
            @PathVariable @ApiParam(value = "Site section id", required = true) Integer sectionId
    );
}