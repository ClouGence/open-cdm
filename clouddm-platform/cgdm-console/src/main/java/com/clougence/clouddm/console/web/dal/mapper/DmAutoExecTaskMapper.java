package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.console.web.dal.enumeration.AutoExecTaskStatus;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecTaskDO;

public interface DmAutoExecTaskMapper extends BaseMapper<DmAutoExecTaskDO> {

    void updateStatusByTaskId(@Param("taskId") Long taskId, @Param("status") AutoExecTaskStatus status);

    void updateStatusAndAffectLineByTaskId(@Param("taskId") Long taskId, @Param("status") AutoExecTaskStatus status, @Param("affectRow") Long affectRow);

    void transactionCommit(@Param("jobId") Long jobId);

    int transactionRollback(@Param("jobId") Long jobId);

    int taskSkip(@Param("jobId") Long jobId, @Param("taskId") Long taskId);

    List<DmAutoExecTaskDO> queryGroupTaskListByStatus(@Param("jobId") Long jobId, @Param("status") AutoExecTaskStatus status);

    DmAutoExecTaskDO queryByBizId(@Param("bizId") String bizId);

    DmAutoExecTaskDO queryOneByJobIdAndStatus(@Param("jobId") Long jobId, @Param("status") AutoExecTaskStatus status);

    List<DmAutoExecTaskDO> queryNeedExecTaskList(@Param("jobId") Long jobId);

    int queryNeedExecTaskCount(@Param("jobId") Long jobId);

    void retryTask(@Param("jobId") Long jobId);

    IPage<DmAutoExecTaskDO> queryListByJobId(Page page, @Param("jobId") Long jobId, @Param("status") AutoExecTaskStatus status);

    List<DmAutoExecTaskDO> queryListByJobId(@Param("jobId") Long jobId, @Param("status") AutoExecTaskStatus status);

    void cancelAllWaitTask(@Param("jobId") Long jobId);
}
