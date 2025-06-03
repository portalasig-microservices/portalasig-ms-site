package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.MediaEntity;
import com.portalasig.ms.site.dto.Media;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between {@link MediaEntity} and {@link Media} DTOs.
 *
 * <p>Used to transform media-related data from the persistence layer into a format suitable for API responses.</p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaMapper {

    /**
     * Maps a {@link MediaEntity} to its corresponding {@link Media} DTO.
     *
     * @param media the media entity
     * @return the DTO representation of the media
     */
    Media toDto(MediaEntity media);
}
