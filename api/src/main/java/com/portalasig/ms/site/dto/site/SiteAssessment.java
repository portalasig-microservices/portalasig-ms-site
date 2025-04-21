package com.portalasig.ms.site.dto.site;

import com.portalasig.ms.site.constant.AssessmentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A course site assessment")
public class SiteAssessment {

    @ApiModelProperty(value = "Site assessment id")
    private Integer siteAssessmentId;

    @ApiModelProperty(value = "assessment weight. How much adds up for the final course score")
    private float weight;

    @ApiModelProperty(value = "Assessment name")
    private String name;

    @ApiModelProperty(value = "Assessment type")
    private AssessmentType assessmentType;

    @ApiModelProperty(value = "When assessment starts being evaluated")
    private Instant startDate;

    @ApiModelProperty(value = "When assessment ends being evaluated")
    private Instant endDate;

}
