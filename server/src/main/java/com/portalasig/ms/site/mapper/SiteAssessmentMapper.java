package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.SiteAssessmentEntity;
import com.portalasig.ms.site.dto.site.SiteAssessment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteAssessmentMapper {

    SiteAssessment toDto(SiteAssessmentEntity siteAssessment);
}
