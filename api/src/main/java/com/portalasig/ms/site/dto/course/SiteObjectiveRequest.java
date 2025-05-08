package com.portalasig.ms.site.dto.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A college course objective for a site")
public class SiteObjectiveRequest {

    @ApiModelProperty(value = "Course objective id")
    private Integer courseObjectiveId;

    @ApiModelProperty(value = "Period type")
    @NotEmpty
    private String periodType;

    @ApiModelProperty(value = "Period year")
    @NotEmpty
    private Integer periodYear;

    @ApiModelProperty(value = "Course Unique code")
    @NotEmpty
    private String courseCode;

    @ApiModelProperty(value = "Course description")
    @NotEmpty
    private String description;

    @ApiModelProperty(value = "Objective priority")
    private Integer priority = 0;

}
