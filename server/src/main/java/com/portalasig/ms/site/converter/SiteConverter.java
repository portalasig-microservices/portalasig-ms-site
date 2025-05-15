package com.portalasig.ms.site.converter;

import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SitePartyEntity;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.uaa.dto.User;
import com.portalasig.ms.uaa.operation.AdminUserOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

record UserInformation(User user, SiteParty siteParty) {
}

record ComposeKey(Long identity, PartyRole partyRole) {
}

@Component
@RequiredArgsConstructor
public class SiteConverter {

    @Qualifier("tokenRelayAdminUserClientV1")
    private final AdminUserOperations adminUserOperations;

    public void patchParties(SiteEntity siteEntity, List<SiteParty> incomingParties) {
        if (incomingParties == null || incomingParties.isEmpty()) {
            return;
        }
        if (siteEntity.getParties() == null) {
            siteEntity.setParties(new HashSet<>());
        }

        var identities = incomingParties.stream().map(SiteParty::getIdentity).toList();
        List<User> users = adminUserOperations.getUsers(identities);
        var usersMap = users.stream().collect(Collectors.toMap(User::getIdentity, user -> user));

        Map<ComposeKey, UserInformation> incomingPartiesMap = incomingParties
                .stream()
                .collect(Collectors.toMap(
                        party -> new ComposeKey(party.getIdentity(), party.getPartyRole()),
                        party -> new UserInformation(usersMap.get(party.getIdentity()), party),
                        (existing, replacement) -> existing
                ));

        patchExistingParties(siteEntity, incomingPartiesMap);
        createNewParties(siteEntity, incomingPartiesMap);
    }

    private void patchExistingParties(SiteEntity siteEntity, Map<ComposeKey, UserInformation> incomingPartiesMap) {
        var existingParties = siteEntity.getParties();
        existingParties.forEach(existingParty -> {
            var composeKey = new ComposeKey(existingParty.getIdentity(), existingParty.getPartyRole());
            UserInformation userInformation = incomingPartiesMap.remove(composeKey);  // ✅ CORRECTO
            if (userInformation != null) {
                User user = userInformation.user();
                existingParty.setEmail(user.getEmail());
                existingParty.setFirstName(user.getFirstName());
                existingParty.setLastName(user.getLastName());
            }
        });
    }

    private void createNewParties(SiteEntity siteEntity, Map<ComposeKey, UserInformation> incomingPartiesMap) {
        var existingParties = siteEntity.getParties();
        incomingPartiesMap.values().forEach(userInformation -> {
            SitePartyEntity newParty = SitePartyEntity.builder()
                    .identity(userInformation.user().getIdentity())
                    .email(userInformation.user().getEmail())
                    .firstName(userInformation.user().getFirstName())
                    .lastName(userInformation.user().getLastName())
                    .partyRole(userInformation.siteParty().getPartyRole())
                    .build();
            newParty.setSite(siteEntity);
            existingParties.add(newParty);
        });
    }
}
