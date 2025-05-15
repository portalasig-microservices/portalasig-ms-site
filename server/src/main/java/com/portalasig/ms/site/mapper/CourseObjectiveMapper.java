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

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseObjectiveMapper {

    CourseObjective toDto(CourseObjectiveEntity courseObjective);

    @Mapping(target = "title", ignore = true)
    @Mapping(target = "sites", ignore = true)
    CourseObjectiveEntity toEntityFromRequest(SiteObjectiveRequest courseObjective);

    @Mapping(target = "title", ignore = true)
    @Mapping(target = "sites", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(@MappingTarget CourseObjectiveEntity objective, SiteObjectiveRequest request);

}
