package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteNewsEntity;
import com.portalasig.ms.site.dto.site.SiteNews;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteNewsMapper {

    SiteNews toDto(SiteNewsEntity news);
}
