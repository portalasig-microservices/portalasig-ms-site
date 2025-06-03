package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ConflictException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.commons.rest.exception.SystemErrorException;
import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.converter.SiteConverter;
import com.portalasig.ms.site.domain.entity.SemesterEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SitePartyEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
        createParties(siteEntity, identityPartyRoleToUserMap);
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
     * Performs a bulk update (patch) of parties associated with a site.
     * Throws ResourceNotFoundException if the site is not found.
     *
     * @param siteId  the site identifier
     * @param request the request containing new parties data
     * @return the updated Site DTO
     */
    public Site processBulkPatchParties(Integer siteId, SitePartyRequest request) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Site with site_id=%s not found", siteId))
        );
        Map<IdentityPartyRole, UserInformation> identityPartyRoleToUserMap = createIdentityPartyRoleMap(request);
        patchParties(siteEntity, identityPartyRoleToUserMap);
        siteEntity = siteRepository.save(siteEntity);
        return siteMapper.toDto(siteEntity);
    }

    /**
     * Creates a map of IdentityPartyRole to UserInformation from a SitePartyRequest.
     *
     * @param request the SitePartyRequest containing party data
     * @return a map linking IdentityPartyRole to UserInformation
     */
    private Map<IdentityPartyRole, UserInformation> createIdentityPartyRoleMap(SitePartyRequest request) {
        var identities = request
                .getParties()
                .stream()
                .map(SiteParty::getIdentity).toList();
        List<User> parties = adminUserOperations.getUsers(identities);

        var identityToUserMap = parties.stream().collect(Collectors.toMap(User::getIdentity, user -> user));
        return siteConverter.toIdentityPartyRoleMap(request.getParties(), identityToUserMap);
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
     * Adds new parties to the given site entity based on the provided map.
     *
     * @param siteEntity                 the site entity to update
     * @param identityPartyRoleToUserMap the map of IdentityPartyRole to UserInformation
     */
    private void createParties(SiteEntity siteEntity, Map<IdentityPartyRole, UserInformation> identityPartyRoleToUserMap) {
        if (siteEntity.getParties() == null) {
            siteEntity.setParties(new HashSet<>());
        }
        var existingParties = siteEntity.getParties();

        Set<IdentityPartyRole> existingKeys = existingParties.stream()
                .map(p -> new IdentityPartyRole(p.getIdentity(), p.getPartyRole()))
                .collect(Collectors.toSet());

        identityPartyRoleToUserMap.values().forEach(userInformation -> {
            IdentityPartyRole key = new IdentityPartyRole(
                    userInformation.user().getIdentity(),
                    userInformation.siteParty().getPartyRole()
            );

            if (!existingKeys.contains(key)) {
                SitePartyEntity newParty = SitePartyEntity.builder()
                        .identity(key.identity())
                        .email(userInformation.user().getEmail())
                        .firstName(userInformation.user().getFirstName())
                        .lastName(userInformation.user().getLastName())
                        .partyRole(key.partyRole())
                        .build();
                newParty.setSite(siteEntity);
                existingParties.add(newParty);
                existingKeys.add(key);
            }
        });
    }

    /**
     * Updates existing parties in the site entity using the given map,
     * and creates new parties for remaining entries in the map.
     *
     * @param siteEntity                 the site entity to patch
     * @param identityPartyRoleToUserMap the map of IdentityPartyRole to UserInformation
     */
    private void patchParties(
            SiteEntity siteEntity,
            Map<IdentityPartyRole, UserInformation> identityPartyRoleToUserMap
    ) {
        if (siteEntity.getParties() == null) {
            siteEntity.setParties(new HashSet<>());
        }
        var existingParties = siteEntity.getParties();

        existingParties.removeIf(existingParty -> {
            var composeKey = new IdentityPartyRole(existingParty.getIdentity(), existingParty.getPartyRole());
            UserInformation userInformation = identityPartyRoleToUserMap.remove(composeKey);
            if (userInformation != null) {
                User user = userInformation.user();
                existingParty.setEmail(user.getEmail());
                existingParty.setFirstName(user.getFirstName());
                existingParty.setLastName(user.getLastName());
                return false;
            } else {
                return true;
            }
        });
        createParties(siteEntity, identityPartyRoleToUserMap);
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
