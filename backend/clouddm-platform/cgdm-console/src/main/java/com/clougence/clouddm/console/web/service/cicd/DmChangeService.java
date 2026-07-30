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
package com.clougence.clouddm.console.web.service.cicd;

import java.util.List;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfoResult;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeExecLogFO;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeExecTaskListFO;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeListFO;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeBodyVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.service.cicd.domain.ChangeTriggerContext;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggest;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeDO;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeItemDO;

public interface DmChangeService {

    DmPageVO<ChangeVO> queryChangeByFlowAndQuery(String ownerUid, long flowId, ChangeListFO fo);

    DmChangeDO queryChangeById(String ownerUid, long changeId);

    ChangeBodyVO fetchChangeBodyByChangeId(String ownerUid, long changeId);

    List<DmChangeItemDO> fetchChangeCheckByChangeId(String ownerUid, long changeId);

    ChangeTicketInfoResult fetchChangeApprovalByChangeId(String ownerUid, long changeId);

    ChangeExecuteInfo fetchChangeExecuteByChangeId(String ownerUid, long changeId);

    void skipCheck(String ownerUid, String userUid, long changeId);

    void confirmExec(String ownerUid, String userUid, long changeId, DmAutoExecConfigFO fo);

    DmAutoExecJobVO queryExecJobInfo(String ownerUid, long changeId);

    DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String ownerUid, ChangeExecTaskListFO fo);

    List<DmBizLogVO> queryExecLog(String ownerUid, ChangeExecLogFO fo);

    void pauseExecJob(String ownerUid, String curUid, long changeId);

    void startExecJob(String ownerUid, String curUid, long changeId);

    void retryExecJob(String ownerUid, String curUid, long changeId);

    void abortExecJob(String ownerUid, String curUid, long changeId);

    void skipExecTask(String ownerUid, String curUid, long changeId, long taskId);

    void continueExecTask(String ownerUid, long changeId, long taskId);

    void retryChange(String ownerUid, String curUid, long changeId);

    void restartChange(String ownerUid, String curUid, long changeId);

    void closeChange(String ownerUid, String curUid, long changeId);

    void verifyFlow(String ownerUid, long flowId);

    CreateSuggest createChangeSuggest(String ownerUid, long flowId, String commitId);

    ResWebData<String> triggerChangeSuggest(String ownerUid, long flowId, ChangeTriggerContext triggerContext);
}
