package com.portalasig.ms.site.mapper;


import com.portalasig.ms.commons.mapper.EnumStringMapper;
import com.portalasig.ms.site.constant.CourseType;
import com.portalasig.ms.site.domain.entity.CareerEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import com.portalasig.ms.site.dto.course.Course;
import com.portalasig.ms.site.dto.course.CourseRequest;
import com.portalasig.ms.site.dto.course.CsvCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {EnumStringMapper.class, CourseType.class, HashSet.class, Set.class},
        uses = {ReferenceMapper.class, CourseTopicMapper.class, CourseObjectiveMapper.class, SemesterMapper.class}
)
public interface CourseMapper {

    Course toDto(CourseEntity courseEntity);

    @Mapping(target = "semesters", ignore = true)
    @Mapping(target = "careers", ignore = true)
    @Mapping(target = "objectives", ignore = true)
    @Mapping(target = "references", ignore = true)
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "sites", ignore = true)
    CourseEntity toEntity(CourseRequest request);

    @Mapping(target = "semesters", ignore = true)
    @Mapping(target = "careers", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "objectives", ignore = true)
    // TODO: I NEED TO IMPORT THIS DATA AND ADD THEM IN INITIALIZATION SCRIPT
    @Mapping(target = "references", ignore = true)
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "sites", ignore = true)
    void toEntityFromExisting(@MappingTarget CourseEntity course, CourseRequest request);

    @Mapping(target = "semesters", ignore = true)
    @Mapping(target = "careers", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    // TODO: I NEED TO IMPORT THIS DATA AND ADD THEM IN INITIALIZATION SCRIPT
    @Mapping(target = "objectives", ignore = true)
    @Mapping(target = "references", ignore = true)
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "sites", ignore = true)
    CourseEntity toEntityFromCsv(CsvCourse courseCsv);

    default List<Integer> flatCareers(Set<CareerEntity> careers) {
        return careers.stream().map(CareerEntity::getCareerId).toList();
    }
}
