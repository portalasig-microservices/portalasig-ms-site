package com.portalasig.ms.site.rest;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.semester.Semester;
import com.portalasig.ms.site.dto.semester.SemesterRequest;
import com.portalasig.ms.site.service.SemesterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(SiteRestConstant.Semester.Path.BASE)
@RequiredArgsConstructor
@Api(value = "Semester Controller", tags = "Semester Management")
public class SemesterController {

    private final SemesterService semesterService;

    @ApiOperation(value = "Get all semester paginated", response = Paginated.class)
    @GetMapping
    public Paginated<Semester> findAll(
            @ApiParam(value = "Pagination information", required = true) Pageable pageable
    ) {
        return semesterService.findAll(pageable);
    }

    @ApiOperation(value = "Upsert a semester", response = Semester.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Semester upserted successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    public Semester upsertSemester(
            @Valid @RequestBody @ApiParam(value = "Semester Request", required = true) SemesterRequest request
    ) {
        return semesterService.upsert(request);
    }

    @ApiOperation(value = "Delete a semester")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Semester deleted successfully"),
            @ApiResponse(code = 404, message = "Semester not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(SiteRestConstant.Semester.Path.SEMESTER_ID)
    public void deleteSemester(
            @PathVariable @ApiParam(value = "Semester id", required = true) Integer semesterId
    ) {
        semesterService.delete(semesterId);
    }

    @ApiOperation(value = "Find semester by academic period", response = Semester.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Semester found"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(SiteRestConstant.Semester.Path.ACADEMIC_PERIOD)
    public Semester findSemesterByAcademicPeriod(
            @PathVariable @ApiParam(value = "Academic Period", required = true) String academicPeriod
    ) {
        return semesterService.findByAcademicPeriod(academicPeriod);
    }

    @ApiOperation(value = "Get active semester", response = Semester.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Semester found"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(SiteRestConstant.Semester.Path.ACTIVE)
    public Semester getActiveSemester() {
        return semesterService.getActiveSemester();
    }

    @GetMapping(SiteRestConstant.Semester.Path.SUGGESTED)
    public List<Semester> getSuggestedSemesters(
            @RequestParam(value = "year_limit", required = false, defaultValue = "2")
            @ApiParam(value = "year_limit") int yearLimit
    ) {
        return semesterService.getSuggestedSemesters(yearLimit);
    }
}
