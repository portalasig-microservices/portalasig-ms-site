package com.portalasig.ms.site.repository;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SiteRepository extends JpaRepository<SiteEntity, Integer> {

    @Query("""
                SELECT site
                FROM SiteEntity  site
                WHERE site.course.code = :courseCode
                AND site.semester.periodYear = :periodYear
                AND site.semester.periodType = :periodType
            """)
    Optional<SiteEntity> findSite(
            @Param("courseCode") String courseCode,
            @Param("periodType") AcademicPeriodType periodType,
            @Param("periodYear") Integer periodYear
    );
}
