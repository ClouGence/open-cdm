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
package com.clougence.clouddm.console.web.component.approval.model;

import java.util.*;

import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBehavior;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import lombok.Getter;

/**
 * Holds all analysis results produced during one approval pre-initialization.
 *
 * @author clougence
 */
@Getter
public class PreInitContext {

    private final DmApprovalDO                       approval;
    private final Map<String, ApprovalBehavior>      behaviors      = new LinkedHashMap<>();
    private final SecRulesCheckResult                ruleCheckResult = new SecRulesCheckResult();

    public PreInitContext(DmApprovalDO approval){
        this.approval = Objects.requireNonNull(approval, "approval");
    }

    public void addBehavior(ApprovalBehavior behavior) {
        String resourceKey = behavior.getResourceType() + "|" + behavior.getResourcePath();
        ApprovalBehavior target = this.behaviors.computeIfAbsent(resourceKey, ignored -> {
            ApprovalBehavior value = new ApprovalBehavior();
            value.setResourceType(behavior.getResourceType());
            value.setResourcePath(behavior.getResourcePath());
            return value;
        });
        target.getActions().addAll(behavior.getActions());
    }

    public List<ApprovalBehavior> getBehaviors() { return new ArrayList<>(this.behaviors.values()); }

    public void addRuleCheckResult(SecRulesCheckResult result) {
        this.ruleCheckResult.merge(result);
    }

    public List<TicketRuleCheckResult> getRuleCheckResults() { return DmConvertUtils.convertToTicketRuleCheckResults(this.ruleCheckResult); }
}
