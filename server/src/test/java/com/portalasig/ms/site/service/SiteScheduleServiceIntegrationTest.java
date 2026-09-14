package com.portalasig.ms.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.AbstractSiteIntegrationTest;
import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.constant.ScheduleType;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteParty;
import com.portalasig.ms.site.dto.site.SitePartyRequest;
import com.portalasig.ms.site.dto.site.SiteScheduleRequest;
import com.portalasig.ms.site.dto.site.SiteSectionRequest;
import com.portalasig.ms.uaa.dto.User;
import com.portalasig.ms.uaa.operation.UserOperations;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class SiteScheduleServiceIntegrationTest extends AbstractSiteIntegrationTest {

  private static final Long INSTRUCTOR_IDENTITY = 22345678L;

  @Autowired private SiteScheduleService siteScheduleService;

  @Autowired private SitePartyService sitePartyService;

  @Autowired private SiteService siteService;

  @MockBean(name = "clientCredentialsUserClientV1")
  private UserOperations userOperations;

  private Integer instructorPartyId;

  @BeforeEach
  void setUpInstructor() {
    when(userOperations.getUserByIdentity(INSTRUCTOR_IDENTITY))
        .thenReturn(
            User.builder()
                .identity(INSTRUCTOR_IDENTITY)
                .firstName("Profesor")
                .lastName("Prueba")
                .email("profesor@portalasig.ucv.ve")
                .build());
  }

  private SiteScheduleRequest baseRequest(String sectionCode) {
    return SiteScheduleRequest.builder()
        .section(SiteSectionRequest.builder().code(sectionCode).build())
        .scheduleType(ScheduleType.THEORY)
        .readOnly(false)
        .day(DayOfWeek.MONDAY)
        .startTime(LocalTime.of(8, 0))
        .endTime(LocalTime.of(10, 0))
        .location("Aula 101")
        .instructor(SiteParty.builder().partyId(instructorPartyId).build())
        .build();
  }

  private SiteEntity givenSiteWithInstructor() {
    SiteEntity site = givenSite();
    sitePartyService.addPartyToSite(
        site.getSiteId(),
        SitePartyRequest.builder()
            .identity(INSTRUCTOR_IDENTITY)
            .partyRole(PartyRole.PROFESSOR)
            .build());
    Site siteDto = siteService.findSite(COURSE_CODE, PERIOD_TYPE, PERIOD_YEAR);
    instructorPartyId = siteDto.getParties().get(0).getPartyId();
    return site;
  }

  @Test
  void upsertScheduleShouldCreateSectionAndScheduleTogether() {
    SiteEntity site = givenSiteWithInstructor();

    Site result = siteScheduleService.upsertSchedule(site.getSiteId(), baseRequest("01"));

    assertThat(result.getSections()).hasSize(1);
    assertThat(result.getSections().get(0).getSchedules())
        .anySatisfy(
            schedule -> {
              assertThat(schedule.getDay()).isEqualTo(DayOfWeek.MONDAY);
              assertThat(schedule.getLocation()).isEqualTo("Aula 101");
            });
  }

  @Test
  void upsertScheduleOnExistingSectionShouldReuseIt() {
    SiteEntity site = givenSiteWithInstructor();
    Site first = siteScheduleService.upsertSchedule(site.getSiteId(), baseRequest("01"));
    Integer sectionId = first.getSections().get(0).getSectionId();

    SiteScheduleRequest second = baseRequest(null);
    second.setSection(SiteSectionRequest.builder().sectionId(sectionId).build());
    second.setDay(DayOfWeek.WEDNESDAY);
    Site result = siteScheduleService.upsertSchedule(site.getSiteId(), second);

    assertThat(result.getSections()).hasSize(1);
    assertThat(result.getSections().get(0).getSchedules()).hasSize(2);
  }

  @Test
  void upsertScheduleWithExistingIdShouldUpdateIt() {
    SiteEntity site = givenSiteWithInstructor();
    Site created = siteScheduleService.upsertSchedule(site.getSiteId(), baseRequest("01"));
    Integer scheduleId = created.getSections().get(0).getSchedules().get(0).getScheduleId();

    SiteScheduleRequest update = baseRequest(null);
    update.setSection(SiteSectionRequest.builder().sectionId(created.getSections().get(0).getSectionId()).build());
    update.setScheduleId(scheduleId);
    update.setLocation("Laboratorio 2");
    update.setDay(DayOfWeek.FRIDAY);
    Site result = siteScheduleService.upsertSchedule(site.getSiteId(), update);

    assertThat(result.getSections().get(0).getSchedules()).hasSize(1);
    assertThat(result.getSections().get(0).getSchedules().get(0).getLocation())
        .isEqualTo("Laboratorio 2");
    assertThat(result.getSections().get(0).getSchedules().get(0).getDay()).isEqualTo(DayOfWeek.FRIDAY);
  }

  @Test
  void deleteScheduleShouldRemoveIt() {
    SiteEntity site = givenSiteWithInstructor();
    Site created = siteScheduleService.upsertSchedule(site.getSiteId(), baseRequest("01"));
    Integer sectionId = created.getSections().get(0).getSectionId();
    Integer scheduleId = created.getSections().get(0).getSchedules().get(0).getScheduleId();

    Site result = siteScheduleService.deleteSchedule(site.getSiteId(), sectionId, scheduleId);

    assertThat(result.getSections().get(0).getSchedules()).isEmpty();
  }

  @Test
  void updateScheduleShouldMoveItToAnotherSection() {
    SiteEntity site = givenSiteWithInstructor();
    Site first = siteScheduleService.upsertSchedule(site.getSiteId(), baseRequest("01"));
    Integer scheduleId = first.getSections().get(0).getSchedules().get(0).getScheduleId();
    Site second = siteScheduleService.upsertSchedule(site.getSiteId(), baseRequest("02"));
    Integer targetSectionId =
        second.getSections().stream()
            .filter(section -> section.getCode().equals("02"))
            .findFirst()
            .orElseThrow()
            .getSectionId();

    SiteScheduleRequest move = baseRequest(null);
    move.setSection(SiteSectionRequest.builder().sectionId(targetSectionId).build());
    move.setScheduleId(scheduleId);
    Site result = siteScheduleService.upsertSchedule(site.getSiteId(), move);

    // the schedule moved to section 02 and the now-empty section 01 was removed
    assertThat(result.getSections()).hasSize(1);
    assertThat(result.getSections().get(0).getCode()).isEqualTo("02");
    assertThat(result.getSections().get(0).getSchedules()).hasSize(2);
  }

  @Test
  void updateUnknownScheduleShouldThrowNotFound() {
    SiteEntity site = givenSiteWithInstructor();
    siteScheduleService.upsertSchedule(site.getSiteId(), baseRequest("01"));
    SiteScheduleRequest update = baseRequest("01");
    update.setScheduleId(999999);

    assertThatThrownBy(() -> siteScheduleService.upsertSchedule(site.getSiteId(), update))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void upsertScheduleOnUnknownSiteShouldThrowNotFound() {
    instructorPartyId = 1;
    assertThatThrownBy(() -> siteScheduleService.upsertSchedule(999999, baseRequest("01")))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
