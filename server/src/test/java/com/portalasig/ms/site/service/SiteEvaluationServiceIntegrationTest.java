package com.portalasig.ms.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.AbstractSiteIntegrationTest;
import com.portalasig.ms.site.constant.EvaluationType;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.evaluation.SiteEvaluationRequest;
import com.portalasig.ms.site.dto.site.Site;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SiteEvaluationServiceIntegrationTest extends AbstractSiteIntegrationTest {

  @Autowired private SiteEvaluationService siteEvaluationService;

  private static SiteEvaluationRequest baseRequest(String name) {
    return SiteEvaluationRequest.builder()
        .name(name)
        .weight(0.25f)
        .evaluationType(EvaluationType.PRACTICE)
        .startDate(LocalDate.of(2025, 4, 1))
        .endDate(LocalDate.of(2025, 4, 30))
        .build();
  }

  @Test
  void createEvaluationShouldAttachItToTheSite() {
    SiteEntity site = givenSite();

    Site result = siteEvaluationService.upsert(baseRequest("Parcial 1"), site.getSiteId());

    assertThat(result.getEvaluations())
        .anySatisfy(
            evaluation -> {
              assertThat(evaluation.getName()).isEqualTo("Parcial 1");
              assertThat(evaluation.getWeight()).isEqualTo(0.25f);
            });
  }

  @Test
  void updateEvaluationShouldModifyExistingOne() {
    SiteEntity site = givenSite();
    Site created = siteEvaluationService.upsert(baseRequest("Parcial 1"), site.getSiteId());
    Integer evaluationId = created.getEvaluations().get(0).getEvaluationId();

    SiteEvaluationRequest updateRequest = baseRequest("Parcial 1 (recuperado)");
    updateRequest.setEvaluationId(evaluationId);
    Site updated = siteEvaluationService.upsert(updateRequest, site.getSiteId());

    assertThat(updated.getEvaluations()).hasSize(1);
    assertThat(updated.getEvaluations().get(0).getName()).isEqualTo("Parcial 1 (recuperado)");
  }

  @Test
  void updateEvaluationWithUnknownIdShouldThrowNotFound() {
    SiteEntity site = givenSite();
    SiteEvaluationRequest request = baseRequest("Fantasma");
    request.setEvaluationId(999999);

    assertThatThrownBy(() -> siteEvaluationService.upsert(request, site.getSiteId()))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void createEvaluationOnUnknownSiteShouldThrowNotFound() {
    assertThatThrownBy(() -> siteEvaluationService.upsert(baseRequest("Parcial 1"), 999999))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void deleteEvaluationShouldRemoveItFromTheSite() {
    SiteEntity site = givenSite();
    Site created = siteEvaluationService.upsert(baseRequest("Parcial 1"), site.getSiteId());
    Integer evaluationId = created.getEvaluations().get(0).getEvaluationId();

    Site result = siteEvaluationService.deleteById(evaluationId, site.getSiteId());

    assertThat(result.getEvaluations()).isEmpty();
  }
}
