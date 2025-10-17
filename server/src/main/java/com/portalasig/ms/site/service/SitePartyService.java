package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.converter.SiteConverter;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SitePartyEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartiesRequest;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.mapper.SitePartyMapper;
import com.portalasig.ms.site.record.IdentityPartyRole;
import com.portalasig.ms.site.record.UserInformation;
import com.portalasig.ms.site.repository.SiteRepository;
import com.portalasig.ms.uaa.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

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

    /**
     * Performs a bulk update (patch) of parties associated with a site.
     * after the association is completed, notify user via email
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
     * @param siteId  the site identifier
     * @param partyId the party identifier to remove
     * @return the updated Site DTO
     */
    public Site deleteParty(Integer siteId, Integer partyId) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("site_id=%s not found", siteId))
        );
        SitePartyEntity partyToRemove = siteEntity
                .getParties()
                .stream()
                .filter(party -> party.getPartyId().equals(partyId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("party_id=%s not found on site_id=%s", partyId, siteId))
                );
        siteEntity.getParties().remove(partyToRemove);
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
    public SiteParty findParty(Integer siteId, Integer partyId, PartyRole partyRole) {
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
        return partyRole == null || partyRole.equals(party.getPartyRole()) && Objects.equals(party.getPartyId(), partyId);
    }

}
