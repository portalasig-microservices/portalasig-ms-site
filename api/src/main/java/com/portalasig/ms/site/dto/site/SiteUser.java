package com.portalasig.ms.site.dto.site;

import com.portalasig.ms.site.constant.SiteUserRoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A Site user. Represents a user in the system that is related to a site. Can be a student, coordinator or teacher")
public class SiteUser {

    @ApiModelProperty(value = "Site user id")
    private Integer siteUserId;

    @ApiModelProperty(value = "ms-uaa user id")
    private Integer userId;

    @ApiModelProperty(value = "User First Name")
    private String firstName;

    @ApiModelProperty(value = "User last name")
    private String lastName;

    @ApiModelProperty(value = "User Identity")
    private String identity;

    @ApiModelProperty(value = "User email")
    private String email;

    @ApiModelProperty(value = "User current site roles")
    private List<SiteUserRoleType> siteUserRoles;

}
