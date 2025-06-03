package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteAssessmentEntity;
import com.portalasig.ms.site.dto.site.SiteAssessment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between {@link SiteAssessmentEntity} and its corresponding DTO {@link SiteAssessment}.
 *
 * <p>This mapper handles the transformation of assessment data from the persistence layer
 * to the API layer representation.</p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteAssessmentMapper {

    /**
     * Converts a {@link SiteAssessmentEntity} to a {@link SiteAssessment} DTO.
     *
     * @param siteAssessment the entity to convert
     * @return the corresponding DTO
     */
    SiteAssessment toDto(SiteAssessmentEntity siteAssessment);
}
