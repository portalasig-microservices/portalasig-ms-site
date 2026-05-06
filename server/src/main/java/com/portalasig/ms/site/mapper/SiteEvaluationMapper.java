package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteEvaluationEntity;
import com.portalasig.ms.site.dto.evaluation.SiteEvaluationRequest;
import com.portalasig.ms.site.dto.site.SiteEvaluation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Mapper for site evaluation entities and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteEvaluationMapper {

    /**
     * Maps a LocalDate to an Instant.
     *
     * @param date the date to map
     * @return the mapped instant
     */
    default Instant map(LocalDate date) {
        return date != null ? date.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;
    }

    /**
     * Maps an Instant to a LocalDate.
     *
     * @param instant the instant to map
     * @return the mapped date
     */
    default LocalDate map(Instant instant) {
        return instant != null ? instant.atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    /**
     * Maps a site evaluation entity to a DTO.
     *
     * @param siteAssessment the entity to map
     * @return the mapped DTO
     */
    SiteEvaluation toDto(SiteEvaluationEntity siteAssessment);

    /**
     * Maps a site evaluation request to an entity.
     *
     * @param request the request to map
     * @return the mapped entity
     */
    @Mapping(target = "site", ignore = true)
    SiteEvaluationEntity toEntityFromRequest(SiteEvaluationRequest request);

    /**
     * Updates an existing entity from a request.
     *
     * @param existingEvaluation the existing entity
     * @param request            the request
     */
    @Mapping(target = "site", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(
            @MappingTarget SiteEvaluationEntity existingEvaluation,
            SiteEvaluationRequest request
    );
}
