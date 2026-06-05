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
package com.clougence.clouddm.console.web.service.project;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.component.project.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.component.project.model.ChangeTicketInfoResult;
import com.clougence.clouddm.platform.dal.model.project.DmProjectChangeDO;
import com.clougence.clouddm.platform.dal.model.project.DmProjectChangeItemDO;
import com.clougence.clouddm.console.web.model.fo.project.ProjectChangeExecLogFO;
import com.clougence.clouddm.console.web.model.fo.project.ProjectChangeExecTaskListFO;
import com.clougence.clouddm.console.web.model.fo.project.ProjectChangeListFO;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectChangeBodyVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectChangeVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmPageVO;
import com.clougence.clouddm.console.web.service.project.domain.CreateSuggest;

public interface DmChangeService {

    IPage<ProjectChangeVO> queryChangeByProjectAndQuery(String ownerUid, long projectId, ProjectChangeListFO fo);

    DmProjectChangeDO queryChangeById(String ownerUid, long changeId);

    ProjectChangeBodyVO fetchChangeBodyByChangeId(String ownerUid, long changeId);

    List<DmProjectChangeItemDO> fetchChangeCheckByChangeId(String ownerUid, long changeId);

    ChangeTicketInfoResult fetchChangeApprovalByChangeId(String ownerUid, long changeId);

    ChangeExecuteInfo fetchChangeExecuteByChangeId(String ownerUid, long changeId);

    void skipCheck(String ownerUid, String userUid, long changeId);

    void confirmExec(String ownerUid, String userUid, long changeId, DmAutoExecConfigFO fo);

    DmAutoExecJobVO queryExecJobInfo(String ownerUid, long changeId);

    DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String ownerUid, ProjectChangeExecTaskListFO fo);

    List<DmBizLogVO> queryExecLog(String ownerUid, ProjectChangeExecLogFO fo);

    void pauseExecJob(String ownerUid, String curUid, long changeId);

    void startExecJob(String ownerUid, String curUid, long changeId);

    void retryExecJob(String ownerUid, String curUid, long changeId);

    void abortExecJob(String ownerUid, String curUid, long changeId);

    void skipExecTask(String ownerUid, String curUid, long changeId, long taskId);

    void retryChange(String ownerUid, String curUid, long changeId);

    void restartChange(String ownerUid, String curUid, long changeId);

    void closeChange(String ownerUid, String curUid, long changeId);

    void verifyDevops(String ownerUid, long projectId, long devopsId);

    CreateSuggest createChangeSuggest(String ownerUid, long projectId, long devopsId, String commitId);

    ResWebData<String> triggerChangeSuggest(String ownerUid, long projectId, long devopsId, String commitId);
}
