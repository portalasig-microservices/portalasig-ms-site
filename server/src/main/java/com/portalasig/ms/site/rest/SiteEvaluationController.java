package com.portalasig.ms.site.rest;


import com.portalasig.ms.site.dto.evaluation.SiteEvaluationRequest;
import com.portalasig.ms.site.dto.site.Site;
import com.portalasig.ms.site.operations.SiteEvaluationOperations;
import com.portalasig.ms.site.service.SiteEvaluationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(value = "Site evaluation management controller", tags = "Site evaluation")
public class SiteEvaluationController implements SiteEvaluationOperations {

    private final SiteEvaluationService evaluationService;

    @Override
    public Site upsertEvaluation(SiteEvaluationRequest request, Integer siteId) {
        return evaluationService.upsert(request, siteId);
    }

    @Override
    public Site deleteEvaluationById(Integer evaluationId, Integer siteId) {
        return evaluationService.deleteById(evaluationId, siteId);
    }
}
