package com.portalasig.ms.site.dto.site;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a site course topic.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A site course topic")
public class SiteCourseTopic {

    @ApiModelProperty(value = "Site course topic id")
    private Integer siteCourseTopicId;

    @ApiModelProperty(value = "topic title")
    private String title;

    @ApiModelProperty(value = "topic description")
    private String description;
}
