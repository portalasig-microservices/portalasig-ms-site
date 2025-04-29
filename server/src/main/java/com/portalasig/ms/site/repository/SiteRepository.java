package com.portalasig.ms.site.repository;

import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteRepository extends JpaRepository<SiteEntity, Integer> {

    @Query("""
            SELECT COUNT(site) > 0
            FROM SiteEntity site
            WHERE site.course.code = :courseCode
              AND site.semester.periodType = :periodType
              AND site.semester.periodYear = :periodYear
            """)
    boolean checkIfSiteExists(
            @Param("courseCode") String courseCode,
            @Param("periodType") String periodType,
            @Param("periodYear") int periodYear
    );
}
