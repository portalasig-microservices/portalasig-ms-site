package com.portalasig.ms.site.domain.entity.course;

import com.portalasig.ms.commons.persistence.AbstractAuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Represents a topic associated with one or more courses.
 * <p>
 * Topics provide a way to categorize or describe the main content areas of a course.
 * This entity is auditable via {@link AbstractAuditEntity}.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course_topic")
@Builder
public class CourseTopicEntity extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_topic_id")
    private Integer courseTopicId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "topics", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @EqualsAndHashCode.Exclude
    private Set<CourseEntity> courses;
}
