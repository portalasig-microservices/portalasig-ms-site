package com.portalasig.ms.site.repository;

import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link SemesterEntity} persistence.
 * Provides custom query methods for accessing semester data based on academic period,
 * active status, and suggested year range.
 */
public interface SemesterRepository extends JpaRepository<SemesterEntity, Integer> {

    /**
     * Retrieves the currently active semester.
     *
     * @return an {@link Optional} containing the active {@link SemesterEntity}, if one exists
     */
    @Query("""
            SELECT semester
            FROM SemesterEntity semester
            WHERE semester.isActive = true
            """)
    Optional<SemesterEntity> getActiveSemester();

    /**
     * Finds a semester by its academic period type and year.
     *
     * @param periodType the type of academic period (e.g., FIRST, SECOND, INTENSIVE)
     * @param periodYear the year of the academic period
     * @return an {@link Optional} containing the matching {@link SemesterEntity}, if found
     */
    @Query("""
            SELECT semester
            FROM SemesterEntity semester
            WHERE semester.periodType = :periodType
            AND semester.periodYear = :periodYear
            """)
    Optional<SemesterEntity> findByAcademicPeriod(
            @Param("periodType") AcademicPeriodType periodType,
            @Param("periodYear") int periodYear
    );

    /**
     * Retrieves a list of suggested semesters within a year range.
     *
     * @param currentYear   the lower bound year (inclusive)
     * @param suggestedYear the upper bound year (exclusive)
     * @return an {@link Optional} containing a list of {@link SemesterEntity} instances that fall within the specified range
     */
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