package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.CareerEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Set;

/**
 * MapStruct mapper for converting between {@link SiteEntity} and its DTO {@link Site}.
 * <p>
 * This mapper leverages other mappers to handle nested objects, including class schedules, assessments,
 * news, media, users, semester, and references.
 * </p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        SiteAssessmentMapper.class,
        SiteSectionMapper.class,
        SiteNewsMapper.class,
        MediaMapper.class,
        SitePartyMapper.class,
        SemesterMapper.class,
        ReferenceMapper.class,
        SiteCourseTopicMapper.class
})
public interface SiteMapper {

    /**
     * Converts a {@link SiteEntity} into a {@link Site} DTO.
     *
     * @param siteEntity the entity to convert
     * @return the mapped DTO
     */
    Site toDto(SiteEntity siteEntity);

    /**
     * Extracts career IDs from a set of {@link CareerEntity} objects.
     *
     * @param careers the set of career entities
     * @return a list of corresponding career IDs
     */
    default List<Integer> mapCareers(Set<CareerEntity> careers) {
        return careers.stream().map(CareerEntity::getCareerId).toList();
    }
}
