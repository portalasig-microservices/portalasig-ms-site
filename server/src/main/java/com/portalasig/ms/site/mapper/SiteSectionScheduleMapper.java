package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteSectionScheduleEntity;
import com.portalasig.ms.site.dto.site.SiteSchedule;
import com.portalasig.ms.site.dto.site.SiteScheduleRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for converting between {@link SiteSectionScheduleEntity} and its DTO/request representations.
 * <p>
 * This interface uses MapStruct to map between:
 * <ul>
 *     <li>{@link SiteSectionScheduleEntity} and {@link SiteSchedule}</li>
 *     <li>{@link SiteScheduleRequest} and {@link SiteSectionScheduleEntity}</li>
 * </ul>
 * <p>
 * Associations such as {@code instructor} and {@code section} are excluded from the mapping
 * and should be handled manually in the service layer.
 *
 * @see SiteSectionScheduleEntity
 * @see SiteSchedule
 * @see SiteScheduleRequest
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteSectionScheduleMapper {

    /**
     * Converts a {@link SiteSectionScheduleEntity} to its DTO representation.
     *
     * @param section the schedule entity to convert
     * @return the mapped {@link SiteSchedule} DTO
     */
    SiteSchedule toDto(SiteSectionScheduleEntity section);

    /**
     * Converts a {@link SiteScheduleRequest} to a new {@link SiteSectionScheduleEntity}.
     * <p>
     * Fields {@code instructor} and {@code section} are ignored and should be set manually.
     *
     * @param request the request data to convert
     * @return a new instance of {@link SiteSectionScheduleEntity}
     */
    @Mapping(target = "section", ignore = true)
    SiteSectionScheduleEntity toEntityFromRequest(SiteScheduleRequest request);

    /**
     * Updates an existing {@link SiteSectionScheduleEntity} with values from a {@link SiteScheduleRequest}.
     * <p>
     * Null values are ignored, preserving the current state of the entity.
     * Fields such as {@code instructor}, {@code section}, {@code createdDate}, and {@code updatedDate} are ignored.
     *
     * @param existingSchedule the entity to update
     * @param request          the incoming request containing new values
     */
    @Mapping(target = "section", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(@MappingTarget SiteSectionScheduleEntity existingSchedule, SiteScheduleRequest request);
}
