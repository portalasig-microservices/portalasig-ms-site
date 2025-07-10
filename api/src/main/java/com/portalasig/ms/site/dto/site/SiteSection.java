package com.portalasig.ms.site.dto.site;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object representing a site section.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A site section")
public class SiteSection {

    @ApiModelProperty(value = "Site section id")
    private Integer sectionId;

    @ApiModelProperty(value = "section code")
    private String code;

    @ApiModelProperty(value = "section schedules")
    private List<SiteSchedule> schedules;
}
