package com.portalasig.ms.site.dto.semester;

import com.portalasig.ms.site.constant.AcademicPeriodType;
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
@ApiModel(description = "A college semester")
public class Semester {

    @ApiModelProperty(value = "Semester identifier")
    private Integer semesterId;

    @ApiModelProperty(value = "Semester academic period")
    @NotNull
    private AcademicPeriodType periodType;

    @ApiModelProperty(value = "Semester description")
    @NotNull
    private Integer periodYear;

    @ApiModelProperty(value = "Semester start date")
    private LocalDate startDate;

    @ApiModelProperty(value = "Semester end date")
    private LocalDate endDate;

    @ApiModelProperty(value = "Semester is active")
    private Boolean isActive;
}
