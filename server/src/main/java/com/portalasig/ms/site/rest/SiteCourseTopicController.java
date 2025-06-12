package com.portalasig.ms.site.rest;

import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteCourseTopicRequest;
import com.portalasig.ms.site.operations.SiteCourseTopicOperations;
import com.portalasig.ms.site.service.SiteCourseTopicService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling site course topics.
 * Implements SiteCourseTopicOperations.
 */
@RestController
@RequiredArgsConstructor
@Api(value = "Site Course Topic Management Controller", tags = "Site Management")
@Slf4j
public class SiteCourseTopicController implements SiteCourseTopicOperations {

    private final SiteCourseTopicService siteCourseTopicService;

    @Override
    public Site upsertSiteCourseTopic(SiteCourseTopicRequest request, Integer siteId) {
        log.info("Upserting site course topic for site_id={}", siteId);
        return siteCourseTopicService.upsertCourseTopic(request, siteId);
    }

    @Override
    public Site deleteSiteCourseTopic(Integer siteId, Integer siteCourseTopicId) {
        log.info("Deleting site_course_topic_id={} for site_id={}", siteCourseTopicId, siteId);
        return siteCourseTopicService.deleteCourseTopic(siteCourseTopicId, siteId);
    }
}
