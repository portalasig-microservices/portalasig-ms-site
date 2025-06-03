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

/**
 * MapStruct mapper for transforming between Course-related DTOs and entities.
 * Handles mapping between {@link CourseEntity}, {@link Course}, {@link CourseRequest}, and {@link CsvCourse}.
 *
 * <p>Special attention is given to ignore nested collections and relational fields (e.g., semesters, topics, sites)
 * which should be handled manually or through converter logic.</p>
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {EnumStringMapper.class, CourseType.class, HashSet.class, Set.class},
        uses = {ReferenceMapper.class, CourseTopicMapper.class, CourseObjectiveMapper.class, SemesterMapper.class}
)
public interface CourseMapper {

    /**
     * Converts a {@link CourseEntity} to its DTO {@link Course}.
     *
     * @param courseEntity the source entity
     * @return the mapped DTO
     */
    Course toDto(CourseEntity courseEntity);

    /**
     * Converts a {@link CourseRequest} to its entity {@link CourseEntity}, ignoring nested relational fields.
     *
     * @param request the source DTO
     * @return the mapped entity
     */
    @Mapping(target = "semesters", ignore = true)
    @Mapping(target = "careers", ignore = true)
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "sites", ignore = true)
    CourseEntity toEntity(CourseRequest request);

    /**
     * Updates an existing {@link CourseEntity} from a {@link CourseRequest}, ignoring audit and nested fields.
     *
     * @param course  the existing entity to update
     * @param request the source DTO with update values
     */
    @Mapping(target = "semesters", ignore = true)
    @Mapping(target = "careers", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    // TODO: I NEED TO IMPORT THIS DATA AND ADD THEM IN INITIALIZATION SCRIPT
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "sites", ignore = true)
    void toEntityFromExisting(@MappingTarget CourseEntity course, CourseRequest request);

    /**
     * Converts a {@link CsvCourse} to a {@link CourseEntity}, ignoring nested and audit fields.
     *
     * @param courseCsv the CSV DTO source
     * @return the mapped entity
     */
    @Mapping(target = "semesters", ignore = true)
    @Mapping(target = "careers", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    // TODO: I NEED TO IMPORT THIS DATA AND ADD THEM IN INITIALIZATION SCRIPT
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "sites", ignore = true)
    CourseEntity toEntityFromCsv(CsvCourse courseCsv);

    /**
     * Extracts a list of career IDs from a set of {@link CareerEntity}.
     *
     * @param careers set of career entities
     * @return list of career IDs
     */
    default List<Integer> flatCareers(Set<CareerEntity> careers) {
        return careers.stream().map(CareerEntity::getCareerId).toList();
    }
}