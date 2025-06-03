package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.site.SiteClassScheduleEntity;
import com.portalasig.ms.site.dto.site.SiteClassSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between {@link SiteClassScheduleEntity} and its DTO {@link SiteClassSchedule}.
 *
 * <p>This mapper transforms class schedule entities into their API-facing representations.</p>
 * <p>It uses {@link SiteUserMapper} for nested user mapping if necessary.</p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {SiteUserMapper.class})
public interface SiteClassScheduleMapper {

    /**
     * Converts a {@link SiteClassScheduleEntity} to a {@link SiteClassSchedule} DTO.
     *
     * @param classSchedule the entity to convert
     * @return the corresponding DTO
     */
    SiteClassSchedule toDto(SiteClassScheduleEntity classSchedule);
}