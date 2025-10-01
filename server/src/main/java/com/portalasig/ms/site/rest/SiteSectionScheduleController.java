package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteScheduleRequest;
import com.portalasig.ms.site.operations.SiteSectionScheduleOperations;
import com.portalasig.ms.site.service.SiteScheduleService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing site section schedules.
 * Provides endpoints to upsert and delete schedules for site sections.
 */
@RestController
@RequiredArgsConstructor
@Api(value = "Site section schedule controller", tags = "Site Management")
@Slf4j
public class SiteSectionScheduleController implements SiteSectionScheduleOperations {

    private final SiteScheduleService siteScheduleService;

    @Override
    public Site upsertSchedule(Integer siteId, SiteScheduleRequest request) {
        return siteScheduleService.upsertSchedule(siteId, request);
    }

    @Override
    public Site deleteSchedule(Integer siteId, Integer sectionId, Integer scheduleId) {
        return siteScheduleService.deleteSchedule(siteId, sectionId, scheduleId);
    }
}
