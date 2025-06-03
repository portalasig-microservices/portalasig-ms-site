package com.portalasig.ms.site.dto;

import com.portalasig.ms.site.constant.ReferenceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a study reference such as a book or a website.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A study reference. It can be a book, a website, etc.")
public class Reference {

    @ApiModelProperty(value = "Reference id")
    private Integer referenceId;

    @ApiModelProperty(value = "Reference type")
    private ReferenceType referenceType;

    @ApiModelProperty(value = "Reference title")
    private String title;

    @ApiModelProperty(value = "Reference description")
    private String description;

    @ApiModelProperty(value = "Reference URL in case of a website")
    private String url;

    @ApiModelProperty(value = "Reference author if known")
    private String author;

    @ApiModelProperty(value = "Relevance order of the reference")
    private Integer priority;

    @ApiModelProperty(value = "Whether the reference is required")
    private Boolean isRequired;
}
