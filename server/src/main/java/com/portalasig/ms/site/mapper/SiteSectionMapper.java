package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteSectionEntity;
import com.portalasig.ms.site.dto.site.SiteSection;
import com.portalasig.ms.site.dto.site.SiteSectionRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for converting between {@link SiteSectionEntity} and its DTO representations.
 * <p>
 * Uses MapStruct to handle the transformation logic between domain entities and DTOs,
 * primarily used in the Site Section API.
 *
 * <p>Note:
 * - The {@code site} reference is intentionally ignored during entity mapping, as it is set externally.
 * - When updating an existing entity, {@code createdDate} and {@code updatedDate} fields are preserved.
 *
 * @see SiteSectionEntity
 * @see SiteSection
 * @see SiteSectionRequest
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        SiteSectionScheduleMapper.class,
})
public interface SiteSectionMapper {

    /**
     * Converts a {@link SiteSectionEntity} to its DTO representation.
     *
     * @param section the section entity to convert
     * @return the mapped {@link SiteSection} DTO
     */
    SiteSection toDto(SiteSectionEntity section);

    /**
     * Converts a {@link SiteSectionRequest} to a new {@link SiteSectionEntity}.
     * <p>
     * The {@code site} association is ignored and should be set manually after mapping.
     *
     * @param request the request containing data to create the entity
     * @return a new {@link SiteSectionEntity} instance
     */
    @Mapping(target = "site", ignore = true)
    SiteSectionEntity toEntityFromRequest(SiteSectionRequest request);

    /**
     * Updates an existing {@link SiteSectionEntity} with values from the given request.
     * <p>
     * - Ignores {@code null} values to preserve existing data.
     * - Ignores {@code site}, {@code createdDate}, and {@code updatedDate} fields.
     *
     * @param existingSection the entity to be updated (will be mutated)
     * @param request         the incoming request data
     */
    @Mapping(target = "site", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(@MappingTarget SiteSectionEntity existingSection, SiteSectionRequest request);
}