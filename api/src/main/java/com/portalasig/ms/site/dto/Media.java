package com.portalasig.ms.site.dto;

import com.portalasig.ms.site.constant.MediaType;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A media entity. Can be anything from pictures, videos, urls, audio, etc")
public class Media {

    private Integer mediaId;

    private String name;

    private String description;

    private String url;

    private String fileName;

    private Long fileSize;

    private MediaType mediaType;

}
