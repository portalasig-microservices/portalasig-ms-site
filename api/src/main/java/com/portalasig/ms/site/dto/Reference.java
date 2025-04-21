package com.portalasig.ms.site.dto;

import com.portalasig.ms.site.constant.ReferenceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ApiModelProperty(value = "Reference Title")
    private String title;

    @ApiModelProperty(value = "Reference URL in case of a website")
    private String url;

    @ApiModelProperty(value = "Reference author in case is known")
    private String author;

    @ApiModelProperty(value = "Relevance order of the reference")
    private Integer priority;

    @ApiModelProperty(value = "Can be marked as s")
    private Boolean isRequired;

}
