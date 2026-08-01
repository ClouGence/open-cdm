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

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecTaskStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoTaskDO;

public interface DmExecAutoTaskMapper extends BaseMapper<DmExecAutoTaskDO> {

    int batchInsert(@Param("tasks") List<DmExecAutoTaskDO> tasks);

    int deleteByJobId(@Param("jobId") Long jobId);

    void updateStatusByTaskId(@Param("taskId") Long taskId, @Param("status") AutoExecTaskStatus status);

    void updateStatusAndAffectLineByTaskId(@Param("taskId") Long taskId, @Param("status") AutoExecTaskStatus status, @Param("affectRow") Long affectRow);

    void transactionCommit(@Param("jobId") Long jobId);

    int transactionRollback(@Param("jobId") Long jobId);

    int taskSkip(@Param("queryId") String queryId);

    List<DmExecAutoTaskDO> queryGroupTaskListByStatus(@Param("jobId") Long jobId, @Param("status") AutoExecTaskStatus status);

    DmExecAutoTaskDO queryByBizId(@Param("bizId") String bizId);

    DmExecAutoTaskDO queryByQueryId(@Param("queryId") String queryId);

    DmExecAutoTaskDO queryOneByJobIdAndStatus(@Param("jobId") Long jobId, @Param("status") AutoExecTaskStatus status);

    List<Long> queryNeedExecTaskIdsBatch(@Param("jobId") Long jobId, @Param("afterExecOrder") int afterExecOrder, @Param("batchSize") int batchSize);

    List<DmExecAutoTaskDO> queryNeedExecTasksByIds(@Param("jobId") Long jobId, @Param("taskIds") List<Long> taskIds);

    int queryNeedExecTaskCount(@Param("jobId") Long jobId);

    int queryNeedExecTaskMaxOrder(@Param("jobId") Long jobId);

    void retryTask(@Param("jobId") Long jobId);

    IPage<DmExecAutoTaskDO> queryListByJobId(Page page, @Param("jobId") Long jobId, @Param("status") AutoExecTaskStatus status);

    List<DmExecAutoTaskDO> queryListByJobId(@Param("jobId") Long jobId, @Param("status") AutoExecTaskStatus status);

    void cancelAllWaitTask(@Param("jobId") Long jobId);
}
