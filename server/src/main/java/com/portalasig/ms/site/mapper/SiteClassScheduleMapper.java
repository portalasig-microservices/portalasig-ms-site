package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.constant.SiteUserRoleType;
import com.portalasig.ms.site.domain.entity.SiteClassScheduleEntity;
import com.portalasig.ms.site.domain.entity.site.SiteUserRoleEntity;
import com.portalasig.ms.site.dto.site.SiteClassSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {SiteUserMapper.class})
public interface SiteClassScheduleMapper {

    SiteClassSchedule toDto(SiteClassScheduleEntity classSchedule);

    default List<SiteUserRoleType> mapUserRoles(Set<SiteUserRoleEntity> userRoles) {
        return MapperConstant.mapUserRoles(userRoles);
    }
}
