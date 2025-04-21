package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.course.CourseTopicEntity;
import com.portalasig.ms.site.dto.course.CourseTopic;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseTopicMapper {

    CourseTopic toDto(CourseTopicEntity courseTopic);
}
