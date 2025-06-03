package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteNewsEntity;
import com.portalasig.ms.site.dto.site.SiteNews;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between {@link SiteNewsEntity} and its DTO {@link SiteNews}.
 * <p>
 * This mapper is responsible for translating site news domain entities to their DTO representation.
 * </p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteNewsMapper {

    /**
     * Converts a {@link SiteNewsEntity} into a {@link SiteNews} DTO.
     *
     * @param news the site news entity to convert
     * @return the mapped DTO
     */
    SiteNews toDto(SiteNewsEntity news);
}
