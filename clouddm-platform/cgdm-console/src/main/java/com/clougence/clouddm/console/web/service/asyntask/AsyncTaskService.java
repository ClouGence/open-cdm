package com.clougence.clouddm.console.web.service.asyntask;

import java.util.List;

import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskConfig;
import com.clougence.clouddm.console.web.dal.model.DmAsyncTaskDO;

/**
 * @author mode 2019-12-30 17:44
 * @since 1.1.3
 */
public interface AsyncTaskService {

    DmAsyncTaskDO queryAsyncTaskByBizId(String bizId, String bizType);

    List<DmAsyncTaskDO> listDockList(String uid);

    void submitTask(String uid, AsyncTaskConfig config);

    void pauseTask(String bizId, String bizType, String reasons);

    void cancelTask(String bizId, String bizType, String reasons);

    void resumeTask(String bizId, String bizType, String reasons);

    void retryTask(String bizId, String bizType, String reasons);
}
