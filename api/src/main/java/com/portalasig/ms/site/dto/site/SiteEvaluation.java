package com.portalasig.ms.site.dto.site;

import com.portalasig.ms.site.constant.EvaluationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Represents an evaluation for a course site, including type, name, weight, and evaluation dates.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A course site evaluation")
public class SiteEvaluation {

    @ApiModelProperty(value = "evaluation id")
    private Integer evaluationId;

    @ApiModelProperty(value = "How much adds up for the final course score")
    private float weight;

    @ApiModelProperty(value = "evaluation name")
    private String name;

    @ApiModelProperty(value = "evaluation type")
    private EvaluationType evaluationType;

    @ApiModelProperty(value = "When evaluation starts being evaluated")
    private Instant startDate;

    @ApiModelProperty(value = "When evaluation ends being evaluated. some evaluations does not have this set.")
    private Instant endDate;

}
