package com.portalasig.ms.site.dto.site;

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
@ApiModel(description = "A site request")
public class SiteRequest {

    @ApiModelProperty(value = "Course code")
    @NotNull
    private String courseCode;

    @ApiModelProperty(value = "Academic period")
    @NotNull
    private String academicPeriod;
}
