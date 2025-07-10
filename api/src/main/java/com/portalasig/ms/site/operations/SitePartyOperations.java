package com.portalasig.ms.site.operations;

import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartiesRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

/**
 * REST operations for managing site parties.
 */
@HttpExchange(SiteRestConstant.SiteParty.Path.BASE)
public interface SitePartyOperations {

    /**
     * Updates parties in bulk for a given site.
     */
    @PatchExchange(SiteRestConstant.SiteParty.Path.LIST)
    Site bulkPatchParties(
            @PathVariable @ApiParam(value = "Site ID", required = true) Integer siteId,
            @RequestBody @Valid @ApiParam(value = "Site Party Request", required = true) SitePartiesRequest request
    );

    /**
     * Deletes a site party for the specified site and party ID.
     *
     * @param siteId  the ID of the site
     * @param partyId the ID of the party to delete
     * @return the updated Site object
     */
    @ApiOperation(value = "Delete a site course topic", response = Site.class)
    @DeleteExchange(SiteRestConstant.SiteParty.Path.ELEMENT)
    Site deleteParty(
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId,
            @PathVariable @ApiParam(value = "party id", required = true) Integer partyId
    );
}
