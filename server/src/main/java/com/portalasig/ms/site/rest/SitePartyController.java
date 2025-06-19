package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
import com.portalasig.ms.site.operations.SitePartyOperations;
import com.portalasig.ms.site.service.SitePartyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling site parties.
 * Implements SitePartyOperations.
 */
@RestController
@RequiredArgsConstructor
@Api(value = "Site Party Management Controller", tags = "Site Management")
@Slf4j
public class SitePartyController implements SitePartyOperations {

    private final SitePartyService sitePartyService;

    @Override
    public Site bulkPatchParties(Integer siteId, SitePartyRequest request) {
        return sitePartyService.processBulkPatchParties(siteId, request);
    }

    @Override
    public Site deleteParty(Integer siteId, Integer partyId) {
        return sitePartyService.deleteParty(siteId, partyId);
    }
}
