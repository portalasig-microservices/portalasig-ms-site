package com.portalasig.ms.site.dto;

import com.portalasig.ms.site.constant.MediaType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a media item such as an image, video, audio, or external URL.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A media entity. Can be anything from pictures, videos, urls, audio, etc")
public class Media {

    @ApiModelProperty(value = "Media identifier")
    private Integer mediaId;

    @ApiModelProperty(value = "Media name")
    private String name;

    @ApiModelProperty(value = "Media description")
    private String description;

    @ApiModelProperty(value = "Media url")
    private String url;

    @ApiModelProperty(value = "Media filename (if applies)")
    private String fileName;

    @ApiModelProperty(value = "Media file size (if applies)")
    private Long fileSize;

    @ApiModelProperty(value = "Media type")
    private MediaType mediaType;
}
