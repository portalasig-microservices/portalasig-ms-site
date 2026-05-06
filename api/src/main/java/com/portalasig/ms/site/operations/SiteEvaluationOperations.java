package com.portalasig.ms.site.operations;

import com.portalasig.ms.site.constant.SiteRestConstant;
import com.portalasig.ms.site.dto.evaluation.SiteEvaluationRequest;
import com.portalasig.ms.site.dto.site.Site;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Defines the REST operations for site evaluations.
 */
@HttpExchange(SiteRestConstant.SiteEvaluation.Path.BASE)
public interface SiteEvaluationOperations {

    /**
     * Creates or updates a site evaluation.
     */
    @ApiOperation(value = "Upsert site evaluation", response = Site.class)
    @PostExchange
    Site upsertEvaluation(
            @RequestBody @ApiParam(value = "Evaluation request", required = true) SiteEvaluationRequest request,
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId
    );

    /**
     * Deletes a site evaluation by ID.
     */
    @ApiOperation(value = "Delete evaluation", response = Site.class)
    @DeleteExchange(SiteRestConstant.SiteEvaluation.Path.ELEMENT)
    Site deleteEvaluationById(
            @PathVariable @ApiParam(value = "evaluation id", required = true) Integer evaluationId,
            @PathVariable @ApiParam(value = "Site id", required = true) Integer siteId
    );
}
