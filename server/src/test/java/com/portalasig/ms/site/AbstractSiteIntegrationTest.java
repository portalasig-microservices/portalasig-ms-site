package com.portalasig.ms.site;

import com.portalasig.ms.commons.test.AbstractMysqlIntegrationTest;
import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.constant.CourseLevelType;
import com.portalasig.ms.site.constant.CourseType;
import com.portalasig.ms.site.domain.entity.CareerEntity;
import com.portalasig.ms.site.domain.entity.SemesterEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.repository.CareerRepository;
import com.portalasig.ms.site.repository.CourseRepository;
import com.portalasig.ms.site.repository.SemesterRepository;
import com.portalasig.ms.site.repository.SiteRepository;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for ms-site integration tests providing persisted reference data (career, course,
 * semester, site) required by most service scenarios.
 */
public abstract class AbstractSiteIntegrationTest extends AbstractMysqlIntegrationTest {

  protected static final String COURSE_CODE = "MA111";
  protected static final AcademicPeriodType PERIOD_TYPE = AcademicPeriodType.FIRST;
  protected static final Integer PERIOD_YEAR = 2025;

  @Autowired protected CareerRepository careerRepository;

  @Autowired protected CourseRepository courseRepository;

  @Autowired protected SemesterRepository semesterRepository;

  @Autowired protected SiteRepository siteRepository;

  protected CareerEntity givenCareer() {
    CareerEntity career = CareerEntity.builder().name("Computacion").build();
    return careerRepository.save(career);
  }

  protected CourseEntity givenCourse() {
    CareerEntity career = givenCareer();
    CourseEntity course =
        CourseEntity.builder()
            .code(COURSE_CODE)
            .name("Matematica I")
            .creditUnits(4)
            .type(CourseType.MANDATORY)
            .courseLevel(CourseLevelType.FIRST)
            .requirements("Ninguno")
            .careers(Set.of(career))
            .build();
    return courseRepository.save(course);
  }

  protected SemesterEntity givenSemester() {
    SemesterEntity semester =
        SemesterEntity.builder()
            .periodType(PERIOD_TYPE)
            .periodYear(PERIOD_YEAR)
            .startDate(LocalDate.of(2025, 3, 1))
            .endDate(LocalDate.of(2025, 7, 31))
            .isActive(true)
            .build();
    return semesterRepository.save(semester);
  }

  protected SiteEntity givenSite() {
    CourseEntity course = givenCourse();
    SemesterEntity semester = givenSemester();
    SiteEntity site = SiteEntity.builder().course(course).semester(semester).build();
    return siteRepository.save(site);
  }
}
