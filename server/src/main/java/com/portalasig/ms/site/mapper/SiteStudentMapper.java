package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteStudentEntity;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.uaa.dto.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Maps {@link SiteStudentEntity} to its corresponding DTO {@link SiteParty}.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SiteStudentMapper {

    /**
     * Converts a {@link SiteStudentEntity} to a {@link SiteParty} DTO.
     *
     * @param student the entity to convert
     * @return the mapped DTO
     */
    @Mapping(target = "section.schedules", ignore = true)
    SiteParty toDto(SiteStudentEntity student);

    /**
     * Converts a {@link User} to a {@link SiteStudentEntity} DTO.
     *
     * @param user user object
     * @return site student entity
     */
    @Mapping(target = "partyRole", ignore = true)
    @Mapping(target = "partySiteTitle", ignore = true)
    SiteStudentEntity toEntityFromUser(User user);
}
