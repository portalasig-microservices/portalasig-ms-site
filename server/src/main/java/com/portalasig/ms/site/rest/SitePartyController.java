package com.portalasig.ms.site.rest;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartiesRequest;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
import com.portalasig.ms.site.operations.SitePartyOperations;
import com.portalasig.ms.site.service.SitePartyService;
import com.portalasig.ms.site.service.StudentImportUseCase;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private final StudentImportUseCase studentImportUseCase;

    @Override
    public void addPartyToSite(Integer siteId, SitePartyRequest request) {
        log.info("adding party={} to site_id={}", request, siteId);
        sitePartyService.addPartyToSite(siteId, request);
    }

    @Override
    public Paginated<SiteParty> getStudents(Integer siteId, Pageable pageable) {
        return sitePartyService.getStudents(siteId, pageable);
    }

    @Override
    public Site bulkPatchParties(Integer siteId, SitePartiesRequest request) {
        return sitePartyService.processBulkPatchParties(siteId, request);
    }

    @Override
    public Site deletePartyByIdentity(Integer siteId, Long identity, PartyRole partyRole, Integer sectionId) {
        return sitePartyService.deletePartyByIdentity(siteId, identity, partyRole, sectionId);
    }

    @Override
    public SiteParty getPartyById(
            Integer siteId,
            Integer partyId,
            PartyRole partyRole
    ) {
        return sitePartyService.getPartyByPartyId(siteId, partyId, partyRole);
    }

    @Override
    public List<SiteParty> findParties(
            Integer siteId,
            String query,
            List<PartyRole> userRoles
    ) {
        return sitePartyService.findParties(siteId, query, userRoles);
    }

    @Override
    public void upsertStudentsFromCsv(Integer siteId, MultipartFile file) {
        studentImportUseCase.upsertStudentsFromExcel(siteId, file);
    }


}
