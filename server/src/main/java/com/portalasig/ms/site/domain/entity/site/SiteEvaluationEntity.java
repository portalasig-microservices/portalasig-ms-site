package com.portalasig.ms.site.domain.entity.site;

import com.portalasig.ms.commons.persistence.AbstractAuditEntity;
import com.portalasig.ms.site.constant.EvaluationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Represents an evaluation associated with a site.
 * <p>
 * These may include exams, projects, or other graded activities.
 * This entity supports auditing through {@link AbstractAuditEntity}.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "site_evaluation")
@Builder
public class SiteEvaluationEntity extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_evaluation_id")
    private Integer evaluationId;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    @NotNull
    private Float weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_type")
    private EvaluationType evaluationType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "site_id")
    @EqualsAndHashCode.Exclude
    private SiteEntity site;
}
