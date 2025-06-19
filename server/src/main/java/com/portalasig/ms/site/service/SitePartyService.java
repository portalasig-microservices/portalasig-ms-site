package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.converter.SiteConverter;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SitePartyEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.record.IdentityPartyRole;
import com.portalasig.ms.site.record.UserInformation;
import com.portalasig.ms.site.repository.SiteRepository;
import com.portalasig.ms.uaa.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;

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
        var identityPartyRoleToUserMap = siteService.createIdentityPartyRoleMap(request);
        patchParties(siteEntity, identityPartyRoleToUserMap);
        siteEntity = siteRepository.save(siteEntity);
        return siteMapper.toDto(siteEntity);
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
        siteConverter.createPartiesEntities(siteEntity, identityPartyRoleToUserMap);
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
}
