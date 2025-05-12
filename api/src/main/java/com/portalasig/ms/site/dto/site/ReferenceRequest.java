package com.portalasig.ms.site.dto.site;

import com.portalasig.ms.commons.rest.exception.BadRequestException;
import com.portalasig.ms.site.constant.ReferenceType;
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
@ApiModel(description = "A college course reference for a site")
public class ReferenceRequest {

    @ApiModelProperty(value = "Reference id")
    private Integer referenceId;

    @ApiModelProperty(value = "Reference type")
    @NotNull
    @NotEmpty
    private ReferenceType referenceType;

    @ApiModelProperty(value = "Reference Title")
    @NotNull
    @NotEmpty
    private String title;

    @ApiModelProperty(value = "Description")
    @NotNull
    @NotEmpty
    private String description;

    @ApiModelProperty(value = "Reference URL in case of a website")
    private String url;

    @ApiModelProperty(value = "Reference author in case is known")
    @NotNull
    @NotEmpty
    private String author;

    @ApiModelProperty(value = "Relevance order of the reference")
    private Integer priority = 0;

    @ApiModelProperty(value = "Can be marked as a mandatory to read")
    private Boolean isRequired = false;

    public void validateUrl() {
        if (ReferenceType.ONLINE.equals(this.referenceType) && (this.url == null || this.url.trim().isEmpty())) {
            throw new BadRequestException("URL is mandatory for online references");
        }
    }

}
