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

import java.util.stream.Stream;

import com.clougence.clouddm.console.web.component.autoexec.model.AutoExecJobCreateRequest;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecTaskStatus;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.clouddm.platform.dal.util.PageObj;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;

public interface AutoExecService {

    void createJob(AutoExecJobCreateRequest request, Stream<SplitScript> scripts);

    void startJob(String jobBizId, String operatorUid);

    void deleteJob(String jobBizId);

    void dispatchJob(Long jobId);

    boolean skipTask(String bizId, SQLJobBizType type, long taskId);

    void continueTask(String bizId, SQLJobBizType type, long taskId);

    void retryJob(String bizId, SQLJobBizType type);

    void endJob(String bizId, SQLJobBizType type);

    void stopJob(String bizId, SQLJobBizType type);

    DmAutoExecJobVO queryAutoExecJob(String bizId, SQLJobBizType type, boolean canOperate);

    DmPageVO<DmAutoExecTaskVO> queryAutoExecTaskList(String bizId, SQLJobBizType type, boolean canOperate, AutoExecTaskStatus status, PageObj page);
}
