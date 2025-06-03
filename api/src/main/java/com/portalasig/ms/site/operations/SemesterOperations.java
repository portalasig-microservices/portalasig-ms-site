package com.portalasig.ms.site.operations;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.semester.Semester;
import com.portalasig.ms.site.dto.semester.SemesterRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * REST operations for managing academic semesters.
 */
@HttpExchange(SiteRestConstant.Semester.Path.BASE)
public interface SemesterOperations {

    /**
     * Retrieves all semesters with pagination.
     */
    @ApiOperation(value = "Get all semesters paginated", response = Paginated.class)
    @GetExchange
    Paginated<Semester> findAllSemesters(
            @RequestParam(defaultValue = "0") @ApiParam(value = "Page number", example = "0") Integer page,
            @RequestParam(defaultValue = "20") @ApiParam(value = "Page size", example = "20") Integer size
    );

    /**
     * Creates or updates a semester.
     */
    @ApiOperation(value = "Upsert a semester", response = Semester.class)
    @PostExchange
    Semester upsertSemester(
            @RequestBody @ApiParam(value = "Semester Request", required = true) SemesterRequest request
    );

    /**
     * Deletes a semester by its ID.
     */
    @ApiOperation(value = "Delete a semester by ID")
    @DeleteExchange(SiteRestConstant.Semester.Path.SEMESTER_ID)
    void deleteSemester(
            @PathVariable @ApiParam(value = "Semester ID", required = true) Integer semesterId
    );

    /**
     * Returns the active semester.
     */
    @ApiOperation(value = "Get active semester", response = Semester.class)
    @GetExchange(SiteRestConstant.Semester.Path.ACTIVE)
    Semester getActiveSemester();

    /**
     * Returns a list of suggested semesters based on year limit.
     */
    @ApiOperation(value = "Get suggested semesters", response = List.class)
    @GetExchange(SiteRestConstant.Semester.Path.SUGGESTED)
    List<Semester> getSuggestedSemesters(
            @RequestParam(value = "year_limit", required = false, defaultValue = "2")
            @ApiParam(value = "Limit of past years to suggest", example = "2") int yearLimit
    );
}
