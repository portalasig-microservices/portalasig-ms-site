package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SitePartyEntity;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.uaa.dto.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Maps {@link SitePartyEntity} to its corresponding DTO {@link SiteParty}.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SitePartyMapper {

    /**
     * Converts a {@link SitePartyEntity} to a {@link SiteParty} DTO.
     *
     * @param siteUser the entity to convert
     * @return the mapped DTO
     */
    SiteParty toDto(SitePartyEntity siteUser);

    /**
     * Converts a {@link SiteParty} to a {@link SitePartyEntity} entity.
     *
     * @param siteUser party object
     * @return site party entity
     */
    SitePartyEntity toEntity(SiteParty siteUser);

    /**
     * Converts a {@link User} to a {@link SitePartyEntity} entity.
     *
     * @param user user object
     * @return site party entity
     */
    @Mapping(target = "partyRole", ignore = true)
    @Mapping(target = "partySiteTitle", ignore = true)
    SitePartyEntity toEntityFromUser(User user);
}
