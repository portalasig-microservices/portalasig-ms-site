package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.constant.SiteUserRoleType;
import com.portalasig.ms.site.domain.entity.site.SiteUserEntity;
import com.portalasig.ms.site.domain.entity.site.SiteUserRoleEntity;
import com.portalasig.ms.site.dto.site.SiteUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteUserMapper {

    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "identity", ignore = true)
    @Mapping(target = "email", ignore = true)
    SiteUser toDto(SiteUserEntity siteUser);

    default List<SiteUserRoleType> mapUserRoles(Set<SiteUserRoleEntity> userRoles) {
        return MapperConstant.mapUserRoles(userRoles);
    }
}
