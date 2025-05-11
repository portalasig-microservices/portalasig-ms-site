package com.portalasig.ms.site.dto.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A college course objective removal request for a site")
public class SiteObjectiveRemoveRequest {

    @ApiModelProperty(value = "Period type")
    @NotEmpty
    private String periodType;

    @ApiModelProperty(value = "Period year")
    @NotEmpty
    private Integer periodYear;

    @ApiModelProperty(value = "Course Unique code")
    @NotEmpty
    private String courseCode;

    @ApiModelProperty(value = "Course objective id")
    @NotNull
    private Integer courseObjectiveId;

}