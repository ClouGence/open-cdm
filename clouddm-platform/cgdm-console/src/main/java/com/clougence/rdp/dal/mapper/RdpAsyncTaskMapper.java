package com.clougence.rdp.dal.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.component.asyntask.RdpAsyncTaskType;
import com.clougence.rdp.dal.model.RdpAsyncTaskDO;

/**
 * @author mode
 * @since 1.1.3
 */
public interface RdpAsyncTaskMapper extends BaseMapper<RdpAsyncTaskDO> {

    int resetAsyncTaskStatus(String consoleIp);

    int resetCancelingAsyncTaskStatus(String consoleIp);

    int resetPausingAsyncTaskStatus(String consoleIp);

    int resetInitAsyncTaskStatus(String consoleIp);

    // --- AsyncTaskScheduleService ---

    void deleteOldData(Date beforeThisTime);

    List<RdpAsyncTaskDO> queryWaitTask(int limit, String consoleIp);

    RdpAsyncTaskDO queryByBiz(String bizId, RdpAsyncTaskType bizType);

    int updateFromWaitTo(long taskId, String toStatus, String message);

    int updateFromRunningTo(long taskId, String toStatus, String message);

    int updateStatusTo(long taskId, String toStatus, String message);

    RdpAsyncTaskDO queryById(long taskId);

    List<RdpAsyncTaskDO> queryDepends(String bizId, RdpAsyncTaskType bizType);

    int batchResumeFromBlock(List<Long> taskIds, String message);

    // --- AsyncTaskService ---

    int updateFromBlockTo(long taskId, String toStatus, String message);

    int updateFromPauseTo(long taskId, String toStatus, String message);

    void retryFailureOrCancelTask(long taskId, String message, String consoleIp);

    void resumePauseTask(long taskId, String message, String consoleIp);

    int activateTask(long taskId, String consoleIp);

    // --- DockTaskController ---

    RdpAsyncTaskDO queryByIdAndOwnerUid(long taskId, String ownerUid);

    List<RdpAsyncTaskDO> queryRunListByOwner(String ownerUid, boolean inDock, int limit);

    List<RdpAsyncTaskDO> queryFinishListByOwner(String ownerUid, boolean inDock, int limit);

    int updateStatusMessage(long taskId, String message);

    int updateFromCancelingTo(long taskId, String toStatus, String message);

    void updateProcess(long taskId, String processType, String processValue);

    void updateFromInitTo(long taskId, String toStatus, String message);

    void updateStatusById(long taskId, String status);
}
