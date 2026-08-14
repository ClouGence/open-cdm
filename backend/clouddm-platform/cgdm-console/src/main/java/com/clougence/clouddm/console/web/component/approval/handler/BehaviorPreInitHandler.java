/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.component.approval.handler;

import java.io.Reader;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.analysis.*;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBehavior;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;

import jakarta.annotation.Resource;

/**
 * Collects approval behaviors one query request at a time.
 *
 * @author clougence
 */
@Service
public class BehaviorPreInitHandler extends AbstractPreInitHandler {

    @Resource
    private ApprovalService      approvalService;
    @Resource
    private QueryAnalysisService queryAnalysisService;

    @Override
    protected String analysisType() {
        return ApprovalAnalysisStateMO.TYPE_BEHAVIOR_ANALYSIS;
    }

    @Override
    public int displayOrder() {
        return 1;
    }

    @Override
    protected void doHandle(PreInitContext context) {
        DmApprovalDO approvalDO = context.getApproval();
        Map<String, ApprovalBehavior> behaviors = new LinkedHashMap<>();
        AtomicLong sqlCounter = new AtomicLong();
        AtomicLong behaviorCounter = new AtomicLong();
        context.writeResult(state -> {
            state.setTotalCount(sqlCounter.get());
            state.setBehaviorCount(behaviorCounter.get());
            state.setBehaviors(new ArrayList<>(behaviors.values()));
        });
        AnalysisQueryOptions options = AnalysisQueryOptions.builder()
            .currentUid(approvalDO.getOwnerUid())
            .dataSourceId(approvalDO.getBindDsId())
            .levels(context.getDsLevels().levelsParam())
            .skip(QueryAnalysisFeature.REWRITE, QueryAnalysisFeature.LINEAGE, QueryAnalysisFeature.MASKING)
            .build();

        this.approvalService.consumeSqlFile(approvalDO.getId(), sql -> {
            try (Reader reader = context.openReader(sql);
                    Stream<QueryRequest> requests = this.queryAnalysisService.analysisRequestsStream(context.getDsConfig(), reader, Collections.emptyList(), 1, 0, options)) {
                requests.forEachOrdered(request -> {
                    behaviorCounter.addAndGet(this.analyzeRequest(request, behaviors));
                    sqlCounter.incrementAndGet();
                    context.itemProcessed(request.getQueryBody());
                });
                return null;
            } catch (AntlerSyntaxException e) {
                throw this.sqlAnalysisError(e);
            }
        });
    }

    private long analyzeRequest(QueryRequest request, Map<String, ApprovalBehavior> behaviors) {
        if (request.hasQueryType(SplitQueryType.TRANSACTION)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_NONSUPPORT_TRANSACTION_OPERATE_ERROR.name()));
        }

        long behaviorCount = 0;
        for (BehaviorRequest behaviorRequest : BehaviorRelations.flattenResourceIgnoringPermission(request.getRelations())) {
            BehaviorAction action = behaviorRequest.action();
            if (action == BehaviorAction.SWITCH) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_NONSUPPORT_SWITCH_CTX_ERROR.name()));
            }

            BehaviorObject resource = behaviorRequest.resource();
            TargetType resourceType = Objects.requireNonNullElse(resource.getObjectType(), TargetType.Unknown);
            String resourcePath = DmDsUtils.normalizeResourcePath(resource.getObjectPath());
            String resourceKey = resourceType + "|" + resourcePath;
            ApprovalBehavior target = behaviors.computeIfAbsent(resourceKey, ignored -> {
                ApprovalBehavior value = new ApprovalBehavior();
                value.setResourceType(resourceType);
                value.setResourcePath(resourcePath);
                return value;
            });
            target.getActions().add(action);
            target.getActionCounts().merge(action, 1L, Long::sum);
            behaviorCount++;
        }
        return behaviorCount;
    }

}
