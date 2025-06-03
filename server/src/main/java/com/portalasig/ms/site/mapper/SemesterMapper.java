package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.SemesterEntity;
import com.portalasig.ms.site.dto.semester.Semester;
import com.portalasig.ms.site.dto.semester.SemesterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for converting between {@link SemesterEntity} and its corresponding DTOs:
 * {@link Semester} and {@link SemesterRequest}.
 *
 * <p>Handles transformation logic between the persistence and API layers for semester data.</p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SemesterMapper {

    /**
     * Converts a {@link SemesterEntity} to a {@link Semester} DTO.
     *
     * @param semesterEntity the entity to convert
     * @return the corresponding DTO
     */
    Semester toDto(SemesterEntity semesterEntity);

    /**
     * Converts a {@link SemesterRequest} to a new {@link SemesterEntity}.
     * Ignores the `isActive`, `courses`, and `sites` fields during mapping.
     *
     * @param semester the incoming request payload
     * @return a new {@link SemesterEntity}
     */
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "sites", ignore = true)
    SemesterEntity toEntity(SemesterRequest semester);

    /**
     * Updates an existing {@link SemesterEntity} with values from a {@link SemesterRequest}.
     * Ignores audit fields (`createdDate`, `updatedDate`) and associations (`courses`, `sites`).
     * Also ignores `isActive` to prevent status overrides during update operations.
     *
     * @param request  the request containing updated values
     * @param semester the entity to update
     */
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "sites", ignore = true)
    void toEntityFromExisting(SemesterRequest request, @MappingTarget SemesterEntity semester);
}
