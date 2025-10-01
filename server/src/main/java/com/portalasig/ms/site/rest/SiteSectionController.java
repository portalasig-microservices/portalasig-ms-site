package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteSection;
import com.portalasig.ms.site.dto.site.SiteSectionRequest;
import com.portalasig.ms.site.operations.SiteSectionOperations;
import com.portalasig.ms.site.service.SiteSectionService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing site sections.
 */
@RestController
@RequiredArgsConstructor
@Api(value = "Site section controller", tags = "Site Management")
@Slf4j
public class SiteSectionController implements SiteSectionOperations {

    private final SiteSectionService siteSectionService;

    @Override
    public Site upsertSiteSection(Integer siteId, SiteSectionRequest request) {
        return siteSectionService.upsertSection(siteId, request);
    }

    @Override
    public Site deleteSiteSection(Integer siteId, Integer sectionId) {
        return siteSectionService.deleteSection(siteId, sectionId);
    }

    @Override
    public List<SiteSection> searchSectionByCode(
            Integer siteId,
            SiteSectionRequest request
    ) {
        return siteSectionService.searchSectionByCode(siteId, request.getCode());
    }
}
