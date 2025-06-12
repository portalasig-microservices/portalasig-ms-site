package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteCourseTopicEntity;
import com.portalasig.ms.site.dto.site.SiteCourseTopic;
import com.portalasig.ms.site.dto.site.SiteCourseTopicRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for converting between {@link SiteCourseTopicEntity} and its DTOs.
 *
 * <p>Used to transform course topic data between the persistence model and API models.</p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteCourseTopicMapper {

    /**
     * Maps a {@link SiteCourseTopicEntity} to its corresponding DTO {@link SiteCourseTopic}.
     *
     * @param courseTopic the course topic entity
     * @return the DTO representation for API responses
     */
    SiteCourseTopic toDto(SiteCourseTopicEntity courseTopic);

    /**
     * Maps a {@link SiteCourseTopicRequest} to a new {@link SiteCourseTopicEntity}.
     * Ignores site binding, which must be set explicitly by the caller.
     *
     * @param request the incoming request containing course topic data
     * @return a new course topic entity ready to be persisted
     */
    @Mapping(target = "site", ignore = true)
    SiteCourseTopicEntity toEntityFromRequest(SiteCourseTopicRequest request);

    /**
     * Maps a {@link SiteCourseTopicRequest} onto an existing {@link SiteCourseTopicEntity}.
     * Ignores audit fields like created/updated dates, and site bindings.
     *
     * @param existingCourseTopic the existing entity to update
     * @param request             the incoming request with updated values
     */
    @Mapping(target = "site", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(@MappingTarget SiteCourseTopicEntity existingCourseTopic, SiteCourseTopicRequest request);
}
