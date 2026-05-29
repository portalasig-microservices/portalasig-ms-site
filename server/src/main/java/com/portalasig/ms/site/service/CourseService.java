package com.portalasig.ms.site.service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.commons.rest.exception.BadRequestException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.commons.rest.exception.SystemErrorException;
import com.portalasig.ms.site.constant.CourseType;
import com.portalasig.ms.site.converter.CourseConverter;
import com.portalasig.ms.site.domain.entity.CareerEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import com.portalasig.ms.site.dto.course.Course;
import com.portalasig.ms.site.dto.course.CourseRequest;
import com.portalasig.ms.site.dto.course.CsvCourse;
import com.portalasig.ms.site.mapper.CourseMapper;
import com.portalasig.ms.site.repository.CareerRepository;
import com.portalasig.ms.site.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service for managing course operations, including create, update, delete, retrieve, and CSV import.
 * Validates course code format to ensure compliance with institutional naming conventions (e.g., MAT-1234).
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final CareerRepository careerRepository;
    private final CourseMapper courseMapper;
    private final CourseConverter courseConverter;

    @Value("${site.tools.courses.csv.input-header}")
    private final HashSet<String> inputCsvHeader;

    /**
     * Returns all courses in paginated form.
     */
    public Paginated<Course> findAll(Pageable pageable) {
        Page<CourseEntity> courses = courseRepository.findAll(pageable);
        if (courses.isEmpty()) {
            throw new ResourceNotFoundException("No courses found");
        }
        return Paginated.wrap(courses.map(courseMapper::toDto));
    }

    /**
     * Creates or updates a course based on the given request.
     */
    @Transactional
    public Course upsert(CourseRequest request) {
        validateRequest(request);
        List<CareerEntity> careers = careerRepository.findAllById(request.getCareers());
        if (careers.isEmpty()) {
            throw new ResourceNotFoundException("No careers found. Skipping Course upsert");
        }

        CourseEntity course;
        Optional<CourseEntity> courseEntity = courseRepository.findByCode(request.getCode());
        if (courseEntity.isEmpty()) {
            course = courseMapper.toEntity(request);
            course.setCareers(new HashSet<>(careers));
        } else {
            course = courseEntity.get();
            courseMapper.toEntityFromExisting(course, request);
            updateCareers(course, request);
        }

        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    private void updateCareers(CourseEntity course, CourseRequest request) {
        if (request.getCareers() == null) {
            return;
        }
        Set<CareerEntity> incomingCareers = new HashSet<>(careerRepository.findAllById(request.getCareers()));
        courseConverter.updateCareers(course, incomingCareers);
    }

    private void validateRequest(CourseRequest request) {
        if (request.getType() == null || CourseType.INVALID.equals(request.getType())) {
            throw new BadRequestException("Course type is invalid");
        }
    }

    /**
     * Deletes a course by its code.
     */
    @Transactional
    public void deleteCourseByCode(String courseCode) {
        courseRepository.findByCode(courseCode).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Course with code=%s not found", courseCode)
                )
        );
        try {
            courseRepository.deleteByCode(courseCode);
        } catch (Exception e) {
            log.error("Error deleting course with code={}", courseCode, e);
            throw new ResourceNotFoundException(
                    String.format("Error deleting course with code=%s", courseCode)
            );
        }
    }

    /**
     * Imports course data from a CSV file.
     */
    public void importCoursesFromCsv(InputStream stream) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(stream))) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            String[] header = reader.readNext(); // skip header
            validateHeader(Arrays.asList(header));

            List<CsvCourse> csvCourses = new CsvToBeanBuilder<CsvCourse>(reader)
                    .withType(CsvCourse.class)
                    .build()
                    .parse();

            log.info("Starting courses import from csv with courses_size={}", csvCourses.size());
            List<CourseEntity> courseEntities = csvCourses.stream().map(this::toEntityFromCsv).toList();
            courseRepository.saveAll(courseEntities);
            stopWatch.stop();
            log.info("Import courses from csv finished in {}ms", stopWatch.getTotalTimeMillis());

        } catch (CsvValidationException | IOException e) {
            throw new SystemErrorException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Something went wrong while parsing csv file",
                    e
            );
        }
    }

    private CourseEntity toEntityFromCsv(CsvCourse csvCourse) {
        CourseEntity entity = courseMapper.toEntityFromCsv(csvCourse);
        entity.setCareers(new HashSet<>(getCareers(csvCourse)));
        return entity;
    }

    private List<CareerEntity> getCareers(CsvCourse csvCourse) {
        try {
            return csvCourse.getCareers() != null
                    ? careerRepository.findAllById(csvCourse.getCareers())
                    : new ArrayList<>();
        } catch (Exception e) {
            log.error("Error when fetching career for course_id={}, skipping", csvCourse.getCode(), e);
            return new ArrayList<>();
        }
    }

    private void validateHeader(List<String> fileHeader) {
        HashSet<String> fileHeaderSet = new HashSet<>(fileHeader);
        if (!fileHeaderSet.containsAll(inputCsvHeader)) {
            throw new BadRequestException("Invalid csv header");
        }
    }

    /**
     * Finds a course by its code.
     */
    public Course findByCode(String courseCode) {
        return courseRepository.findByCode(courseCode)
                .map(courseMapper::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Course with code=%s not found", courseCode))
                );
    }
}


