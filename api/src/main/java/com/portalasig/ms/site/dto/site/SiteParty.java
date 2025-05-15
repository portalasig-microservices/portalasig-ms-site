package com.portalasig.ms.site.dto.site;

import com.portalasig.ms.site.constant.PartyRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A Site Party. Represents a user in the system that is related to a site. Can be a student, coordinator or teacher")
public class SiteParty {

    @ApiModelProperty(value = "User Identity")
    @NotNull
    private Long identity;

    @ApiModelProperty(value = "User current site roles")
    @NotNull
    private PartyRole partyRole;

    @ApiModelProperty(value = "User First Name")
    private String firstName;

    @ApiModelProperty(value = "User last name")
    private String lastName;

    @ApiModelProperty(value = "User email")
    private String email;

}
