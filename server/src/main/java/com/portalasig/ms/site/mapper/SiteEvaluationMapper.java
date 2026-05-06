package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteEvaluationEntity;
import com.portalasig.ms.site.dto.evaluation.SiteEvaluationRequest;
import com.portalasig.ms.site.dto.site.SiteEvaluation;
import org.mapstruct.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteEvaluationMapper {

    default Instant map(LocalDate date) {
        return date != null ? date.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;
    }

    default LocalDate map(Instant instant) {
        return instant != null ? instant.atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    SiteEvaluation toDto(SiteEvaluationEntity siteAssessment);

    @Mapping(target = "site", ignore = true)
    SiteEvaluationEntity toEntityFromRequest(SiteEvaluationRequest request);

    @Mapping(target = "site", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(@MappingTarget SiteEvaluationEntity existingEvaluation, SiteEvaluationRequest request);
}
