package com.portalasig.ms.site.domain.entity.site;

import com.portalasig.ms.site.constant.SiteUserRoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "site_user_role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteUserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_user_role_id")
    private Long siteUserRoleId;

    @ManyToOne
    @JoinColumn(name = "site_id", insertable = false, updatable = false)
    private SiteEntity site;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private SiteUserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "site_user_role_type", nullable = false)
    private SiteUserRoleType siteUserRoleType;
}