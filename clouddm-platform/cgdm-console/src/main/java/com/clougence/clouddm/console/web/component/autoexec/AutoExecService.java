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
package com.clougence.clouddm.console.web.component.autoexec;

import java.util.List;

import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmPageVO;
import com.clougence.clouddm.platform.dal.util.PageObj;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecTaskStatus;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;

public interface AutoExecService {

    // true == jobFinish
    boolean skipTask(String bizId, SQLJobBizType type, long taskId, String uid);

    void continueTask(String bizId, SQLJobBizType type, long taskId);

    void retryJob(String bizId, SQLJobBizType type, String uid);

    void endJob(String bizId, SQLJobBizType type, String uid);

    void stopJob(String bizId, SQLJobBizType type, String uid);

    DmAutoExecJobVO queryAutoExecJob(String bizId, SQLJobBizType type, boolean canOperate);

    DmPageVO<DmAutoExecTaskVO> queryAutoExecTaskList(String bizId, SQLJobBizType type, boolean canOperate, AutoExecTaskStatus status, PageObj page);

    void createJob(String ownerUid, String execUser, DmAutoExecConfigFO config, DsLevels dsLevels, SQLJobBizType bizType, String bizId, List<SplitScript> scripts);
}
