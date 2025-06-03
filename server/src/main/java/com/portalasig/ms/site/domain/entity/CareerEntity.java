package com.portalasig.ms.site.domain.entity;

import com.portalasig.ms.commons.persistence.AbstractAuditEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Represents an academic career (degree program) that can be associated with multiple courses.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "career")
@Builder
public class CareerEntity extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Integer careerId;

    @NotNull
    @Column(name = "name", length = 32)
    private String name;

    @Transient
    private boolean shouldBeRemoved;

    @ManyToMany(mappedBy = "careers", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @EqualsAndHashCode.Exclude
    private Set<CourseEntity> courses;
}
