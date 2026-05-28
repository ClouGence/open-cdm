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

import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;

public interface ApprovalHandler {

    ApprovalBiz handleType();

    //
    // for Create
    //

    void createApproval(long approvalId, ImSenderService sender);

    //
    // for execute
    //

    void executeTicket(long approvalId, ApprovalBiz bizType, ImSenderService sender);

    void runningCheck(long approvalId, ApprovalBiz bizType, ImSenderService sender);

    //
    // for query person
    //

    List<PrimaryUserVO> queryPerson(long approvalId);

    //
    // for callback
    //

    void approvalCompleted(long approvalId, ApprovalBiz bizType, ImSenderService sender);

    void approvalRefuse(long approvalId, ApprovalBiz bizType, ImSenderService sender);

    void approvalFailed(long approvalId, ApprovalBiz bizType, ImSenderService sender);

    void approvalCanceled(long approvalId, ApprovalBiz bizType, ImSenderService sender);
}
