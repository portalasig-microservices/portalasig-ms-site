package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.dto.Paginated;
import com.portalasig.ms.commons.rest.exception.BadRequestException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.constant.AcademicPeriodType;
import com.portalasig.ms.site.domain.entity.SemesterEntity;
import com.portalasig.ms.site.dto.semester.Semester;
import com.portalasig.ms.site.dto.semester.SemesterRequest;
import com.portalasig.ms.site.mapper.SemesterMapper;
import com.portalasig.ms.site.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SemesterService {

    private final SemesterRepository semesterRepository;

    private final SemesterMapper semesterMapper;

    @Value("${site.semester.default-creation-status:false}")
    private Boolean isSemesterActiveByDefault;


    public Paginated<Semester> findAll(Pageable pageable) {
        Page<SemesterEntity> semesters = semesterRepository.findAll(pageable);
        if (semesters.isEmpty()) {
            throw new ResourceNotFoundException("No semesters found");
        }
        return Paginated.wrap(semesters.map(semesterMapper::toDto));
    }

    public Semester upsert(SemesterRequest request) {
        SemesterEntity semester;
        if (request.getSemesterId() == null) {
            semester = semesterMapper.toEntity(request);
            semester.setIsActive(isSemesterActiveByDefault);
            semesterRepository.getActiveSemester().ifPresent(
                    activeSemester -> {
                        activeSemester.setIsActive(false);
                        semesterRepository.save(activeSemester);
                        log.info("Semester semester_id={} has been deactivated", activeSemester.getSemesterId());
                    }
            );
        } else {
            semester = semesterRepository.findById(request.getSemesterId()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            String.format("Semester with semester_id=%s not found", request.getSemesterId())
                    )
            );
            semesterMapper.toEntityFromExisting(request, semester);
        }
        semester = semesterRepository.save(semester);
        log.info("Semester with semester_id={} has been upserted and activated", semester.getSemesterId());
        return semesterMapper.toDto(semester);
    }

    public void delete(Integer semesterId) {
        semesterRepository.findById(semesterId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Semester with semester_id=%s not found", semesterId))
        );
        try {
            semesterRepository.deleteById(semesterId);
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    String.format("Error deleting semester with semester_id=%s", semesterId)
            );
        }
    }

    public Semester findByAcademicPeriod(String academicPeriodString) {
        Pair<AcademicPeriodType, Integer> academicPeriod = toAcademicPeriod(academicPeriodString);
        SemesterEntity entity = semesterRepository.findByAcademicPeriod(
                academicPeriod.getFirst().getCode(),
                academicPeriod.getSecond()
        ).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Semester with academic_period=%s not found", academicPeriod)
                )
        );
        return semesterMapper.toDto(entity);
    }

    /**
     * Converts a string label in the format <type>-<year> into a pair of AcademicPeriodType and year.
     *
     * @param label the academic period label in the format type-year
     * @return a Pair containing the AcademicPeriodType and the year
     * @throws BadRequestException if the label format is invalid or contains invalid values
     */
    public static Pair<AcademicPeriodType, Integer> toAcademicPeriod(String label) {
        if (label == null || !label.contains("-")) {
            throw new BadRequestException("Invalid label format, expected <type>-<year>");
        }
        String[] parts = label.split("-");
        if (parts.length != 2) {
            throw new BadRequestException("Invalid label format, expected <type>-<year>");
        }

        int periodYear;
        try {
            periodYear = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid year format, expected numeric value");
        }

        AcademicPeriodType periodType = AcademicPeriodType.fromCode(parts[0]);
        if (AcademicPeriodType.INVALID.equals(periodType)) {
            throw new BadRequestException("Invalid academic period type");
        }

        return Pair.of(periodType, periodYear);
    }

    public Semester getActiveSemester() {
        SemesterEntity entity = semesterRepository.getActiveSemester().orElseThrow(
                () -> new ResourceNotFoundException("No active semester found")
        );
        return semesterMapper.toDto(entity);
    }

    public List<Semester> getSuggestedSemesters(int yearLimit) {
        int currentYear = java.time.LocalDate.now().getYear();
        int suggestedYear = currentYear + yearLimit;
        List<SemesterEntity> semesters = semesterRepository.findSuggestedSemesters(currentYear, suggestedYear)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("No semesters found within year limit of %d", yearLimit)
                ));
        return semesters.stream().map(semesterMapper::toDto).toList();
    }
}
