package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.course.CourseObjectiveEntity;
import com.portalasig.ms.site.dto.course.CourseObjective;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseObjectiveMapper {

    CourseObjective toDto(CourseObjectiveEntity courseObjective);
}
