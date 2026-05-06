package com.portalasig.ms.site.service;

import com.portalasig.ms.commons.rest.exception.ResourceNotFoundException;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.domain.entity.site.SiteEvaluationEntity;
import com.portalasig.ms.site.dto.evaluation.SiteEvaluationRequest;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.mapper.SiteEvaluationMapper;
import com.portalasig.ms.site.mapper.SiteMapper;
import com.portalasig.ms.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;

/**
 * Service for managing site evaluations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SiteEvaluationService {

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;
    private final SiteEvaluationMapper evaluationMapper;

    /**
     * Creates or updates a site evaluation.
     *
     * @param request the evaluation request
     * @param siteId  the site id
     * @return the updated site
     */
    public Site upsert(SiteEvaluationRequest request, Integer siteId) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        if (request.getEvaluationId() == null) {
            createEvaluation(siteEntity, request);
        } else {
            updateEvaluation(siteEntity, request);
        }

        siteEntity = siteRepository.save(siteEntity);
        log.info("Site evaluation has been upserted in site_id={}", siteId);
        return siteMapper.toDto(siteEntity);
    }

    private void createEvaluation(SiteEntity siteEntity, SiteEvaluationRequest request) {
        SiteEvaluationEntity evaluationEntity = evaluationMapper.toEntityFromRequest(request);
        evaluationEntity.setSite(siteEntity);

        if (siteEntity.getEvaluations() == null) {
            siteEntity.setEvaluations(new HashSet<>());
        }

        siteEntity.getEvaluations().add(evaluationEntity);
    }

    private void updateEvaluation(SiteEntity siteEntity, SiteEvaluationRequest request) {
        SiteEvaluationEntity existingEvaluation = siteEntity
                .getEvaluations()
                .stream()
                .filter(evaluation -> Objects.equals(
                        evaluation.getEvaluationId(), request.getEvaluationId()
                ))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("site_evaluation_id=%d to edit not found", request.getEvaluationId()))
                );

        evaluationMapper.toEntityFromExisting(existingEvaluation, request);
    }

    /**
     * Deletes a site evaluation by ID.
     *
     * @param evaluationId the evaluation id
     * @param siteId       the site id
     * @return the updated site
     */
    public Site deleteById(Integer evaluationId, Integer siteId) {
        SiteEntity siteEntity = siteRepository.findById(siteId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Site with site_id=%d not found", siteId))
        );

        SiteEvaluationEntity evaluation = siteEntity
                .getEvaluations()
                .stream()
                .filter(eval -> eval.getEvaluationId().equals(evaluationId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("site_evaluation_id=%d not found", evaluationId)
                ));

        siteEntity.getEvaluations().remove(evaluation);
        siteRepository.save(siteEntity);

        log.info("site_evaluation_id={} deleted from site_id={}", evaluation, siteId);
        return siteMapper.toDto(siteEntity);
    }
}
