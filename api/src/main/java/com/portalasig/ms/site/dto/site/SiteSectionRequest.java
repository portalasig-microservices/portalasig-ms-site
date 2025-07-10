package com.portalasig.ms.site.dto.site;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO representing a section within a site.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A college section for a site")
public class SiteSectionRequest {

    @ApiModelProperty(value = "Section id")
    private Integer sectionId;

    @ApiModelProperty(value = "Section code")
    @NotEmpty
    private String code;

}
