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

/**
 * Request body for creating or updating a college semester.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A college semester request")
public class SemesterRequest {

    @ApiModelProperty(value = "Semester id")
    private Integer semesterId;

    @ApiModelProperty(value = "Semester academic period")
    private AcademicPeriodType periodType;

    @ApiModelProperty(value = "Semester description")
    @NotNull
    private Integer periodYear;

    @ApiModelProperty(value = "Name")
    @NotNull
    private String name;

    @ApiModelProperty(value = "Start date")
    @NotNull
    private LocalDate startDate;

    @ApiModelProperty(value = "End date")
    @NotNull
    private LocalDate endDate;

    @ApiModelProperty(value = "Description")
    private String description;
}
