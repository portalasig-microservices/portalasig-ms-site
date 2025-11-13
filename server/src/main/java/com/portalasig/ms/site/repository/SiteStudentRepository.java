package com.portalasig.ms.site.repository;

import com.portalasig.ms.site.domain.entity.site.SiteStudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Site student repository.
 */
public interface SiteStudentRepository extends JpaRepository<SiteStudentEntity, Integer> {

    /**
     * Get students of all sections of a site.
     *
     * @param siteId   site id
     * @param pageable pageable object
     * @return Site students paginated
     */
    @Query("""
             SELECT studentEntity
             FROM SiteStudentEntity studentEntity
             WHERE studentEntity.section.site.siteId = :siteId
            """)
    Page<SiteStudentEntity> getStudentsBySiteId(
            @Param("siteId") Integer siteId,
            Pageable pageable
    );

}