package com.portalasig.ms.site.repository;

import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link CourseEntity} persistence.
 * Provides standard CRUD operations and custom query methods for course code lookup.
 */
public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

    /**
     * Finds a course by its unique code.
     *
     * @param code the course code to look up (must not be null)
     * @return an {@link Optional} containing the course if found, or empty otherwise
     */
    Optional<CourseEntity> findByCode(@NotNull String code);

    /**
     * Deletes a course by its unique code.
     *
     * @param code the course code to delete (must not be null)
     */
    void deleteByCode(@NotNull String code);
}
