package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.course.CourseTopicEntity;
import com.portalasig.ms.site.dto.course.CourseTopic;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between {@link CourseTopicEntity} and {@link CourseTopic} DTOs.
 *
 * <p>Used to expose course topics in a clean DTO format for API responses.</p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseTopicMapper {

    /**
     * Maps a {@link CourseTopicEntity} to its corresponding {@link CourseTopic} DTO.
     *
     * @param courseTopic the course topic entity
     * @return the DTO representation
     */
    CourseTopic toDto(CourseTopicEntity courseTopic);
}

