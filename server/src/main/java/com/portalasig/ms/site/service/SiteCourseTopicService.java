package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.domain.entity.site.SiteCourseTopicEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteCourseTopicRequest;
import com.portalasig.ms.site.mapper.SiteCourseTopicMapper;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;

/**
 * Service class for managing course topics assigned to a site.
 * Provides methods to create, update, and delete course topics for a given site.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SiteCourseTopicService {

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;
    private final SiteCourseTopicMapper siteCourseTopicMapper;

    /**
     * Creates or updates a course topic in the specified site.
     * If the request contains a null ID, a new course topic is created;
     * otherwise, the existing topic is updated.
     *
     * @param request the request payload with topic data
     * @param siteId  the ID of the site
     * @return the updated site DTO
     * @throws ResourceNotFoundException if the site does not exist
     */
    public Site upsertCourseTopic(SiteCourseTopicRequest request, Integer siteId) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        if (request.getSiteCourseTopicId() == null) {
            createCourseTopic(siteEntity, request);
        } else {
            updateCourseTopic(siteEntity, request);
        }

        siteEntity = siteRepository.save(siteEntity);
        log.info("Site course topic has been upserted in site_id={}", siteId);
        return siteMapper.toDto(siteEntity);
    }

    /**
     * Adds a new course topic to the specified site.
     *
     * @param siteEntity the site entity to which the topic is added
     * @param request    the request containing the topic data
     */
    private void createCourseTopic(SiteEntity siteEntity, SiteCourseTopicRequest request) {
        SiteCourseTopicEntity newCourseTopic = siteCourseTopicMapper.toEntityFromRequest(request);
        newCourseTopic.setSite(siteEntity);

        if (siteEntity.getCourseTopics() == null) {
            siteEntity.setCourseTopics(new HashSet<>());
        }

        siteEntity.getCourseTopics().add(newCourseTopic);
    }

    /**
     * Updates an existing course topic in the specified site.
     *
     * @param siteEntity the site entity that contains the topic
     * @param request    the request with updated topic data
     * @throws ResourceNotFoundException if the topic ID is not found in the site
     */
    private void updateCourseTopic(SiteEntity siteEntity, SiteCourseTopicRequest request) {
        SiteCourseTopicEntity existingCourseTopic = siteEntity
                .getCourseTopics()
                .stream()
                .filter(courseTopic -> Objects.equals(
                        courseTopic.getSiteCourseTopicId(), request.getSiteCourseTopicId()
                ))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("site_course_topic=%d to edit not found", request.getSiteCourseTopicId()))
                );

        siteCourseTopicMapper.toEntityFromExisting(existingCourseTopic, request);
    }

    /**
     * Deletes a course topic from the specified site.
     *
     * @param siteCourseTopicId the ID of the course topic to delete
     * @param siteId            the ID of the site
     * @return the updated site DTO without the deleted topic
     * @throws ResourceNotFoundException if the site or topic does not exist
     */
    public Site deleteCourseTopic(Integer siteCourseTopicId, Integer siteId) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        SiteCourseTopicEntity courseTopic = siteEntity
                .getCourseTopics()
                .stream()
                .filter(cT -> cT.getSiteCourseTopicId().equals(siteCourseTopicId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("site_course_topic_id=%d not found", siteCourseTopicId)
                ));

        siteEntity.getCourseTopics().remove(courseTopic);
        siteRepository.save(siteEntity);

        log.info("Site course topic with id={} deleted from site_id={}", siteCourseTopicId, siteId);
        return siteMapper.toDto(siteEntity);
    }
}
