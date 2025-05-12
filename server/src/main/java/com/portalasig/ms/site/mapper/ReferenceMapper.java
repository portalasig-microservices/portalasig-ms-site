package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.ReferenceEntity;
import com.portalasig.ms.site.dto.Reference;
import com.portalasig.ms.site.dto.site.ReferenceRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReferenceMapper {

    Reference toDto(ReferenceEntity entity);

    ReferenceEntity toEntityFromRequest(ReferenceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(@MappingTarget ReferenceEntity referenceEntity, ReferenceRequest request);
}
