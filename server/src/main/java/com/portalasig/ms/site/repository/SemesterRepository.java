package com.portalasig.ms.site.repository;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface
SemesterRepository extends JpaRepository<SemesterEntity, Integer> {

    @Query("""
            SELECT semester
            FROM SemesterEntity semester
            WHERE semester.isActive = true
            """)
    Optional<SemesterEntity> getActiveSemester();

    @Query("""
            SELECT semester
                        FROM SemesterEntity semester
                        WHERE semester.periodType = :periodType
                        AND semester.periodYear = :periodYear
            """)
    Optional<SemesterEntity> findByAcademicPeriod(@Param("periodType") AcademicPeriodType periodType,
                                                  @Param("periodYear") int periodYear
    );

    @Query("""
            SELECT semester
            FROM SemesterEntity semester
            WHERE semester.periodYear >= :currentYear
            AND semester.periodYear < :suggestedYear
            """)
    Optional<List<SemesterEntity>> findSuggestedSemesters(
            @Param("currentYear") int currentYear,
            @Param("suggestedYear") int suggestedYear
    );
}
