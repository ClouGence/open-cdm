package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.AutoExecJobStatus;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;

public interface DmAutoExecJobMapper extends BaseMapper<DmAutoExecJobDO> {

    DmAutoExecJobDO queryById(long jobId);

    DmAutoExecJobDO queryByDependOnBizId(@Param("bizId") String bizId);

    DmAutoExecJobDO queryByBizId(@Param("bizId") String bizId);

    DmAutoExecJobDO queryByBizIdForUpdate(@Param("bizId") String bizId);

    DmAutoExecJobDO queryByIdForUpdate(@Param("id") Long id);

    List<Long> listUnFinishJobIdList(@Param("time") Date date);

    int updateJobStatus(@Param("jobId") Long jobId, @Param("status") AutoExecJobStatus status);

    void finishJob(@Param("jobId") Long jobId);

    int retryJob(@Param("jobId") Long jobId);

    void updateReportTime(@Param("jobIdList") List<Long> jobIdList);

    void updateOverOutJob(@Param("date") Date date);

    void updateQueryIdByJobId(@Param("jobId") Long jobId, @Param("queryId") String queryId);

    void updateWorkerErrorJob(@Param("wsn") String wsn);

    void updateWorkerWaitExecuteJob(@Param("wsn") String wsn);

    List<DmAutoExecJobDO> queryErrorJob(@Param("wsn") String wsn);
}
