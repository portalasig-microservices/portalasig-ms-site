package com.portalasig.ms.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.AbstractSiteIntegrationTest;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.dto.site.SiteSection;
import com.portalasig.ms.site.dto.site.SiteSectionRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SiteSectionServiceIntegrationTest extends AbstractSiteIntegrationTest {

  @Autowired private SiteSectionService siteSectionService;

  @Test
  void upsertSectionShouldCreateSectionInSite() {
    SiteEntity site = givenSite();

    Site result =
        siteSectionService.upsertSection(
            site.getSiteId(), SiteSectionRequest.builder().code("01").build());

    assertThat(result.getSections())
        .anySatisfy(section -> assertThat(section.getCode()).isEqualTo("01"));
  }

  @Test
  void upsertSectionWithExistingIdShouldUpdateIt() {
    SiteEntity site = givenSite();
    Site created =
        siteSectionService.upsertSection(
            site.getSiteId(), SiteSectionRequest.builder().code("01").build());
    Integer sectionId = created.getSections().get(0).getSectionId();

    Site updated =
        siteSectionService.upsertSection(
            site.getSiteId(), SiteSectionRequest.builder().sectionId(sectionId).code("02").build());

    assertThat(updated.getSections())
        .anySatisfy(section -> assertThat(section.getCode()).isEqualTo("02"));
  }

  @Test
  void deleteSectionShouldRemoveItFromSite() {
    SiteEntity site = givenSite();
    Site created =
        siteSectionService.upsertSection(
            site.getSiteId(), SiteSectionRequest.builder().code("01").build());
    Integer sectionId = created.getSections().get(0).getSectionId();

    Site result = siteSectionService.deleteSection(site.getSiteId(), sectionId);

    assertThat(result.getSections()).isEmpty();
  }

  @Test
  void upsertSectionWithUnknownSiteShouldThrowNotFound() {
    assertThatThrownBy(
            () -> siteSectionService.upsertSection(999999, SiteSectionRequest.builder().build()))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void searchSectionByCodeShouldReturnMatches() {
    SiteEntity site = givenSite();
    siteSectionService.upsertSection(site.getSiteId(), SiteSectionRequest.builder().code("01").build());

    List<SiteSection> sections = siteSectionService.searchSectionByCode(site.getSiteId(), "01");

    assertThat(sections).hasSize(1);
    assertThat(sections.get(0).getCode()).isEqualTo("01");
  }
}
