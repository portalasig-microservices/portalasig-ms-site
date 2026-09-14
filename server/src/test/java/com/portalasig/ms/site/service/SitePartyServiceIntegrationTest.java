package com.portalasig.ms.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.commons.rest.exception.BadRequestException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.notify.operation.EmailOperations;
import com.portalasig.ms.site.AbstractSiteIntegrationTest;
import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.dto.site.SitePartiesRequest;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
import com.portalasig.ms.site.dto.site.SiteSectionRequest;
import com.portalasig.ms.uaa.dto.User;
import com.portalasig.ms.uaa.operation.AdminUserOperations;
import com.portalasig.ms.uaa.operation.UserOperations;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

class SitePartyServiceIntegrationTest extends AbstractSiteIntegrationTest {

  private static final Long IDENTITY = 32345678L;

  @Autowired private SitePartyService sitePartyService;

  @Autowired private SiteSectionService siteSectionService;

  @MockBean(name = "clientCredentialsUserClientV1")
  private UserOperations userOperations;

  @MockBean(name = "tokenRelayAdminUserClientV1")
  private AdminUserOperations adminUserOperations;

  @MockBean(name = "clientCredentialsEmailClientV1")
  private EmailOperations emailOperations;

  private User mockUaaUser() {
    User user =
        User.builder()
            .identity(IDENTITY)
            .firstName("Estudiante")
            .lastName("Prueba")
            .email("estudiante@portalasig.ucv.ve")
            .build();
    when(userOperations.getUserByIdentity(IDENTITY)).thenReturn(user);
    return user;
  }

  @Autowired private SiteService siteService;

  @Test
  void addProfessorToSiteShouldRegisterParty() {
    SiteEntity site = givenSite();
    mockUaaUser();
    SitePartyRequest request =
        SitePartyRequest.builder().identity(IDENTITY).partyRole(PartyRole.PROFESSOR).build();

    sitePartyService.addPartyToSite(site.getSiteId(), request);

    Site siteDto = siteService.findSite(COURSE_CODE, PERIOD_TYPE, PERIOD_YEAR);
    assertThat(siteDto.getParties())
        .anySatisfy(
            party -> {
              assertThat(party.getIdentity()).isEqualTo(IDENTITY);
              assertThat(party.getPartyRole()).isEqualTo(PartyRole.PROFESSOR);
            });
  }

  @Test
  void addStudentWithoutSectionShouldThrowBadRequest() {
    SiteEntity site = givenSite();
    mockUaaUser();
    SitePartyRequest request =
        SitePartyRequest.builder().identity(IDENTITY).partyRole(PartyRole.STUDENT).build();

    assertThatThrownBy(() -> sitePartyService.addPartyToSite(site.getSiteId(), request))
        .isInstanceOf(BadRequestException.class);
  }

  @Test
  void addStudentToSectionShouldRegisterIt() {
    SiteEntity site = givenSite();
    Site withSection =
        siteSectionService.upsertSection(
            site.getSiteId(), SiteSectionRequest.builder().code("01").build());
    Integer sectionId = withSection.getSections().get(0).getSectionId();
    mockUaaUser();
    SitePartyRequest request =
        SitePartyRequest.builder()
            .identity(IDENTITY)
            .partyRole(PartyRole.STUDENT)
            .sectionId(sectionId)
            .build();

    sitePartyService.addPartyToSite(site.getSiteId(), request);

    Paginated<SiteParty> students =
        sitePartyService.getStudents(site.getSiteId(), PageRequest.of(0, 10));
    assertThat(students.getTotalElements()).isEqualTo(1);
    assertThat(students.getContent().get(0).getIdentity()).isEqualTo(IDENTITY);
  }

  @Test
  void addPartyWithUnknownUserShouldThrowNotFound() {
    SiteEntity site = givenSite();
    when(userOperations.getUserByIdentity(IDENTITY)).thenReturn(null);
    SitePartyRequest request =
        SitePartyRequest.builder().identity(IDENTITY).partyRole(PartyRole.PROFESSOR).build();

    assertThatThrownBy(() -> sitePartyService.addPartyToSite(site.getSiteId(), request))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void deletePartyByIdentityShouldRemoveIt() {
    SiteEntity site = givenSite();
    mockUaaUser();
    sitePartyService.addPartyToSite(
        site.getSiteId(),
        SitePartyRequest.builder().identity(IDENTITY).partyRole(PartyRole.PROFESSOR).build());

    sitePartyService.deletePartyByIdentity(site.getSiteId(), IDENTITY, PartyRole.PROFESSOR, null);

    Site siteDto = siteService.findSite(COURSE_CODE, PERIOD_TYPE, PERIOD_YEAR);
    assertThat(siteDto.getParties()).isEmpty();
  }

  @Test
  void processBulkPatchPartiesShouldUpdateExistingAndCreateNewOnes() {
    SiteEntity site = givenSite();
    mockUaaUser();
    sitePartyService.addPartyToSite(
        site.getSiteId(),
        SitePartyRequest.builder().identity(IDENTITY).partyRole(PartyRole.PROFESSOR).build());

    Long newIdentity = 11111111L;
    User updatedExistingUser =
        User.builder()
            .identity(IDENTITY)
            .firstName("NombreActualizado")
            .lastName("Prueba")
            .email("actualizado@portalasig.ucv.ve")
            .build();
    User newUser =
        User.builder()
            .identity(newIdentity)
            .firstName("Coordinador")
            .lastName("Nuevo")
            .email("coordinador@portalasig.ucv.ve")
            .build();
    when(adminUserOperations.getUsers(List.of(IDENTITY, newIdentity)))
        .thenReturn(List.of(updatedExistingUser, newUser));

    SitePartiesRequest request =
        SitePartiesRequest.builder()
            .parties(
                List.of(
                    SiteParty.builder().identity(IDENTITY).partyRole(PartyRole.PROFESSOR).build(),
                    SiteParty.builder()
                        .identity(newIdentity)
                        .partyRole(PartyRole.COORDINATOR)
                        .build()))
            .build();

    sitePartyService.processBulkPatchParties(site.getSiteId(), request);

    Site siteDto = siteService.findSite(COURSE_CODE, PERIOD_TYPE, PERIOD_YEAR);
    assertThat(siteDto.getParties()).hasSize(2);
    assertThat(siteDto.getParties())
        .anySatisfy(
            party -> {
              assertThat(party.getIdentity()).isEqualTo(IDENTITY);
              assertThat(party.getFirstName()).isEqualTo("NombreActualizado");
            });
    assertThat(siteDto.getParties())
        .anySatisfy(
            party -> {
              assertThat(party.getIdentity()).isEqualTo(newIdentity);
              assertThat(party.getPartyRole()).isEqualTo(PartyRole.COORDINATOR);
            });
    // patchParties() removes existing parties from the map, so only newly assigned users are notified
    verify(emailOperations, times(1)).sendEmail(any());
  }

  @Test
  void findPartiesShouldFilterByQueryAndRolesIgnoringAccents() {
    SiteEntity site = givenSite();
    User professor =
        User.builder()
            .identity(IDENTITY)
            .firstName("José")
            .lastName("Pérez")
            .email("jose@portalasig.ucv.ve")
            .build();
    when(userOperations.getUserByIdentity(IDENTITY)).thenReturn(professor);
    sitePartyService.addPartyToSite(
        site.getSiteId(),
        SitePartyRequest.builder().identity(IDENTITY).partyRole(PartyRole.PROFESSOR).build());

    List<SiteParty> matches =
        sitePartyService.findParties(site.getSiteId(), "jose", List.of(PartyRole.PROFESSOR));

    assertThat(matches).hasSize(1);
    assertThat(matches.get(0).getFirstName()).isEqualTo("José");
    // null roles default to STUDENT only: the professor must not appear
    assertThat(sitePartyService.findParties(site.getSiteId(), null, null)).isEmpty();
    assertThat(sitePartyService.findParties(site.getSiteId(), "xyz", List.of(PartyRole.PROFESSOR)))
        .isEmpty();
  }

  @Test
  void getPartyByPartyIdShouldReturnMatchingParty() {
    SiteEntity site = givenSite();
    mockUaaUser();
    sitePartyService.addPartyToSite(
        site.getSiteId(),
        SitePartyRequest.builder().identity(IDENTITY).partyRole(PartyRole.PROFESSOR).build());
    Site siteDto = siteService.findSite(COURSE_CODE, PERIOD_TYPE, PERIOD_YEAR);
    Integer partyId = siteDto.getParties().get(0).getPartyId();

    SiteParty found = sitePartyService.getPartyByPartyId(site.getSiteId(), partyId, PartyRole.PROFESSOR);

    assertThat(found).isNotNull();
    assertThat(found.getIdentity()).isEqualTo(IDENTITY);
    assertThat(sitePartyService.getPartyByPartyId(site.getSiteId(), partyId, PartyRole.STUDENT_TEACHER))
        .isNull();
  }
}
