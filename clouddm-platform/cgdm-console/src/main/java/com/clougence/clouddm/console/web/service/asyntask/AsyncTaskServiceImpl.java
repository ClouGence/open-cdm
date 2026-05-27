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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskConfig;
import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskScheduleService;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.util.InstanceUtil;
import com.clougence.clouddm.console.web.util.RdpTimerUtils;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskProcessType;
import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;
import com.clougence.utils.HostUtil;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * Low-latency task distribution
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-10-09
 */
@Slf4j
@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {
    @Resource
    private ExecutionDal             executionDal;
    @Resource
    private AsyncTaskScheduleService asyncTaskScheduleService;
    @Resource
    private DmConsoleConfig          dmConfig;

    @Override
    public void submitTask(String uid, AsyncTaskConfig config) {
        List<DmExecAsyncTaskDO> taskList = this.storeTask(uid, config);

        for (DmExecAsyncTaskDO task : taskList) {
            DmGlobalEventBus.triggerDmAsyncEvent(task);
        }

        this.activateTask(config.getDelayActivate(), taskList);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
    public List<DmExecAsyncTaskDO> storeTask(String uid, AsyncTaskConfig config) {
        List<DmExecAsyncTaskDO> taskList = new ArrayList<>();

        DmExecAsyncTaskDO rootTask = genAsyncTaskConfig(uid, config, null);
        taskList.add(rootTask);

        if (config.getSubTask() != null) {
            AsyncTaskConfig depend = null;
            for (AsyncTaskConfig subConf : config.getSubTask()) {
                taskList.add(genAsyncTaskConfig(uid, subConf, depend));
                if (!Boolean.FALSE.equals(config.getParallel())) {
                    depend = subConf;
                }
            }
        }

        for (DmExecAsyncTaskDO task : taskList) {
            this.executionDal.asyncTaskMapper().insert(task);
        }

        return taskList;
    }

    private void activateTask(int delay, List<DmExecAsyncTaskDO> taskList) {
        Runnable supplier = () -> {
            for (DmExecAsyncTaskDO task : taskList) {
                this.executionDal.asyncTaskMapper().activateTask(task.getId(), getHostIp());
            }
            this.asyncTaskScheduleService.trigger();
        };

        if (delay > 0) {
            RdpTimerUtils.onTimeout(t -> supplier.run(), delay, TimeUnit.MICROSECONDS);
        } else {
            supplier.run();
        }
    }

    private String getHostIp() { return HostUtil.getHostIp(); }

    private static DmExecAsyncTaskDO genAsyncTaskConfig(String uid, AsyncTaskConfig config, AsyncTaskConfig depend) {
        DmExecAsyncTaskDO taskDO = new DmExecAsyncTaskDO();
        taskDO.setTitle(config.getTitle());
        taskDO.setDescription(config.getDescription());
        taskDO.setBizId(config.getBizId());
        taskDO.setBizType(config.getBizType());
        taskDO.setDependOnBizId(depend == null ? null : depend.getBizId());
        taskDO.setDependOnBizType(depend == null ? null : depend.getBizType());
        taskDO.setUid(uid);
        taskDO.setHandlerName(InstanceUtil.getSpringServiceAnnotationValue(config.getHandlerType()));
        taskDO.setHandlerType(config.getHandlerType().getName());
        taskDO.setConfigData(config.getConfigData());
        taskDO.setShowInDock(config.isShowInDock());
        taskDO.setProcessType(config.getProcessType() == null ? AsyncTaskProcessType.SCROLL : config.getProcessType());
        taskDO.setFastFail(config.isFastFail());
        taskDO.setStatus(AsyncTaskStatus.INIT);
        taskDO.setStatusMsg("");
        return taskDO;
    }

    @Override
    public List<DmExecAsyncTaskDO> listDockList(String uid) {
        int dockSize = this.dmConfig.getAsyncTaskDockSize();

        List<DmExecAsyncTaskDO> taskList = this.executionDal.asyncTaskMapper().queryRunListByOwner(uid, true, dockSize);
        if (taskList.size() < dockSize) {
            //List<DmExecAsyncTaskDO> appendList = this.asyncTaskMapper.queryFinishListByOwner(uid, true, dockSize - taskList.size());
            //taskList.addAll(appendList);
        }

        return taskList;
    }

    @Override
    public DmExecAsyncTaskDO queryAsyncTaskByBizId(String bizId, String bizType) {
        return this.executionDal.asyncTaskMapper().queryByBiz(bizId, bizType);
    }

    @Override
    public void pauseTask(String bizId, String bizType, String reasons) {
        DmExecAsyncTaskDO taskDO = this.executionDal.asyncTaskMapper().queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.asyncTaskScheduleService.pauseTask(taskDO.getId(), reasons);
        }
    }

    @Override
    public void cancelTask(String bizId, String bizType, String reasons) {
        DmExecAsyncTaskDO taskDO = this.executionDal.asyncTaskMapper().queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.asyncTaskScheduleService.cancelTask(taskDO.getId(), reasons);
        }
    }

    @Override
    public void resumeTask(String bizId, String bizType, String reasons) {
        DmExecAsyncTaskDO taskDO = this.executionDal.asyncTaskMapper().queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.asyncTaskScheduleService.resumeTask(taskDO.getId(), reasons);
        }
    }

    @Override
    public void retryTask(String bizId, String bizType, String reasons) {
        DmExecAsyncTaskDO taskDO = this.executionDal.asyncTaskMapper().queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.asyncTaskScheduleService.retryTask(taskDO.getId(), reasons);
        }
    }
}
