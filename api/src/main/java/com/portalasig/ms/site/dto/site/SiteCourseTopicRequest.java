package com.portalasig.ms.site.dto.site;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for site course topic requests.
 * Contains information about a course topic such as id, title, and description.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A site course topic request")
public class SiteCourseTopicRequest {

    @ApiModelProperty(value = "Site course topic id")
    private Integer siteCourseTopicId;

    @ApiModelProperty(value = "topic title")
    @NotEmpty
    @NotNull
    private String title;

    @ApiModelProperty(value = "topic description")
    @NotEmpty
    @NotNull
    private String description;
}
