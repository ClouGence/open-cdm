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
package com.clougence.clouddm.console.web.service.asyntask;

import java.util.List;

import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskConfig;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;

/**
 * @author mode 2019-12-30 17:44
 * @since 1.1.3
 */
public interface AsyncTaskService {

    DmExecAsyncTaskDO queryAsyncTaskByBizId(String bizId, String bizType);

    List<DmExecAsyncTaskDO> listDockList(String uid);

    void submitTask(String uid, AsyncTaskConfig config);

    void pauseTask(String bizId, String bizType, String reasons);

    void cancelTask(String bizId, String bizType, String reasons);

    void resumeTask(String bizId, String bizType, String reasons);

    void retryTask(String bizId, String bizType, String reasons);
}
