package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.course.CourseObjectiveEntity;
import com.portalasig.ms.site.dto.course.CourseObjective;
import com.portalasig.ms.site.dto.site.SiteObjectiveRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper responsible for converting between {@link CourseObjectiveEntity},
 * {@link CourseObjective}, and {@link SiteObjectiveRequest}.
 *
 * <p>Used in the site objective lifecycle to persist or update course objectives
 * associated with a specific academic site.</p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseObjectiveMapper {

    /**
     * Converts a {@link CourseObjectiveEntity} to a DTO representation.
     *
     * @param courseObjective the course objective entity
     * @return the mapped DTO
     */
    CourseObjective toDto(CourseObjectiveEntity courseObjective);

    /**
     * Maps a {@link SiteObjectiveRequest} to a new {@link CourseObjectiveEntity}.
     * Ignores the title and sites fields, as those are handled elsewhere.
     *
     * @param courseObjective the input request
     * @return a new CourseObjectiveEntity
     */
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "sites", ignore = true)
    CourseObjectiveEntity toEntityFromRequest(SiteObjectiveRequest courseObjective);

    /**
     * Updates an existing {@link CourseObjectiveEntity} with data from a {@link SiteObjectiveRequest}.
     * Ignores null values and fields like title and sites, which are managed externally.
     *
     * @param objective the existing entity to update
     * @param request   the incoming request with updated fields
     */
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "sites", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(@MappingTarget CourseObjectiveEntity objective, SiteObjectiveRequest request);
}
