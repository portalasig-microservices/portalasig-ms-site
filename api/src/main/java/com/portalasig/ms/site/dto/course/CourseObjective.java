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
@ApiModel(description = "A college course objective")
public class CourseObjective {

    @ApiModelProperty(value = "Course objective id")
    private Long courseObjectiveId;

    @ApiModelProperty(value = "Course description")
    private String description;

}
