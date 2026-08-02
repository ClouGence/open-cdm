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

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.analysis.BehaviorRelations;
import com.clougence.clouddm.console.web.component.analysis.BehaviorRequest;
import com.clougence.clouddm.console.web.component.approval.PreInitHandler;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBehavior;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

/**
 * Collects approval behaviors one query request at a time.
 *
 * @author clougence
 */
@Service
public class BehaviorPreInitHandler implements PreInitHandler {

    @Override
    public boolean supports(PreInitContext context) {
        var approval = context.getApproval();
        return approval.getApproBiz() == ApprovalBiz.DM_QUERY || approval.getApproBiz() == ApprovalBiz.DM_CHANGE;
    }

    @Override
    public void handle(QueryRequest request, PreInitContext context) {
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
}
