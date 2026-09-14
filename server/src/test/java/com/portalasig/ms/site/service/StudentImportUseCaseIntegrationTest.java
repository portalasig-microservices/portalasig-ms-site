package com.portalasig.ms.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.portalasig.ms.commons.rest.exception.BadRequestException;
import com.portalasig.ms.site.AbstractSiteIntegrationTest;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.uaa.dto.RegisterRequest;
import com.portalasig.ms.uaa.dto.User;
import com.portalasig.ms.uaa.operation.UserOperations;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

class StudentImportUseCaseIntegrationTest extends AbstractSiteIntegrationTest {

  private static final int SECTION_ROW = 8;
  private static final int HEADER_ROW = 10;
  private static final int FIRST_DATA_ROW = 11;

  @Autowired private StudentImportUseCase studentImportUseCase;

  @MockBean(name = "clientCredentialsUserClientV1")
  private UserOperations userOperations;

  private byte[] buildConestExcel(
      String sectionCode, List<String> header, List<Object[]> students) throws IOException {
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("CONEST");
      if (sectionCode != null) {
        Row sectionRow = sheet.createRow(SECTION_ROW);
        sectionRow.createCell(1).setCellValue(sectionCode);
      }
      if (header != null) {
        Row headerRow = sheet.createRow(HEADER_ROW);
        for (int i = 0; i < header.size(); i++) {
          headerRow.createCell(i).setCellValue(header.get(i));
        }
      }
      for (int i = 0; i < students.size(); i++) {
        Object[] student = students.get(i);
        Row row = sheet.createRow(FIRST_DATA_ROW + i);
        row.createCell(2).setCellValue((Double) student[0]);
        row.createCell(3).setCellValue((String) student[1]);
        row.createCell(4).setCellValue((String) student[2]);
        row.createCell(5).setCellValue((String) student[3]);
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      workbook.write(out);
      return out.toByteArray();
    }
  }

  private MockMultipartFile excelFile(byte[] content) {
    return new MockMultipartFile("file", "estudiantes.xlsx", null, content);
  }

  private void mockBulkRegister() {
    when(userOperations.bulkRegister(anyList()))
        .thenAnswer(
            invocation -> {
              List<RegisterRequest> requests = invocation.getArgument(0);
              return requests.stream()
                  .map(
                      request ->
                          User.builder()
                              .identity(request.getIdentity())
                              .firstName(request.getFirstName())
                              .lastName(request.getLastName())
                              .email(request.getEmail())
                              .build())
                  .toList();
            });
  }

  @Test
  void importStudentsShouldCreateSectionAndStudents() throws IOException {
    SiteEntity site = givenSite();
    mockBulkRegister();
    byte[] excel =
        buildConestExcel(
            "01 SECCION TEORIA",
            List.of("NRO", "LICENCIATURA", "CEDULA", "NOMBRES", "APELLIDOS", "CORREO", "ESTADO"),
            List.of(
                new Object[] {30222333.0, "Ana", "Pérez", "ana@portalasig.ucv.ve"},
                new Object[] {30222334.0, "Luis", "Gómez", "luis@portalasig.ucv.ve"}));

    studentImportUseCase.upsertStudentsFromExcel(site.getSiteId(), excelFile(excel));

    SiteEntity reloaded = siteRepository.findById(site.getSiteId()).orElseThrow();
    assertThat(reloaded.getSections()).hasSize(1);
    assertThat(reloaded.getSections().iterator().next().getCode()).isEqualTo("01");
    assertThat(reloaded.getSections().iterator().next().getStudents())
        .anySatisfy(student -> assertThat(student.getIdentity()).isEqualTo(30222333L))
        .anySatisfy(student -> assertThat(student.getIdentity()).isEqualTo(30222334L));
  }

  @Test
  void importStudentsShouldNullifyBlacklistedEmails() throws IOException {
    SiteEntity site = givenSite();
    final List<RegisterRequest>[] captured = new List[1];
    when(userOperations.bulkRegister(anyList()))
        .thenAnswer(
            invocation -> {
              captured[0] = invocation.getArgument(0);
              return List.of(
                  User.builder()
                      .identity(30222335L)
                      .firstName("Bloqueado")
                      .lastName("Correo")
                      .build());
            });
    byte[] excel =
        buildConestExcel(
            "01",
            List.of("NRO", "LICENCIATURA", "CEDULA", "NOMBRES", "APELLIDOS", "CORREO", "ESTADO"),
            List.<Object[]>of(
                new Object[] {30222335.0, "Bloqueado", "Correo", "bloqueado@portalasig.ucv.ve"}));

    studentImportUseCase.upsertStudentsFromExcel(site.getSiteId(), excelFile(excel));

    assertThat(captured[0]).hasSize(1);
    assertThat(captured[0].get(0).getEmail()).isNull();
  }

  @Test
  void importEmptyFileShouldFail() {
    SiteEntity site = givenSite();

    assertThatThrownBy(
            () ->
                studentImportUseCase.upsertStudentsFromExcel(
                    site.getSiteId(), excelFile(new byte[0])))
        .isInstanceOf(BadRequestException.class);
  }

  @Test
  void importWithInvalidHeaderShouldFail() throws IOException {
    SiteEntity site = givenSite();
    byte[] excel = buildConestExcel("01", List.of("FOO", "BAR"), List.of());

    assertThatThrownBy(
            () -> studentImportUseCase.upsertStudentsFromExcel(site.getSiteId(), excelFile(excel)))
        .isInstanceOf(BadRequestException.class);
  }

  @Test
  void importWithMissingSectionCodeShouldFail() throws IOException {
    SiteEntity site = givenSite();
    byte[] excel =
        buildConestExcel(null, List.of("NRO", "LICENCIATURA", "CEDULA", "NOMBRES", "APELLIDOS", "CORREO", "ESTADO"), List.of());

    assertThatThrownBy(
            () -> studentImportUseCase.upsertStudentsFromExcel(site.getSiteId(), excelFile(excel)))
        .isInstanceOf(BadRequestException.class);
  }
}
