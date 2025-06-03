package com.portalasig.ms.site.operations;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.course.Course;
import com.portalasig.ms.site.dto.course.CourseRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.io.IOException;

/**
 * REST operations for managing courses.
 */
@HttpExchange(SiteRestConstant.Course.Path.BASE)
public interface CourseOperations {

    /**
     * Retrieves all courses with pagination.
     */
    @ApiOperation(value = "Get all courses paginated", response = Paginated.class)
    @GetExchange
    Paginated<Course> findAllCourses(
            @RequestParam(defaultValue = "0") @ApiParam(value = "Page number", example = "0") Integer page,
            @RequestParam(defaultValue = "100") @ApiParam(value = "Page size", example = "20") Integer size
    );

    /**
     * Retrieves a course by its code.
     */
    @ApiOperation(value = "Get course by code", response = Course.class)
    @GetExchange(SiteRestConstant.Course.Path.COURSE_CODE)
    Course getCourseByCode(
            @PathVariable @ApiParam(value = "Course code", required = true) String courseCode
    );

    /**
     * Creates or updates a course.
     */
    @ApiOperation(value = "Upsert a course", response = Course.class)
    @PostExchange
    Course upsertCourse(
            @Valid @RequestBody @ApiParam(value = "Course Request", required = true) CourseRequest request
    );

    /**
     * Deletes a course by its code.
     */
    @ApiOperation(value = "Delete a course by course code")
    @DeleteExchange(SiteRestConstant.Course.Path.COURSE_CODE)
    void deleteCourseByCode(
            @PathVariable @ApiParam(value = "Course code", required = true) String courseCode
    );

    /**
     * Imports courses from a CSV file.
     */
    @ApiOperation(value = "Import courses from CSV file")
    @PostExchange(SiteRestConstant.CSV_PATH)
    void importCoursesFromCsv(
            @RequestParam @ApiParam(value = "CSV file containing course data", required = true) MultipartFile file
    ) throws IOException;
}
