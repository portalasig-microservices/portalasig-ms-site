package com.portalasig.ms.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.portalasig.ms.commons.rest.exception.ConflictException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.commons.rest.exception.SystemErrorException;
import com.portalasig.ms.site.AbstractSiteIntegrationTest;
import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.dto.site.SiteRequest;
import com.portalasig.ms.uaa.dto.User;
import com.portalasig.ms.uaa.operation.AdminUserOperations;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class SiteServiceIntegrationTest extends AbstractSiteIntegrationTest {

  private static final Long PROFESSOR_IDENTITY = 22345678L;

  @Autowired private SiteService siteService;

  @MockBean(name = "tokenRelayAdminUserClientV1")
  private AdminUserOperations adminUserOperations;

  private static SiteRequest baseRequest(List<SiteParty> parties) {
    return SiteRequest.builder()
        .courseCode(COURSE_CODE)
        .periodType(PERIOD_TYPE)
        .periodYear(PERIOD_YEAR)
        .parties(parties)
        .build();
  }

  @Test
  void createSiteShouldPersistSiteWithParties() {
    givenCourse();
    givenSemester();
    SiteParty professor =
        SiteParty.builder().identity(PROFESSOR_IDENTITY).partyRole(PartyRole.PROFESSOR).build();
    User uaaUser =
        User.builder()
            .identity(PROFESSOR_IDENTITY)
            .firstName("Profesor")
            .lastName("Prueba")
            .email("profesor@portalasig.ucv.ve")
            .build();
    when(adminUserOperations.getUsers(anyList())).thenReturn(List.of(uaaUser));

    Site site = siteService.createSite(baseRequest(List.of(professor)));

    assertThat(site.getSiteId()).isNotNull();
    assertThat(site.getParties())
        .anySatisfy(
            party -> {
              assertThat(party.getIdentity()).isEqualTo(PROFESSOR_IDENTITY);
              assertThat(party.getPartyRole()).isEqualTo(PartyRole.PROFESSOR);
            });
  }

  @Test
  void createSiteForExistingCourseAndPeriodShouldThrowConflict() {
    givenSite();
    when(adminUserOperations.getUsers(anyList())).thenReturn(List.of());

    assertThatThrownBy(() -> siteService.createSite(baseRequest(List.of())))
        .isInstanceOf(ConflictException.class);
  }

  @Test
  void createSiteWithUnknownCourseShouldThrowNotFound() {
    givenSemester();

    assertThatThrownBy(() -> siteService.createSite(baseRequest(List.of())))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void createSiteWithUnknownSemesterShouldThrowSystemError() {
    givenCourse();

    assertThatThrownBy(() -> siteService.createSite(baseRequest(List.of())))
        .isInstanceOf(SystemErrorException.class);
  }

  @Test
  void findSiteShouldReturnExistingSite() {
    givenSite();

    Site site = siteService.findSite(COURSE_CODE, PERIOD_TYPE, PERIOD_YEAR);

    assertThat(site.getSiteId()).isNotNull();
  }

  @Test
  void deleteSiteShouldRemoveItWithItsContents() {
    SiteEntity site = givenSite();

    siteService.deleteSite(site.getSiteId());

    assertThat(siteRepository.findById(site.getSiteId())).isEmpty();
  }
}
