package com.portalasig.ms.site.dto.course;

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
@ApiModel(description = "A college course topic")
public class CourseTopic {

    @ApiModelProperty(value = "Course topic id")
    private Long courseTopicId;

    @ApiModelProperty(value = "Topic title")
    private String title;

    @ApiModelProperty(value = "Topic description")
    private String description;

}
