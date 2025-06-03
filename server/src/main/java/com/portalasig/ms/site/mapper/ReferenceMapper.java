package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.ReferenceEntity;
import com.portalasig.ms.site.dto.Reference;
import com.portalasig.ms.site.dto.site.ReferenceRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for converting between {@link ReferenceEntity} and {@link Reference} or {@link ReferenceRequest} DTOs.
 *
 * <p>Facilitates transformation between persistence and transport layers for reference-related data.</p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReferenceMapper {

    /**
     * Maps a {@link ReferenceEntity} to a {@link Reference} DTO.
     *
     * @param entity the reference entity
     * @return the DTO representation of the reference
     */
    Reference toDto(ReferenceEntity entity);

    /**
     * Maps a {@link ReferenceRequest} to a new {@link ReferenceEntity}.
     * The list of associated sites is ignored during this mapping.
     *
     * @param request the reference request payload
     * @return a new {@link ReferenceEntity}
     */
    @Mapping(target = "sites", ignore = true)
    ReferenceEntity toEntityFromRequest(ReferenceRequest request);

    /**
     * Updates an existing {@link ReferenceEntity} with values from a {@link ReferenceRequest}.
     * Ignores null values and does not modify audit fields or site associations.
     *
     * @param referenceEntity the target entity to update
     * @param request         the source request with updated values
     */
    @Mapping(target = "sites", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(@MappingTarget ReferenceEntity referenceEntity, ReferenceRequest request);
}
