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
package com.clougence.clouddm.platform.dal.mapper.execution;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;

/**
 * @author mode
 * @since 1.1.3
 */
public interface DmExecAsyncTaskMapper extends BaseMapper<DmExecAsyncTaskDO> {

    /**
     * @deprecated Legacy consoleIp-based task ownership reset.
     */
    @Deprecated
    int resetAsyncTaskStatus(String consoleIp);

    /**
     * @deprecated Legacy consoleIp-based task ownership reset.
     */
    @Deprecated
    int resetCancelingAsyncTaskStatus(String consoleIp);

    /**
     * @deprecated Legacy consoleIp-based task ownership reset.
     */
    @Deprecated
    int resetPausingAsyncTaskStatus(String consoleIp);

    /**
     * @deprecated Legacy consoleIp-based task ownership reset.
     */
    @Deprecated
    int resetInitAsyncTaskStatus(String consoleIp);

    // --- AsyncTaskScheduleService ---

    /**
     * @deprecated Legacy consoleIp-based task ownership query.
     */
    @Deprecated
    List<DmExecAsyncTaskDO> queryWaitTask(int limit, String consoleIp);

    DmExecAsyncTaskDO queryByBiz(String bizId, String bizType);

    int updateFromWaitTo(long taskId, String toStatus, String message);

    int updateFromRunningTo(long taskId, String toStatus, String message);

    int updateStatusTo(long taskId, String toStatus, String message);

    DmExecAsyncTaskDO queryById(long taskId);

    List<DmExecAsyncTaskDO> queryDepends(String bizId, String bizType);

    int batchResumeFromBlock(List<Long> taskIds, String message);

    // --- AsyncTaskService ---

    int updateFromBlockTo(long taskId, String toStatus, String message);

    int updateFromPauseTo(long taskId, String toStatus, String message);

    /**
     * @deprecated Legacy consoleIp-based task reassignment.
     */
    @Deprecated
    void retryFailureOrCancelTask(long taskId, String message, String consoleIp);

    /**
     * @deprecated Legacy consoleIp-based task reassignment.
     */
    @Deprecated
    void resumePauseTask(long taskId, String message, String consoleIp);

    /**
     * @deprecated Legacy consoleIp-based task activation.
     */
    @Deprecated
    int activateTask(long taskId, String consoleIp);

    // --- DockTaskController --- 

    DmExecAsyncTaskDO queryByIdAndOwnerUid(long taskId, String ownerUid);

    List<DmExecAsyncTaskDO> queryRunListByOwner(String ownerUid, boolean inDock, int limit);

    List<DmExecAsyncTaskDO> queryFinishListByOwner(String ownerUid, boolean inDock, int limit);

    int updateStatusMessage(long taskId, String message);

    int updateFromCancelingTo(long taskId, String toStatus, String message);

    void updateProcess(long taskId, String processType, String processValue);

    void updateFromInitTo(long taskId, String toStatus, String message);
}
