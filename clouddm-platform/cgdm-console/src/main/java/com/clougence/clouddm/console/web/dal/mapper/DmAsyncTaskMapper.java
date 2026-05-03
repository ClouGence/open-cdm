package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmAsyncTaskDO;

/**
 * @author mode
 * @since 1.1.3
 */
public interface DmAsyncTaskMapper extends BaseMapper<DmAsyncTaskDO> {

    int resetAsyncTaskStatus(String consoleIp);

    int resetCancelingAsyncTaskStatus(String consoleIp);

    int resetPausingAsyncTaskStatus(String consoleIp);

    int resetInitAsyncTaskStatus(String consoleIp);

    // --- AsyncTaskScheduleService ---

    List<DmAsyncTaskDO> queryWaitTask(int limit, String consoleIp);

    DmAsyncTaskDO queryByBiz(String bizId, String bizType);

    int updateFromWaitTo(long taskId, String toStatus, String message);

    int updateFromRunningTo(long taskId, String toStatus, String message);

    int updateStatusTo(long taskId, String toStatus, String message);

    DmAsyncTaskDO queryById(long taskId);

    List<DmAsyncTaskDO> queryDepends(String bizId, String bizType);

    int batchResumeFromBlock(List<Long> taskIds, String message);

    // --- AsyncTaskService ---

    int updateFromBlockTo(long taskId, String toStatus, String message);

    int updateFromPauseTo(long taskId, String toStatus, String message);

    void retryFailureOrCancelTask(long taskId, String message, String consoleIp);

    void resumePauseTask(long taskId, String message, String consoleIp);

    int activateTask(long taskId, String consoleIp);

    // --- DockTaskController --- 

    DmAsyncTaskDO queryByIdAndOwnerUid(long taskId, String ownerUid);

    List<DmAsyncTaskDO> queryRunListByOwner(String ownerUid, boolean inDock, int limit);

    List<DmAsyncTaskDO> queryFinishListByOwner(String ownerUid, boolean inDock, int limit);

    int updateStatusMessage(long taskId, String message);

    int updateFromCancelingTo(long taskId, String toStatus, String message);

    void updateProcess(long taskId, String processType, String processValue);

    void updateFromInitTo(long taskId, String toStatus, String message);
}
