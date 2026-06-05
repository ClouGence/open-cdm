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
package com.clougence.clouddm.platform.dal.model.approval;


/**
 * @author Ekko
 * @date 2024/6/28 14:45
*/
public enum ApprovalStage {

    /** EXPLAIN before dingding */
    EXPLAIN("TICKET_STAGE_EXPLAIN", ApprovalBiz.DM_QUERY),

    /** Wait to approval */
    APPROVAL("TICKET_STAGE_APPROVAL", ApprovalBiz.DM_QUERY, ApprovalBiz.DATA_SOURCE_AUTH, ApprovalBiz.DM_CHANGE),

    /** wait to confirm */
    CONFIRM("TICKET_STAGE_CONFIRM", ApprovalBiz.DM_QUERY),

    /** In execution */
    EXECUTION("TICKET_STAGE_EXECUTION", ApprovalBiz.DM_QUERY, ApprovalBiz.DATA_SOURCE_AUTH);

    private final ApprovalBiz[] approvalBizs;
    private final String           i18nKey;

    ApprovalStage(String i18nKey, ApprovalBiz... bizs){
        this.i18nKey = i18nKey;
        this.approvalBizs = bizs;
    }

    public String getI18nKey() { return i18nKey; }

    public boolean checkBiz(ApprovalBiz approvalBiz) {
        for (ApprovalBiz approval : this.approvalBizs) {
            if (approval == approvalBiz) {
                return true;
            }
        }
        return false;
    }
}
