package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.ReferenceEntity;
import com.portalasig.ms.site.dto.Reference;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReferenceMapper {

    Reference toDto(ReferenceEntity courseTopic);
}
