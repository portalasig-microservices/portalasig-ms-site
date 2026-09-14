package com.portalasig.ms.site.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.portalasig.ms.site.AbstractSiteIntegrationTest;
import com.portalasig.ms.site.constant.ReferenceType;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.ReferenceRequest;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteCourseTopicRequest;
import com.portalasig.ms.site.dto.site.SiteObjectiveRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SiteContentServiceIntegrationTest extends AbstractSiteIntegrationTest {

  @Autowired private SiteObjectiveService siteObjectiveService;

  @Autowired private SiteReferenceService siteReferenceService;

  @Autowired private SiteCourseTopicService siteCourseTopicService;

  @Test
  void upsertObjectiveShouldAttachItToTheSite() {
    SiteEntity site = givenSite();
    SiteObjectiveRequest request =
        SiteObjectiveRequest.builder()
            .title("Comprender los limites")
            .description("El estudiante comprende el concepto de limite")
            .priority(1)
            .build();

    Site result =
        siteObjectiveService.upsertObjective(request, PERIOD_TYPE, PERIOD_YEAR, COURSE_CODE);

    assertThat(result.getObjectives())
        .anySatisfy(
            objective ->
                assertThat(objective.getDescription())
                    .isEqualTo("El estudiante comprende el concepto de limite"));
  }

  @Test
  void deleteObjectiveShouldRemoveIt() {
    SiteEntity site = givenSite();
    Site created =
        siteObjectiveService.upsertObjective(
            SiteObjectiveRequest.builder().title("Objetivo").description("Desc").build(),
            PERIOD_TYPE,
            PERIOD_YEAR,
            COURSE_CODE);
    Integer objectiveId = created.getObjectives().get(0).getCourseObjectiveId();

    Site result =
        siteObjectiveService.deleteObjective(objectiveId, PERIOD_TYPE, PERIOD_YEAR, COURSE_CODE);

    assertThat(result.getObjectives()).isEmpty();
  }

  @Test
  void upsertReferenceShouldAttachItToTheSite() {
    SiteEntity site = givenSite();
    ReferenceRequest request =
        ReferenceRequest.builder()
            .title("Calculo de Stewart")
            .author("James Stewart")
            .referenceType(ReferenceType.BOOK)
            .url("https://example.com/stewart")
            .isRequired(true)
            .build();

    Site result = siteReferenceService.upsertReference(request, PERIOD_TYPE, PERIOD_YEAR, COURSE_CODE);

    assertThat(result.getReferences())
        .anySatisfy(reference -> assertThat(reference.getTitle()).isEqualTo("Calculo de Stewart"));
  }

  @Test
  void deleteReferenceShouldRemoveIt() {
    SiteEntity site = givenSite();
    Site created =
        siteReferenceService.upsertReference(
            ReferenceRequest.builder()
                .title("Ref")
                .referenceType(ReferenceType.ONLINE)
                .url("https://example.com")
                .build(),
            PERIOD_TYPE,
            PERIOD_YEAR,
            COURSE_CODE);
    Integer referenceId = created.getReferences().get(0).getReferenceId();

    Site result =
        siteReferenceService.deleteReference(referenceId, PERIOD_TYPE, PERIOD_YEAR, COURSE_CODE);

    assertThat(result.getReferences()).isEmpty();
  }

  @Test
  void upsertReferenceShouldUpdateExistingFields() {
    SiteEntity site = givenSite();
    Site created =
        siteReferenceService.upsertReference(
            ReferenceRequest.builder()
                .title("Calculo de Stewart")
                .author("James Stewart")
                .description("Septima edicion")
                .referenceType(ReferenceType.BOOK)
                .isRequired(true)
                .build(),
            PERIOD_TYPE,
            PERIOD_YEAR,
            COURSE_CODE);
    Integer referenceId = created.getReferences().get(0).getReferenceId();

    // Partial update: null fields must be ignored by the mapper, non-null ones applied
    Site result =
        siteReferenceService.upsertReference(
            ReferenceRequest.builder()
                .referenceId(referenceId)
                .title("Calculo de Stewart, 8va edicion")
                .referenceType(ReferenceType.BOOK)
                .priority(5)
                .build(),
            PERIOD_TYPE,
            PERIOD_YEAR,
            COURSE_CODE);

    assertThat(result.getReferences()).hasSize(1);
    var updated = result.getReferences().get(0);
    assertThat(updated.getTitle()).isEqualTo("Calculo de Stewart, 8va edicion");
    assertThat(updated.getAuthor()).isEqualTo("James Stewart");
    assertThat(updated.getPriority()).isEqualTo(5);
  }

  @Test
  void upsertObjectiveShouldUpdateExistingFields() {
    SiteEntity site = givenSite();
    Site created =
        siteObjectiveService.upsertObjective(
            SiteObjectiveRequest.builder()
                .title("Objetivo 1")
                .description("Desc original")
                .priority(1)
                .build(),
            PERIOD_TYPE,
            PERIOD_YEAR,
            COURSE_CODE);
    Integer objectiveId = created.getObjectives().get(0).getCourseObjectiveId();

    Site result =
        siteObjectiveService.upsertObjective(
            SiteObjectiveRequest.builder()
                .courseObjectiveId(objectiveId)
                .description("Desc actualizada")
                .build(),
            PERIOD_TYPE,
            PERIOD_YEAR,
            COURSE_CODE);

    assertThat(result.getObjectives()).hasSize(1);
    assertThat(result.getObjectives().get(0).getDescription()).isEqualTo("Desc actualizada");
  }

  @Test
  void upsertCourseTopicShouldAttachItToTheSite() {
    SiteEntity site = givenSite();
    SiteCourseTopicRequest request =
        SiteCourseTopicRequest.builder().title("Limites y continuidad").description("Unidad 1").build();

    Site result = siteCourseTopicService.upsertCourseTopic(request, site.getSiteId());

    assertThat(result.getCourseTopics())
        .anySatisfy(topic -> assertThat(topic.getTitle()).isEqualTo("Limites y continuidad"));
  }

  @Test
  void deleteCourseTopicShouldRemoveIt() {
    SiteEntity site = givenSite();
    Site created =
        siteCourseTopicService.upsertCourseTopic(
            SiteCourseTopicRequest.builder().title("Tema").description("Desc").build(),
            site.getSiteId());
    Integer topicId = created.getCourseTopics().get(0).getSiteCourseTopicId();

    Site result = siteCourseTopicService.deleteCourseTopic(topicId, site.getSiteId());

    assertThat(result.getCourseTopics()).isEmpty();
  }
}
