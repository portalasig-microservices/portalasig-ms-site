package com.portalasig.ms.site.dto.site;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for creating or updating a site.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A site request")
public class SiteRequest {

    @ApiModelProperty(value = "Course code")
    @NotNull
    private String courseCode;

    @ApiModelProperty(value = "Academic period type")
    @NotNull
    @NotEmpty
    private AcademicPeriodType periodType;

    @ApiModelProperty(value = "Academic period year")
    @NotNull
    private Integer periodYear;
}
