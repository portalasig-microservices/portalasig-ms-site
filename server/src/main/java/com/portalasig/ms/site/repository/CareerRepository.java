package com.portalasig.ms.site.repository;

import com.portalasig.ms.site.domain.entity.CareerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing {@link CareerEntity} data.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
public interface CareerRepository extends JpaRepository<CareerEntity, Integer> {
}
