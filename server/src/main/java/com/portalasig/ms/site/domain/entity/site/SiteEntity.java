package com.portalasig.ms.site.domain.entity.site;

import com.portalasig.ms.commons.persistence.AbstractAuditEntity;
import com.portalasig.ms.site.domain.entity.MediaEntity;
import com.portalasig.ms.site.domain.entity.ReferenceEntity;
import com.portalasig.ms.site.domain.entity.SemesterEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import com.portalasig.ms.site.domain.entity.course.CourseObjectiveEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "site")
@Builder
public class SiteEntity extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")
    private Integer siteId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private SemesterEntity semester;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "site_objective_link",
            joinColumns = @JoinColumn(name = "site_id"),
            inverseJoinColumns = @JoinColumn(name = "course_objective_id")
    )
    private Set<CourseObjectiveEntity> objectives;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "site_reference_link",
            joinColumns = @JoinColumn(name = "site_id"),
            inverseJoinColumns = @JoinColumn(name = "reference_id")
    )
    private Set<ReferenceEntity> references;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "site_class_schedule_link",
            joinColumns = @JoinColumn(name = "site_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "site_class_schedule_id")
    )
    private Set<SiteClassScheduleEntity> classSchedules;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "site_assessment_link",
            joinColumns = @JoinColumn(name = "site_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "site_assessment_id")
    )
    private Set<SiteAssessmentEntity> assessments;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "site_news_link",
            joinColumns = @JoinColumn(name = "site_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "site_news_id")
    )
    private Set<SiteNewsEntity> news;

    @ManyToMany
    @JoinTable(
            name = "site_media_link",
            joinColumns = @JoinColumn(name = "site_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Set<MediaEntity> media;

    @ManyToMany
    @JoinTable(
            name = "site_user_link",
            joinColumns = @JoinColumn(name = "site_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<SiteUserEntity> relatedUsers;
}
