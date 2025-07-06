package com.portalasig.ms.site.email.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the template for site party emails, including title, body content, URL, and closing message.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SitePartyEmailTemplate {

    private String title;
    private String target;
    private String primaryBody;
    private String secondaryBody;
    private String url;
    private String urlLabel;
    private String closingMessage;
}
