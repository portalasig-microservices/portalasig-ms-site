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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SiteRestConstant.Course.BASE_PATH)
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

    @ApiOperation(value = "Upsert a course", response = Course.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Course upserted successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    public Course upsertCourse(
            @RequestBody @ApiParam(value = "Course Request", required = true) CourseRequest request
    ) {
        return courseService.upsert(request);
    }

    @ApiOperation(value = "Delete a course")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Course deleted successfully"),
            @ApiResponse(code = 404, message = "Course not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(SiteRestConstant.Course.COURSE_ID_PATH)
    public void deleteCourse(
            @PathVariable @ApiParam(value = "Course id", required = true) Integer courseId
    ) {
        courseService.delete(courseId);
    }

}