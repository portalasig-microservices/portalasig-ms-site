package com.portalasig.ms.site.rest;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.site.dto.semester.Semester;
import com.portalasig.ms.site.dto.semester.SemesterRequest;
import com.portalasig.ms.site.operations.SemesterOperations;
import com.portalasig.ms.site.service.SemesterService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Semester Management Controller", tags = "Semester Management")
@Slf4j
public class SemesterController implements SemesterOperations {

    private final SemesterService semesterService;

    @Override
    public Paginated<Semester> findAllSemesters(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return semesterService.findAll(pageable);
    }

    @Override
    public Semester upsertSemester(@Valid SemesterRequest request) {
        log.info("Upserting semester: {}", request);
        return semesterService.upsert(request);
    }

    @Override
    public void deleteSemester(Integer semesterId) {
        semesterService.delete(semesterId);
    }

    @Override
    public Semester getActiveSemester() {
        return semesterService.getActiveSemester();
    }

    @Override
    public List<Semester> getSuggestedSemesters(int yearLimit) {
        return semesterService.getSuggestedSemesters(yearLimit);
    }
}
