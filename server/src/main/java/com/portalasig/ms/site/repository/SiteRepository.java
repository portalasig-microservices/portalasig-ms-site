package com.portalasig.ms.site.repository;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link SiteEntity} data.
 * Extends {@link JpaRepository} to provide standard CRUD operations and includes
 * a custom query method to locate a site by its course code and academic period.
 */
public interface SiteRepository extends JpaRepository<SiteEntity, Integer> {

    /**
     * Finds a site by the course code, academic period type, and year.
     *
     * @param courseCode the code of the course
     * @param periodType the academic period type (e.g., FIRST, SECOND)
     * @param periodYear the academic year
     * @return an {@link Optional} containing the matching {@link SiteEntity}, if found
     */
    @Query("""
             SELECT site
             FROM SiteEntity site
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