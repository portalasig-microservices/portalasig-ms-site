package com.portalasig.ms.site.mapper;

import com.portalasig.ms.site.domain.entity.CareerEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        SiteClassScheduleMapper.class,
        SiteAssessmentMapper.class,
        SiteNewsMapper.class,
        MediaMapper.class,
        SiteUserMapper.class,
        SemesterMapper.class,
        ReferenceMapper.class,
})
public interface SiteMapper {

    Site toDto(SiteEntity siteEntity);

    default List<Integer> mapCareers(Set<CareerEntity> careers) {
        return careers.stream().map(CareerEntity::getCareerId).toList();
    }
}
