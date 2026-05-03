package com.clougence.rdp.component.asyntask;

import java.util.List;

import com.clougence.rdp.component.asyntask.model.RdpAsyncTaskConfig;
import com.clougence.rdp.dal.model.RdpAsyncTaskDO;

/**
 * @author mode 2019-12-30 17:44
 * @since 1.1.3
 */
public interface RdpAsyncTaskService {

    RdpAsyncTaskDO queryAsyncTaskByBizId(String bizId, RdpAsyncTaskType bizType);

    List<RdpAsyncTaskDO> listDockList(String uid);

    void submitTask(String uid, RdpAsyncTaskConfig config);

    void pauseTask(String bizId, RdpAsyncTaskType bizType, String reasons);

    void cancelTask(String bizId, RdpAsyncTaskType bizType, String reasons);

    void resumeTask(String bizId, RdpAsyncTaskType bizType, String reasons);

    void retryTask(String bizId, RdpAsyncTaskType bizType, String reasons);

}
