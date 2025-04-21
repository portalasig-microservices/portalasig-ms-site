package com.portalasig.ms.site.repository;

import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    Optional<CourseEntity> findByCode(@NotNull String code);

    void deleteByCode(@NotNull String code);
}
