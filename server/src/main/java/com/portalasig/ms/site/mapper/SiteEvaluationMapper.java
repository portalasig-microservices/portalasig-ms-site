package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteEvaluationEntity;
import com.portalasig.ms.site.dto.evaluation.SiteEvaluationRequest;
import com.portalasig.ms.site.dto.site.SiteEvaluation;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteEvaluationMapper {

    SiteEvaluation toDto(SiteEvaluationEntity siteAssessment);

    @Mapping(target = "site", ignore = true)
    SiteEvaluationEntity toEntityFromRequest(SiteEvaluationRequest request);

    @Mapping(target = "site", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(SiteEvaluationEntity existingEvaluation, SiteEvaluationRequest request);
}
