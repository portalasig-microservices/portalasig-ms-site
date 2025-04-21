package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.MediaEntity;
import com.portalasig.ms.site.dto.Media;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaMapper {

    Media toDto(MediaEntity media);
}
