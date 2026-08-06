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
package com.clougence.clouddm.console.web.component.approval;

import java.util.List;

import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.platform.dal.model.approval.*;

public interface ApprovalStateService {

    // approval

    void updateApprovalStatus(long ticketId, ApprovalStatus status, String message);

    void finalizeApproval(long ticketId, ApprovalStatus status, String message);

    //
    // process

    DmApprovalProcessDO initializeProcess(long ticketId, ApprovalStage stage, ApprovalProcessStatus status, String context);

    void updateProcessStatus(long ticketId, ApprovalStage stage, ApprovalProcessStatus status, String context);

    //
    // activity

    DmApprovalProcessActivityDO initializeActivity(long ticketId, ApprovalStage stage, String activityId, String activityTitle, int orderNumber, String status, String context);

    void updateActivityStatus(long ticketId, ApprovalStage stage, String activityId, String status, String context);

    //
    // analysis

    void initializeAnalysisActivities(long ticketId, List<ApprovalAnalysisStateMO> states);

    //
    // execution

    void initializeExecutionProgress(long ticketId);

    void resetExecutionProgress(long ticketId);

    void reportExecutionPreparationProgress(String approvalBizId, long processedCount, long totalCount);

    void markExecutionDispatched(String approvalBizId);

    void markExecutionRunning(String approvalBizId);

    void completeExecution(String approvalBizId);

    void failExecution(String approvalBizId, String errorMessage);

    void cancelExecution(String approvalBizId);
}
