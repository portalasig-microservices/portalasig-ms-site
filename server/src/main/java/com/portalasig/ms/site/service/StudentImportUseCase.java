package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.BadRequestException;
import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.commons.rest.exception.SystemErrorException;
import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SiteSectionEntity;
import com.portalasig.ms.site.domain.entity.site.SiteStudentEntity;
import com.portalasig.ms.site.dto.site.SiteSectionRequest;
import com.portalasig.ms.site.mapper.SiteStudentMapper;
import com.portalasig.ms.site.repository.SiteRepository;
import com.portalasig.ms.uaa.dto.RegisterRequest;
import com.portalasig.ms.uaa.dto.User;
import com.portalasig.ms.uaa.operation.UserOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Student import use case service class.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentImportUseCase {

    @Value("${site.tools.students.import.input-header}")
    private final List<String> expectedHeader;

    @Value("${site.tools.students.import.email-blacklist}")
    private final List<String> emailBlacklist;

    private static final int HEADER_ROW_INDEX = 10;
    private static final int INITIAL_DATA_ROW_INDEX = 11;
    private static final int SECTION_CODE_ROW_INDEX = 8;
    private static final int SECTION_CODE_COLUMN_INDEX = 1;

    private final DataFormatter dataFormatter = new DataFormatter();
    @Qualifier("clientCredentialsUserClientV1")
    private final UserOperations userOperations;

    private final SiteStudentMapper siteStudentMapper;
    private final SiteRepository siteRepository;
    private final SiteSectionService siteSectionService;

    /**
     * Upsert students from a csv. if they aren't registered they will also be.
     *
     * @param siteId site id
     * @param file   Excel file from CONEST containing the student list
     */
    public void upsertStudentsFromExcel(Integer siteId, MultipartFile file) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        if (file.isEmpty()) {
            throw new BadRequestException("File is empty. Skipping process.");
        }
        log.info("Starting upsert of students from csv");
        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Sheet sheet = workbook.getSheetAt(0);
            validateHeader(sheet);

            String sectionCode = getSectionCodeFromSheet(sheet);
            SiteSectionEntity sectionEntity = createOrFindSection(siteEntity, sectionCode);

            List<RegisterRequest> students = getStudentsToAdd(sheet);
            // This method register or simply the return the existing user
            List<User> users = userOperations.bulkRegister(students);

            List<SiteStudentEntity> studentEntities = users
                    .stream()
                    .map(siteStudentMapper::toEntityFromUser)
                    .toList();
            studentEntities.forEach(studentEntity -> {
                studentEntity.setSection(sectionEntity);
                studentEntity.setPartyRole(PartyRole.STUDENT);
            });
            sectionEntity.getStudents().addAll(studentEntities);

            siteRepository.save(siteEntity);
            stopWatch.stop();
            log.info(
                    "Finished upsert of {} students in {} ms",
                    students.size(),
                    stopWatch.getTotalTimeMillis()
            );

        } catch (IOException e) {
            throw new SystemErrorException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Something went wrong while parsing csv file",
                    e
            );
        }
    }

    private List<RegisterRequest> getStudentsToAdd(Sheet sheet) {
        List<RegisterRequest> students = new ArrayList<>();
        int lastRowNum = sheet.getLastRowNum();
        for (int i = INITIAL_DATA_ROW_INDEX; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) {
                continue;
            }

            RegisterRequest student = toUserRequestFromRow(row);
            students.add(student);
        }
        return students;
    }

    private SiteSectionEntity createOrFindSection(SiteEntity siteEntity, String sectionCode) {
        SiteSectionEntity sectionEntity = siteEntity
                .getSections()
                .stream()
                .filter(section -> section.getCode().equals(sectionCode))
                .findFirst()
                .orElse(null);
        if (sectionEntity == null) {
            SiteSectionRequest siteSectionRequest = SiteSectionRequest
                    .builder()
                    .code(sectionCode)
                    .build();
            siteSectionService.createSection(siteEntity, siteSectionRequest);
            log.info("Creating site section with section_code={}", sectionCode);
            siteEntity = siteRepository.save(siteEntity);
            sectionEntity = siteEntity
                    .getSections()
                    .stream()
                    .filter(section -> section.getCode().equals(sectionCode))
                    .findFirst()
                    .orElse(null);
        }
        return sectionEntity;
    }

    private String getSectionCodeFromSheet(Sheet sheet) {
        Row row = sheet.getRow(SECTION_CODE_ROW_INDEX);
        if (row == null) {
            throw new BadRequestException("Row for section code is invalid. Skipping process.");
        }

        Cell cell = row.getCell(SECTION_CODE_COLUMN_INDEX, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        String rawValue = castCellToType(cell, String.class);
        if (rawValue == null || rawValue.isBlank()) {
            throw new BadRequestException("Section code is missing. Skipping process.");
        }
        rawValue = rawValue.trim();

        int spaceIndex = rawValue.indexOf(' ');
        if (spaceIndex > 0) {
            return rawValue.substring(0, spaceIndex).trim().toUpperCase();
        }

        return rawValue.toUpperCase();
    }

    private boolean isRowEmpty(Row row) {
        short lastCellNum = row.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            String value = castCellToType(cell, String.class);
            if (!value.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    private void validateHeader(Sheet sheet) {
        Row headerRow = sheet.getRow(HEADER_ROW_INDEX);
        if (headerRow == null) {
            throw new BadRequestException("Header row invalid. Skipping process.");
        }

        Set<String> header = new HashSet<>(getRowValuesAsString(headerRow));
        header = header.stream()
                .map(h -> {
                    h = h.trim().toUpperCase();
                    String normalized = Normalizer.normalize(h, Normalizer.Form.NFD);
                    return normalized.replaceAll("\\p{M}", "");
                })
                .collect(Collectors.toSet());
        if (!header.containsAll(expectedHeader)) {
            throw new BadRequestException("Invalid csv header. Skipping process.");
        }
    }

    private List<String> getRowValuesAsString(Row row) {
        List<String> values = new ArrayList<>();

        for (Cell cell : row) {
            String value = castCellToType(cell, String.class);
            values.add(value != null ? value.trim() : "");
        }

        return values;
    }

    @SuppressWarnings("unchecked")
    private <T> T castCellToType(Cell cell, Class<T> clazz) {
        String value = dataFormatter.formatCellValue(cell);
        value = value != null ? value.trim() : "";

        if (clazz.equals(String.class)) {
            return (T) value;
        }
        if (clazz.equals(Long.class)) {
            return (T) Long.valueOf(value);
        }

        throw new IllegalArgumentException("Unsupported type: " + clazz);
    }

    private RegisterRequest toUserRequestFromRow(Row row) {
        Long identity = castCellToType(
                row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL),
                Long.class
        );

        String firstName = castCellToType(
                row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL),
                String.class
        );

        String lastName = castCellToType(
                row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL),
                String.class
        );
        String email = castCellToType(
                row.getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL),
                String.class
        );

        email = validateEmail(email);

        return RegisterRequest
                .builder()
                .identity(identity)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

    private String validateEmail(String email) {
        return !emailBlacklist.contains(email) ? email : null;
    }
}
