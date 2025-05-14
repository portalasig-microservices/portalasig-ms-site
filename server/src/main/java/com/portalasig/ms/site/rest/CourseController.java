package com.portalasig.ms.site.rest;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.commons.rest.exception.BadRequestException;
import com.portalasig.ms.site.dto.course.Course;
import com.portalasig.ms.site.dto.course.CourseRequest;
import com.portalasig.ms.site.operations.CourseOperations;
import com.portalasig.ms.site.service.CourseService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(value = "Course Management Controller", tags = "Course Management")
@Slf4j
public class CourseController implements CourseOperations {

    private final CourseService courseService;

    @Override
    public Paginated<Course> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseService.findAll(pageable);
    }

    @Override
    public Course getCourseByCode(String courseCode) {
        return courseService.findByCode(courseCode);
    }

    @Override
    public Course upsertCourse(@Valid CourseRequest request) {
        log.info("Upserting course: {}", request.getCode());
        return courseService.upsert(request);
    }

    @Override
    public void deleteCourseByCode(String courseCode) {
        courseService.deleteCourseByCode(courseCode);
    }

    @Override
    public void importCoursesFromCsv(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            courseService.importCoursesFromCsv(file.getInputStream());
        } else {
            throw new BadRequestException("File is empty");
        }
    }
}
