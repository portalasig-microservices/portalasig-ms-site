package com.portalasig.ms.site.dto.site;

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
@ApiModel(description = "A college course objective for a site")
public class SiteObjectiveRequest {

    @ApiModelProperty(value = "Course objective id")
    private Integer courseObjectiveId;

    @ApiModelProperty(value = "Objective title")
    private String title;

    @ApiModelProperty(value = "Course description")
    @NotEmpty
    @NotNull
    private String description;

    @ApiModelProperty(value = "Objective priority")
    private Integer priority = 0;

}
