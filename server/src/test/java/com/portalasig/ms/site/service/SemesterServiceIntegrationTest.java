package com.portalasig.ms.site.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.site.AbstractSiteIntegrationTest;
import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.SemesterEntity;
import com.portalasig.ms.site.dto.semester.Semester;
import com.portalasig.ms.site.dto.semester.SemesterRequest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class SemesterServiceIntegrationTest extends AbstractSiteIntegrationTest {

  @Autowired private SemesterService semesterService;

  private static SemesterRequest baseRequest(AcademicPeriodType type, Integer year) {
    return SemesterRequest.builder()
        .periodType(type)
        .periodYear(year)
        .name(String.format("%s-%s", type.getCode(), year))
        .startDate(LocalDate.of(year, 3, 1))
        .endDate(LocalDate.of(year, 7, 31))
        .description("Semestre de prueba")
        .build();
  }

  @Test
  void upsertShouldCreateSemester() {
    Semester semester = semesterService.upsert(baseRequest(AcademicPeriodType.FIRST, 2026));

    assertThat(semester.getSemesterId()).isNotNull();
    assertThat(semester.getPeriodYear()).isEqualTo(2026);
  }

  @Test
  void upsertShouldUpdateExistingSemester() {
    Semester created = semesterService.upsert(baseRequest(AcademicPeriodType.FIRST, 2026));

    SemesterRequest updateRequest = baseRequest(AcademicPeriodType.FIRST, 2026);
    updateRequest.setSemesterId(created.getSemesterId());
    updateRequest.setEndDate(LocalDate.of(2026, 8, 31));
    Semester updated = semesterService.upsert(updateRequest);

    assertThat(updated.getSemesterId()).isEqualTo(created.getSemesterId());
    assertThat(updated.getEndDate()).isEqualTo(LocalDate.of(2026, 8, 31));
    assertThat(semesterRepository.count()).isEqualTo(1);
  }

  @Test
  void findAllShouldReturnPaginatedSemesters() {
    semesterService.upsert(baseRequest(AcademicPeriodType.FIRST, 2026));

    Paginated<Semester> result = semesterService.findAll(PageRequest.of(0, 10));

    assertThat(result.getTotalElements()).isEqualTo(1);
  }

  @Test
  void deleteShouldRemoveSemester() {
    Semester created = semesterService.upsert(baseRequest(AcademicPeriodType.SECOND, 2026));

    semesterService.delete(created.getSemesterId());

    assertThat(semesterRepository.findById(created.getSemesterId())).isEmpty();
  }

  @Test
  void getActiveSemesterShouldReturnTheActiveOne() {
    givenSemester();

    Semester active = semesterService.getActiveSemester();

    assertThat(active.getPeriodType()).isEqualTo(PERIOD_TYPE);
    assertThat(active.getPeriodYear()).isEqualTo(PERIOD_YEAR);
  }

  @Test
  void getSuggestedSemestersShouldIncludeRecentPeriods() {
    int currentYear = LocalDate.now().getYear();
    semesterRepository.save(
        SemesterEntity.builder()
            .periodType(PERIOD_TYPE)
            .periodYear(currentYear)
            .startDate(LocalDate.of(currentYear, 3, 1))
            .endDate(LocalDate.of(currentYear, 7, 31))
            .isActive(true)
            .build());

    List<Semester> suggested = semesterService.getSuggestedSemesters(5);

    assertThat(suggested).isNotEmpty();
    assertThat(suggested)
        .anySatisfy(semester -> assertThat(semester.getPeriodYear()).isEqualTo(currentYear));
  }
}
