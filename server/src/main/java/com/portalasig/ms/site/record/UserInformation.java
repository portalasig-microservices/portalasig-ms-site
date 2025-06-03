package com.portalasig.ms.site.record;

import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.uaa.dto.User;

/**
 * Record that encapsulates a {@link User} and their corresponding {@link SiteParty} information.
 *
 * @param user      the user data, typically from the identity service
 * @param siteParty the site-specific party details assigned to the user
 */
public record UserInformation(User user, SiteParty siteParty) {
}
