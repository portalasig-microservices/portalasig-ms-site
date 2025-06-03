package com.portalasig.ms.site.domain.entity.site;

import com.portalasig.ms.commons.persistence.AbstractAuditEntity;
import com.portalasig.ms.site.constant.PartyRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Represents a party involved in a site, such as a professor or assistant,
 * with associated identity and role information.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "site_party")
@Builder
public class SitePartyEntity extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Integer partyId;

    @Column(name = "identity")
    private Long identity;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "party_role")
    @Enumerated(EnumType.STRING)
    private PartyRole partyRole;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SiteClassScheduleEntity> classSchedules;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private SiteEntity site;
}
