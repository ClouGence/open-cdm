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

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;

public interface DmExecAutoJobMapper extends BaseMapper<DmExecAutoJobDO> {

    DmExecAutoJobDO queryById(long jobId);

    DmExecAutoJobDO queryByDependOnBizId(@Param("bizId") String bizId);

    DmExecAutoJobDO queryByBizId(@Param("bizId") String bizId);

    DmExecAutoJobDO queryByBizIdForUpdate(@Param("bizId") String bizId);

    DmExecAutoJobDO queryByIdForUpdate(@Param("id") Long id);

    List<Long> listUnFinishJobIdList(@Param("time") Date date);

    int updateJobStatus(@Param("jobId") Long jobId, @Param("status") AutoExecJobStatus status);

    void finishJob(@Param("jobId") Long jobId);

    int retryJob(@Param("jobId") Long jobId);

    void updateReportTime(@Param("jobIdList") List<Long> jobIdList);

    void updateOverOutJob(@Param("date") Date date);

    void updateQueryIdByJobId(@Param("jobId") Long jobId, @Param("queryId") String queryId);

    void updateWorkerErrorJob(@Param("wsn") String wsn);

    void updateWorkerWaitExecuteJob(@Param("wsn") String wsn);

    List<DmExecAutoJobDO> queryErrorJob(@Param("wsn") String wsn);
}
