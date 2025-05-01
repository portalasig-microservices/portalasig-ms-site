package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteClassScheduleEntity;
import com.portalasig.ms.site.dto.site.SiteClassSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {SiteUserMapper.class})
public interface SiteClassScheduleMapper {

    SiteClassSchedule toDto(SiteClassScheduleEntity classSchedule);
}
