package com.portalasig.ms.site.dto.site;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A course site news")
public class SiteNews {

    @ApiModelProperty(value = "Site news id")
    private Integer siteNewsId;

    @ApiModelProperty(value = "News title")
    private String title;

    @ApiModelProperty(value = "News description or body")
    private String description;

}
