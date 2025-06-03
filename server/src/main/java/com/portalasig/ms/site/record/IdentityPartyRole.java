package com.portalasig.ms.site.record;

import com.portalasig.ms.site.constant.PartyRole;

/**
 * Record that represents a mapping between an identity and a party role.
 *
 * @param identity  the unique identifier of the user or identity
 * @param partyRole the role that the identity holds in the context of a site (e.g., PROFESSOR, STUDENT)
 */
public record IdentityPartyRole(Long identity, PartyRole partyRole) {
}

