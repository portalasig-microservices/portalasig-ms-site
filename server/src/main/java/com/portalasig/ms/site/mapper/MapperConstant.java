package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.constant.SiteUserRoleType;
import com.portalasig.ms.site.domain.entity.site.SiteUserRoleEntity;

import java.util.List;
import java.util.Set;

public class MapperConstant {

    public static List<SiteUserRoleType> mapUserRoles(Set<SiteUserRoleEntity> userRoles) {
        return userRoles.stream()
                .map(SiteUserRoleEntity::getSiteUserRoleType)
                .toList();
    }
}
