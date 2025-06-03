package com.portalasig.ms.site.converter;

import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.record.IdentityPartyRole;
import com.portalasig.ms.site.record.UserInformation;
import com.portalasig.ms.uaa.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Site converter.
 */
@Component
@RequiredArgsConstructor
public class SiteConverter {

    /**
     * Converts a list of SiteParty objects and a map of user identities to users
     * into a map of IdentityPartyRole to UserInformation.
     *
     * @param parties      the list of SiteParty objects
     * @param identityUser a map of user identity IDs to User objects
     * @return a map where the key is IdentityPartyRole and the value is UserInformation
     */
    public Map<IdentityPartyRole, UserInformation> toIdentityPartyRoleMap(List<SiteParty> parties, Map<Long, User> identityUser) {
        if (parties == null || parties.isEmpty() || identityUser == null || identityUser.isEmpty()) {
            return new HashMap<>();
        }

        return parties
                .stream()
                .collect(Collectors.toMap(
                        party -> new IdentityPartyRole(party.getIdentity(), party.getPartyRole()),
                        party -> new UserInformation(identityUser.get(party.getIdentity()), party),
                        (existing, replacement) -> existing
                ));
    }
}
