package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SitePartyEntity;
import com.portalasig.ms.site.dto.site.SiteParty;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Maps {@link SitePartyEntity} to its corresponding DTO {@link SiteParty}.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteUserMapper {

    /**
     * Converts a {@link SitePartyEntity} to a {@link SiteParty} DTO.
     *
     * @param siteUser the entity to convert
     * @return the mapped DTO
     */
    SiteParty toDto(SitePartyEntity siteUser);
}
