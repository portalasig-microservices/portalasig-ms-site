package com.portalasig.ms.site.dto.evaluation;

import com.portalasig.ms.site.constant.EvaluationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A site evaluation request")
public class SiteEvaluationRequest {

    @ApiModelProperty(value = "Evaluation id")
    private Integer evaluationId;

    @ApiModelProperty(value = "evaluation name")
    @NotNull
    private String name;

    @ApiModelProperty("percentage of weight for student course final score")
    @NotNull
    private Float weight;

    @ApiModelProperty("type of evaluation")
    @NotNull
    private EvaluationType evaluationType;

    @ApiModelProperty("When does the evaluation start")
    @NotNull
    private LocalDate startDate;

    @ApiModelProperty("When does the evaluation ends")
    private LocalDate endDate;
}
