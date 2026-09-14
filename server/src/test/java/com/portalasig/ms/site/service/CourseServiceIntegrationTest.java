package com.portalasig.ms.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.commons.rest.exception.BadRequestException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.AbstractSiteIntegrationTest;
import com.portalasig.ms.site.constant.CourseLevelType;
import com.portalasig.ms.site.constant.CourseType;
import com.portalasig.ms.site.domain.entity.CareerEntity;
import com.portalasig.ms.site.dto.course.Course;
import com.portalasig.ms.site.dto.course.CourseRequest;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class CourseServiceIntegrationTest extends AbstractSiteIntegrationTest {

  @Autowired private CourseService courseService;

  private CourseRequest baseRequest(String code, Integer careerId) {
    return CourseRequest.builder()
        .code(code)
        .name("Calculo I")
        .creditUnits(4)
        .type(CourseType.MANDATORY)
        .courseLevel(CourseLevelType.FIRST)
        .requirements("Ninguno")
        .careers(List.of(careerId))
        .build();
  }

  @Test
  void upsertShouldCreateNewCourse() {
    CareerEntity career = givenCareer();

    Course course = courseService.upsert(baseRequest("MA212", career.getCareerId()));

    assertThat(course.getCourseId()).isNotNull();
    assertThat(course.getCode()).isEqualTo("MA212");
    assertThat(courseRepository.findByCode("MA212")).isPresent();
  }

  @Test
  void upsertShouldUpdateExistingCourse() {
    CareerEntity career = givenCareer();
    courseService.upsert(baseRequest("MA212", career.getCareerId()));

    CourseRequest updateRequest = baseRequest("MA212", career.getCareerId());
    updateRequest.setName("Calculo Avanzado");
    Course updated = courseService.upsert(updateRequest);

    assertThat(updated.getName()).isEqualTo("Calculo Avanzado");
    assertThat(courseRepository.findByCode("MA212")).isPresent();
  }

  @Test
  void upsertWithInvalidTypeShouldThrowBadRequest() {
    CareerEntity career = givenCareer();
    CourseRequest request = baseRequest("MA212", career.getCareerId());
    request.setType(CourseType.INVALID);

    assertThatThrownBy(() -> courseService.upsert(request))
        .isInstanceOf(BadRequestException.class);
  }

  @Test
  void upsertWithUnknownCareerShouldThrowNotFound() {
    CourseRequest request = baseRequest("MA212", 999999);

    assertThatThrownBy(() -> courseService.upsert(request))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void findAllShouldReturnPaginatedCourses() {
    givenCourse();

    Paginated<Course> result = courseService.findAll(PageRequest.of(0, 10));

    assertThat(result.getTotalElements()).isEqualTo(1);
    assertThat(result.getContent().get(0).getCode()).isEqualTo(COURSE_CODE);
  }

  @Test
  void deleteCourseByCodeShouldRemoveIt() {
    givenCourse();

    courseService.deleteCourseByCode(COURSE_CODE);

    assertThat(courseRepository.findByCode(COURSE_CODE)).isEmpty();
  }

  @Test
  void deleteUnknownCourseShouldThrowNotFound() {
    assertThatThrownBy(() -> courseService.deleteCourseByCode("NO_EXISTE"))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void findByCodeShouldReturnCourse() {
    givenCourse();

    Course course = courseService.findByCode(COURSE_CODE);

    assertThat(course.getName()).isEqualTo("Matematica I");
  }

  @Test
  void importCoursesFromCsvShouldPersistCourses() {
    CareerEntity career = givenCareer();
    String csv =
        "code,name,credits,type,level,requirements,careers\n"
            + "MA611,Algebra Lineal,4,MANDATORY,Ninguno," + career.getCareerId() + ",FIRST\n"
            + "MA712,Estadistica,3,MANDATORY,Ninguno,,SECOND\n";

    courseService.importCoursesFromCsv(
        new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

    assertThat(courseRepository.findByCode("MA611")).isPresent();
    assertThat(courseRepository.findByCode("MA712")).isPresent();
  }

  @Test
  void importCoursesFromCsvWithInvalidHeaderShouldThrowBadRequest() {
    String csv = "foo,bar\n1,2\n";

    assertThatThrownBy(
            () ->
                courseService.importCoursesFromCsv(
                    new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))))
        .isInstanceOf(BadRequestException.class);
  }
}
