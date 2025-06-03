package com.portalasig.ms.site.dto.site;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request body containing a list of site parties.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A site request")
public class SitePartyRequest {

    @ApiModelProperty(value = "Site parties")
    List<SiteParty> parties;
}
