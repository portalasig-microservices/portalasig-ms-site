package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ConflictException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.commons.rest.exception.SystemErrorException;
import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.converter.SiteConverter;
import com.portalasig.ms.site.domain.entity.SemesterEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartiesRequest;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.dto.site.SiteRequest;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.record.IdentityPartyRole;
import com.portalasig.ms.site.record.UserInformation;
import com.portalasig.ms.site.repository.CourseRepository;
import com.portalasig.ms.site.repository.SemesterRepository;
import com.portalasig.ms.site.repository.SiteRepository;
import com.portalasig.ms.uaa.dto.User;
import com.portalasig.ms.uaa.operation.AdminUserOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for handling business logic related to course sites.
 * Includes creation, retrieval, and bulk update of site parties.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SiteService {

    private final CourseRepository courseRepository;
    private final SiteRepository siteRepository;
    private final SemesterRepository semesterRepository;
    private final SiteMapper siteMapper;
    private final SiteConverter siteConverter;
    @Qualifier("tokenRelayAdminUserClientV1")
    private final AdminUserOperations adminUserOperations;

    /**
     * Creates a new site for the specified course and academic period.
     * Throws ConflictException if the site already exists.
     * Also initializes the site's parties if provided.
     *
     * @param request the site request containing course code and period details
     * @return the newly created Site DTO
     */
    public Site createSite(SiteRequest request) {
        validateSite(request);
        CourseEntity course = courseRepository.findByCode(request.getCourseCode())
                .orElseThrow(ResourceNotFoundException::new);

        SemesterEntity semester = semesterRepository.findByAcademicPeriod(
                request.getPeriodType(),
                request.getPeriodYear()
        ).orElseThrow(() -> new SystemErrorException(
                String.format(
                        "Semester with academic_period=%s-%s not found",
                        request.getPeriodType(),
                        request.getPeriodYear()
                )));
        SiteEntity siteEntity = SiteEntity
                .builder()
                .course(course)
                .semester(semester)
                .build();
        var identityPartyRoleToUserMap = createIdentityPartyRoleMap(request.getParties());
        siteConverter.createPartiesEntities(siteEntity, identityPartyRoleToUserMap);
        siteEntity = siteRepository.save(siteEntity);
        log.info("Site with site_id={} was successfully created", siteEntity.getSiteId());
        log.debug("DEBUG -- Full object: {}", siteEntity);

        return siteMapper.toDto(siteEntity);
    }

    /**
     * Finds a site by course code, academic period type, and period year.
     * Throws ResourceNotFoundException if no site is found.
     *
     * @param courseCode the course code
     * @param periodType the academic period type
     * @param periodYear the academic period year
     * @return the matching Site DTO
     */
    public Site findSite(String courseCode, AcademicPeriodType periodType, Integer periodYear) {
        SiteEntity siteEntity = siteRepository.findSite(
                courseCode,
                periodType,
                periodYear
        ).orElseThrow(ResourceNotFoundException::new);
        return siteMapper.toDto(siteEntity);
    }

    /**
     * Deletes a site by its ID.
     * Throws ResourceNotFoundException if the site does not exist.
     *
     * @param siteId the ID of the site to delete
     */
    public void deleteSite(Integer siteId) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Site with site_id=%s not found", siteId))
        );
        log.info("Attempting to delete site_id={}", siteId);
        siteRepository.delete(siteEntity);
    }

    /**
     * Creates a map of IdentityPartyRole to UserInformation from a SitePartyRequest.
     *
     * @param request the SitePartyRequest containing party data
     * @return a map linking IdentityPartyRole to UserInformation
     */
    public Map<IdentityPartyRole, UserInformation> createIdentityPartyRoleMap(SitePartiesRequest request) {
        var identities = request
                .getParties()
                .stream()
                .map(SiteParty::getIdentity)
                .toList();

        List<User> users = adminUserOperations.getUsers(identities);
        var identityToUserMap = users
                .stream()
                .collect(Collectors.toMap(User::getIdentity, user -> user));

        return request.getParties().stream()
                .collect(Collectors.toMap(
                        siteParty -> new IdentityPartyRole(siteParty.getIdentity(), siteParty.getPartyRole()),
                        siteParty -> {
                            User uaaUser = identityToUserMap.get(siteParty.getIdentity());
                            return new UserInformation(uaaUser, siteParty);
                        }
                ));
    }

    /**
     * Creates a map of IdentityPartyRole to UserInformation from a list of SiteParty.
     *
     * @param siteParties the list of SiteParty
     * @return a map linking IdentityPartyRole to UserInformation
     */
    private Map<IdentityPartyRole, UserInformation> createIdentityPartyRoleMap(List<SiteParty> siteParties) {
        var identities = siteParties
                .stream()
                .map(SiteParty::getIdentity).toList();
        List<User> parties = adminUserOperations.getUsers(identities);
        var identityToUserMap = parties.stream().collect(Collectors.toMap(User::getIdentity, user -> user));
        return siteConverter.toIdentityPartyRoleMap(siteParties, identityToUserMap);
    }

    /**
     * Validates that a site does not already exist for the given course and academic period.
     * Throws ConflictException if a site already exists.
     *
     * @param request the site request to validate
     */
    private void validateSite(SiteRequest request) {
        Optional<SiteEntity> siteOptional = siteRepository.findSite(
                request.getCourseCode(),
                request.getPeriodType(),
                request.getPeriodYear()
        );
        if (siteOptional.isPresent()) {
            throw new ConflictException("Course already exists");
        }
    }
}
