package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.ReferenceEntity;
import com.portalasig.ms.site.dto.Reference;
import com.portalasig.ms.site.dto.site.ReferenceRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReferenceMapper {

    Reference toDto(ReferenceEntity entity);

    @Mapping(target = "sites", ignore = true)
    ReferenceEntity toEntityFromRequest(ReferenceRequest request);

    @Mapping(target = "sites", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromExisting(@MappingTarget ReferenceEntity referenceEntity, ReferenceRequest request);
}
