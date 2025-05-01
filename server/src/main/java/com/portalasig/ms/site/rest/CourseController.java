package com.portalasig.ms.site.rest;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.course.Course;
import com.portalasig.ms.site.dto.course.CourseRequest;
import com.portalasig.ms.site.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(SiteRestConstant.Course.Path.BASE)
@RequiredArgsConstructor
@Api(value = "Course Controller", tags = "Course Management")
public class CourseController {

    private final CourseService courseService;

    @ApiOperation(value = "Get all courses paginated", response = Paginated.class)
    @GetMapping
    public Paginated<Course> findAll(
            @ApiParam(value = "Pagination information", required = true) Pageable pageable
    ) {
        return courseService.findAll(pageable);
    }

    @ApiOperation(value = "Get course by code", response = Course.class)
    @GetMapping(SiteRestConstant.Course.Path.COURSE_CODE)
    public Course getCourseByCode(
            @PathVariable @ApiParam(value = "Course code", required = true) String courseCode
    ) {
        return courseService.findByCode(courseCode);
    }

    @ApiOperation(value = "Upsert a course", response = Course.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Course upserted successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('PROFESSOR', 'ADMIN')")
    public Course upsertCourse(
            @Valid @RequestBody @ApiParam(value = "Course Request", required = true) CourseRequest request
    ) {
        return courseService.upsert(request);
    }

    @ApiOperation(value = "Delete a course by course code")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Course deleted successfully"),
            @ApiResponse(code = 404, message = "Course not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(SiteRestConstant.Course.Path.COURSE_CODE)
    public void deleteCourseByCode(
            @PathVariable @ApiParam(value = "Course id", required = true) String courseCode
    ) {
        courseService.deleteCourseByCode(courseCode);
    }

    @ApiOperation(value = "Import courses from CSV")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Courses imported successfully"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(SiteRestConstant.CSV_PATH)
    public void importCoursesFromCsv(
            @ApiParam(value = "CSV file containing course data", required = true)
            @RequestParam MultipartFile file
    ) throws IOException {
        courseService.importCoursesFromCsv(file.getInputStream());
    }
}
