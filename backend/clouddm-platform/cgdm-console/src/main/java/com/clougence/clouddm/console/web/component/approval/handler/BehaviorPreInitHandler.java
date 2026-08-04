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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.analysis.*;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
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
    protected void doHandle(DataSourceConfig dsConfig, DsLevels dsLevels, PreInitContext context, Runnable onProcessed) {
        DmApprovalDO approvalDO = context.getApproval();
        AnalysisQueryOptions options = AnalysisQueryOptions.builder()
            .currentUid(approvalDO.getOwnerUid())
            .dataSourceId(approvalDO.getBindDsId())
            .levels(dsLevels.levelsParam())
            .skip(QueryAnalysisFeature.REWRITE, QueryAnalysisFeature.LINEAGE, QueryAnalysisFeature.MASKING)
            .build();

        this.approvalService.consumeSqlFile(approvalDO.getId(), sql -> {
            try (Reader reader = Files.newBufferedReader(sql, StandardCharsets.UTF_8);
                    Stream<QueryRequest> requests = this.queryAnalysisService.analysisRequestsStream(dsConfig, reader, Collections.emptyList(), 1, 0, options)) {
                requests.forEachOrdered(request -> {
                    this.analyzeRequest(request, context);
                    onProcessed.run();
                });
                return null;
            }
        });
    }

    private void analyzeRequest(QueryRequest request, PreInitContext context) {
        if (request.hasQueryType(SplitQueryType.TRANSACTION)) {
            throw new UnsupportedOperationException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_NONSUPPORT_TRANSACTION_OPERATE_ERROR.name()));
        }

        for (BehaviorRequest behaviorRequest : BehaviorRelations.flattenResourceIgnoringPermission(request.getRelations())) {
            BehaviorAction action = behaviorRequest.action();
            if (action == BehaviorAction.SWITCH) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_NONSUPPORT_SWITCH_CTX_ERROR.name()));
            }

            BehaviorObject resource = behaviorRequest.resource();
            TargetType resourceType = Objects.requireNonNullElse(resource.getObjectType(), TargetType.Unknown);
            String resourcePath = DmDsUtils.normalizeResourcePath(resource.getObjectPath());
            ApprovalBehavior behavior = new ApprovalBehavior();
            behavior.setResourceType(resourceType);
            behavior.setResourcePath(resourcePath);
            behavior.getActions().add(action);
            context.addBehavior(behavior);
        }
    }

    @Override
    protected void fillResult(ApprovalAnalysisStateMO state, PreInitContext context) {
        state.setBehaviors(context.getBehaviors());
    }
}
