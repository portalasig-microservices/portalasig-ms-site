package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.commons.rest.exception.BadRequestException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.converter.SiteConverter;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SitePartyEntity;
import com.portalasig.ms.site.domain.entity.site.SiteSectionEntity;
import com.portalasig.ms.site.domain.entity.site.SiteStudentEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartiesRequest;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.mapper.SitePartyMapper;
import com.portalasig.ms.site.mapper.SiteStudentMapper;
import com.portalasig.ms.site.record.IdentityPartyRole;
import com.portalasig.ms.site.record.UserInformation;
import com.portalasig.ms.site.repository.SiteRepository;
import com.portalasig.ms.site.repository.SiteStudentRepository;
import com.portalasig.ms.uaa.dto.User;
import com.portalasig.ms.uaa.operation.UserOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for managing parties associated with a site.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SitePartyService {

    private final SiteRepository siteRepository;
    private final SiteService siteService;
    private final SiteMapper siteMapper;
    private final SiteConverter siteConverter;
    private final SiteNotificationService siteNotificationService;
    private final SitePartyMapper sitePartyMapper;
    private final SiteStudentMapper siteStudentMapper;

    @Value("${ms.site.rest.find-party.max-result-size:10}")
    private final Integer maxResultSize;
    private final SiteStudentRepository siteStudentRepository;
    @Qualifier("clientCredentialsUserClientV1")
    private final UserOperations userOperations;

    /**
     * Performs a bulk update (patch) of parties associated with a site.
     * After the association is completed, notifies users via email.
     * Throws ResourceNotFoundException if the site is not found.
     *
     * @param siteId  the site identifier
     * @param request the request containing new parties data
     * @return the updated Site DTO
     */
    public Site processBulkPatchParties(Integer siteId, SitePartiesRequest request) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Site with site_id=%s not found", siteId))
        );
        var partiesMap = siteService.createIdentityPartyRoleMap(request);
        patchParties(siteEntity, partiesMap);
        siteEntity = siteRepository.save(siteEntity);
        log.info("site_id={} parties request has been processed", siteId);
        SiteEntity finalSiteEntity = siteEntity;
        partiesMap.forEach(
                (identityPartyRole, userInformation) ->
                        siteNotificationService.notifyPartyAssociation(userInformation, finalSiteEntity)
        );
        return siteMapper.toDto(siteEntity);
    }

    /**
     * Updates existing parties in the site entity using the given map,
     * and creates new parties for remaining entries in the map.
     *
     * @param siteEntity the site entity to patch
     * @param partiesMap the map of IdentityPartyRole to UserInformation
     */
    private void patchParties(
            SiteEntity siteEntity,
            Map<IdentityPartyRole, UserInformation> partiesMap
    ) {
        if (siteEntity.getParties() == null) {
            siteEntity.setParties(new HashSet<>());
        }
        var existingParties = siteEntity.getParties();

        existingParties.forEach(existingParty -> {
            var composeKey = new IdentityPartyRole(existingParty.getIdentity(), existingParty.getPartyRole());
            UserInformation userInformation = partiesMap.remove(composeKey);
            if (userInformation != null) {
                patchParty(userInformation.user(), existingParty);
            }
        });
        siteConverter.createPartiesEntities(siteEntity, partiesMap);
    }

    private void patchParty(
            User user,
            SitePartyEntity existingParty
    ) {
        if (user.getFirstName() != null) {
            existingParty.setFirstName(user.getFirstName());
        }

        if (user.getLastName() != null) {
            existingParty.setLastName(user.getLastName());
        }

        if (user.getEmail() != null) {
            existingParty.setEmail(user.getEmail());
        }
    }

    /**
     * Deletes a party from the specified site by party ID.
     * Throws ResourceNotFoundException if the site or party is not found.
     *
     * @param siteId    the site identifier
     * @param identity  the national identifier number
     * @param partyRole
     * @param sectionId
     * @return the updated Site DTO
     */
    public Site deletePartyByIdentity(Integer siteId, Long identity, PartyRole partyRole, Integer sectionId) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("site_id=%s not found", siteId))
        );
        if (partyRole.isSiteStaff()) {
            SitePartyEntity partyToRemove = siteEntity
                    .getParties()
                    .stream()
                    .filter(party -> Objects.equals(party.getIdentity(), identity))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("party_id=%s not found on site_id=%s", identity, siteId))
                    );
            siteEntity.getParties().remove(partyToRemove);
        } else {
            SiteSectionEntity sectionEntity = siteEntity
                    .getSections()
                    .stream()
                    .filter(section -> Objects.equals(section.getSectionId(), sectionId))
                    .findFirst()
                    .orElseThrow(() ->
                            new ResourceNotFoundException(String.format("section_id=%s not found on site_id=%s", sectionId, siteId))
                    );
            SiteStudentEntity studentEntity = sectionEntity
                    .getStudents()
                    .stream()
                    .filter(student -> Objects.equals(student.getIdentity(), identity))
                    .findFirst()
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    String.format("identity=%s not found on section_id=%s", identity, sectionId)
                            )
                    );
            sectionEntity.getStudents().remove(studentEntity);
        }
        siteEntity = siteRepository.save(siteEntity);
        return siteMapper.toDto(siteEntity);
    }

    /**
     * Finds a party associated with a site by site ID, party ID, and optionally party role.
     * Returns the corresponding SiteParty DTO if found, or null if not found.
     *
     * @param siteId    the site identifier
     * @param partyId   the party identifier
     * @param partyRole the party role to filter by (optional, can be null)
     * @return the SiteParty DTO if found, otherwise null
     * @throws ResourceNotFoundException if the site is not found
     */
    public SiteParty getPartyByPartyId(Integer siteId, Integer partyId, PartyRole partyRole) {
        SiteEntity siteEntity = siteRepository
                .findById(siteId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("site_id=%s not found", siteId))
                );
        SitePartyEntity targetParty = siteEntity
                .getParties()
                .stream()
                .filter(party -> filterPartyByOptionalPartyRoleAndPartyId(party, partyId, partyRole))
                .findFirst()
                .orElse(null);

        return targetParty == null ? null : sitePartyMapper.toDto(targetParty);
    }

    private boolean filterPartyByOptionalPartyRoleAndPartyId(
            SitePartyEntity party,
            Integer partyId,
            PartyRole partyRole
    ) {
        return partyRole == null
                || partyRole.equals(party.getPartyRole()) && Objects.equals(party.getPartyId(), partyId);
    }

    /**
     * Finds parties given a query string.
     *
     * @param siteId     site id
     * @param query      a query string
     * @param partyRoles list of party roles to specify search subgroups
     * @return list of matches
     */
    public List<SiteParty> findParties(Integer siteId, String query, List<PartyRole> partyRoles) {
        List<PartyRole> validRoles = validatePartyRoles(partyRoles);
        SiteEntity siteEntity = siteRepository
                .findById(siteId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("site_id=%s not found", siteId)));
        var siteUsers = new ArrayList<SiteParty>();
        var parties = siteEntity.getParties().stream().map(sitePartyMapper::toDto).toList();
        var students = siteEntity
                .getSections()
                .stream()
                .flatMap(section -> section.getStudents().stream())
                .map(siteStudentMapper::toDto)
                .toList();
        siteUsers.addAll(parties);
        siteUsers.addAll(students);
        Set<SiteParty> matches = filterPartiesByQueryAndPartyRoles(siteUsers, query, validRoles);

        return matches.stream().toList();
    }

    /**
     * Adds a party to the site.
     *
     * @param siteId  site id
     * @param request site party request
     */
    public void addPartyToSite(Integer siteId, SitePartyRequest request) {
        User user = userOperations.getUserByIdentity(request.getIdentity());
        if (user == null) {
            throw new ResourceNotFoundException(String.format("user with identity=%s not found", siteId));
        }
        SiteEntity siteEntity = siteRepository
                .findById(siteId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("site_id=%s not found", siteId)));
        if (request.getPartyRole().isSiteStaff()) {
            addSiteStaff(siteEntity, request, user);
        } else {
            addStudentToSection(siteEntity, request, user);
        }
        siteRepository.save(siteEntity);
        log.info("Party added successfully to site_id={}", siteId);
    }

    private void addSiteStaff(SiteEntity siteEntity, SitePartyRequest request, User user) {
        SitePartyEntity partyEntity = sitePartyMapper.toEntityFromUser(user);
        partyEntity.setPartyRole(request.getPartyRole());
        partyEntity.setPartySiteTitle(request.getPartySiteTitle());
        partyEntity.setSite(siteEntity);
        siteEntity.getParties().add(partyEntity);
    }

    private void addStudentToSection(SiteEntity siteEntity, SitePartyRequest request, User user) {
        if (request.getSectionId() == null) {
            throw new BadRequestException("section_id is null");
        }

        SiteSectionEntity sectionEntity = siteEntity
                .getSections()
                .stream()
                .filter(section -> Objects.equals(section.getSectionId(), request.getSectionId()))
                .findFirst()
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("section_id=%s not found", request.getSectionId())
                        )
                );
        SiteStudentEntity studentEntity = siteStudentMapper.toEntityFromUser(user);
        studentEntity.setPartyRole(request.getPartyRole());
        studentEntity.setPartySiteTitle(request.getPartySiteTitle());
        studentEntity.setSection(sectionEntity);
        sectionEntity.getStudents().add(studentEntity);
    }

    /**
     * Retrieves a paginated list of students for the given site.
     *
     * @param siteId   the site identifier
     * @param pageable pagination information
     * @return a paginated list of SiteParty DTOs representing students
     */
    public Paginated<SiteParty> getStudents(Integer siteId, Pageable pageable) {
        Page<SiteStudentEntity> siteStudents = siteStudentRepository.getStudentsBySiteId(siteId, pageable);
        return Paginated.wrap(siteStudents.map(siteStudentMapper::toDto));
    }

    /**
     * Filters parties by query and party roles.
     *
     * @param parties    list of parties to filter
     * @param query      search query
     * @param partyRoles roles to include
     * @return a set of matching parties
     */
    private Set<SiteParty> filterPartiesByQueryAndPartyRoles(
            List<SiteParty> parties,
            String query,
            List<PartyRole> partyRoles
    ) {
        final String normalizedQuery = normalize(query);
        final boolean hasQuery = !normalizedQuery.isBlank();
        return parties.stream()
                .filter(siteParty -> partyRoles.contains(siteParty.getPartyRole()))
                .filter(siteParty -> {
                    if (!hasQuery) {
                        return true;
                    }
                    final String first = normalize(siteParty.getFirstName());
                    final String last = normalize(siteParty.getLastName());
                    final String email = normalize(siteParty.getEmail());
                    final String identity = normalize(
                            siteParty.getIdentity() == null
                                    ? null
                                    : String.valueOf(siteParty.getIdentity())
                    );

                    final String full = (first + " " + last).trim();
                    final String fullRev = (last + " " + first).trim();

                    return startsWith(first, normalizedQuery)
                            || startsWith(last, normalizedQuery)
                            || startsWith(full, normalizedQuery)
                            || startsWith(fullRev, normalizedQuery)
                            || startsWith(email, normalizedQuery)
                            || startsWith(identity, normalizedQuery);
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Validates the provided party roles list.
     * Defaults to {@link PartyRole#STUDENT} when the list is null or empty.
     *
     * @param partyRoles list of roles to validate
     * @return a non-empty list of party roles
     */
    private List<PartyRole> validatePartyRoles(List<PartyRole> partyRoles) {
        if (partyRoles == null || partyRoles.isEmpty()) {
            partyRoles = List.of(PartyRole.STUDENT);
        }
        return partyRoles;
    }

    private static boolean startsWith(String value, String prefix) {
        if (value == null || prefix == null) {
            return false;
        }
        return value.startsWith(prefix);
    }

    /**
     * Normalizes a string, removing special characters, accents and others.
     *
     * @param query string that represents the partial search
     * @return normalized query
     */
    private static String normalize(String query) {
        if (query == null) {
            return "";
        }
        String trimmed = query.trim().toLowerCase(Locale.ROOT);
        String decomposed = Normalizer.normalize(trimmed, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{M}+", "");
    }
}
