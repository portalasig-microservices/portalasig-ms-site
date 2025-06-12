package com.portalasig.ms.site.domain.entity.site;

import com.portalasig.ms.commons.persistence.AbstractAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing a course topic associated with a site.
 * <p>
 * Maps to the <code>site_course_topic</code> table and stores information about
 * individual topics, including title and description, for a specific site.
 * </p>
 * <p>
 * Inherits auditing fields from {@link com.portalasig.ms.commons.persistence.AbstractAuditEntity}.
 * </p>
 *
 * @author fuhranku
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "site_course_topic")
@Builder
public class SiteCourseTopicEntity extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_course_topic_id")
    private Integer siteCourseTopicId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private SiteEntity site;
}
