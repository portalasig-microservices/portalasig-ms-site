package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.SemesterEntity;
import com.portalasig.ms.site.dto.semester.Semester;
import com.portalasig.ms.site.dto.semester.SemesterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SemesterMapper {


    Semester toDto(SemesterEntity semesterEntity);

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "courses", ignore = true)
    SemesterEntity toEntity(SemesterRequest semester);

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    void toEntityFromExisting(SemesterRequest request, @MappingTarget SemesterEntity semester);
}
