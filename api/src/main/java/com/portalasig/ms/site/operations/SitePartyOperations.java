package com.portalasig.ms.site.operations;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartiesRequest;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * REST operations for managing site parties.
 */
@HttpExchange(SiteRestConstant.SiteParty.Path.BASE)
public interface SitePartyOperations {

    /**
     * Add party to site.
     *
     * @param siteId  site id
     * @param request site party
     */
    @PostExchange
    void addPartyToSite(
            @PathVariable @ApiParam(value = "Site ID", required = true) Integer siteId,
            @RequestBody @Valid @ApiParam(value = "Site Party Request", required = true) SitePartyRequest request
    );

    /**
     * Get all students from site.
     *
     * @param siteId   site id
     * @param pageable pageable
     * @return
     */
    @GetExchange(SiteRestConstant.SiteParty.Path.STUDENT)
    Paginated<SiteParty> getStudents(
            @PathVariable @ApiParam(value = "Site ID", required = true) Integer siteId,
            Pageable pageable
    );

    /**
     * Updates parties in bulk for a given site.
     */
    @PatchExchange(SiteRestConstant.SiteParty.Path.LIST)
    Site bulkPatchParties(
            @PathVariable @ApiParam(value = "Site ID", required = true) Integer siteId,
            @RequestBody @Valid @ApiParam(value = "Site Party Request", required = true) SitePartiesRequest request
    );

    /**
     * Deletes a site party for the specified site and identity.
     *
     * @param siteId   the ID of the site
     * @param identity the national identity number
     * @return the updated Site object
     */
    @ApiOperation(value = "Delete a site party by identity", response = Site.class)
    @DeleteExchange(SiteRestConstant.SiteParty.Path.IDENTITY)
    Site deletePartyByIdentity(
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId,
            @PathVariable @ApiParam(value = "identity", required = true) Long identity,
            @RequestParam("party_role") @ApiParam(value = "Party role") PartyRole partyRole,
            @RequestParam(value = "section_id", required = false) @ApiParam(value = "Section id") Integer sectionId
    );

    /**
     * Find a site party for the specified site and party ID.
     *
     * @param siteId  the ID of the site
     * @param partyId the ID of the party to delete
     * @return the updated Site object
     */
    @ApiOperation(value = "Find a party within a site by site id, party id and role", response = SiteParty.class)
    @GetExchange(SiteRestConstant.SiteParty.Path.ELEMENT)
    SiteParty getPartyById(
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId,
            @PathVariable @ApiParam(value = "party id", required = true) Integer partyId,
            @RequestParam(name = "party_role", required = false) @ApiParam(value = "Party Role") PartyRole partyRole
    );

    /**
     * Find parties given a query. It can search by name and identity number.
     *
     * @param siteId
     * @param query
     * @param partyRoles
     * @return a list of matching parties
     */
    @ApiOperation(value = "Find a party within a site by site id, party id and role", response = SiteParty.class)
    @GetExchange(SiteRestConstant.SiteParty.Path.FIND)
    List<SiteParty> findParties(
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId,
            @RequestParam String query,
            @RequestParam(value = "party_roles", required = false) List<PartyRole> partyRoles
    );
}
