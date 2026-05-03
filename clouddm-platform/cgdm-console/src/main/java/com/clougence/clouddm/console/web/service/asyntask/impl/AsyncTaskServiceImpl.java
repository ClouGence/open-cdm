package com.clougence.clouddm.console.web.service.asyntask.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskConfig;
import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskScheduleService;
import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.dal.enumeration.DmAsyncTaskProcessType;
import com.clougence.clouddm.console.web.dal.enumeration.DmAsyncTaskStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmAsyncTaskMapper;
import com.clougence.clouddm.console.web.dal.model.DmAsyncTaskDO;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.service.asyntask.AsyncTaskService;
import com.clougence.clouddm.console.web.util.InstanceUtil;
import com.clougence.rdp.util.RdpTimerUtils;
import com.clougence.utils.HostUtil;

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
    private DmAsyncTaskMapper        dmAsyncTaskMapper;
    @Resource
    private AsyncTaskScheduleService asyncTaskScheduleService;
    @Resource
    private DmConsoleConfig          dmConfig;

    @Override
    public void submitTask(String uid, AsyncTaskConfig config) {
        List<DmAsyncTaskDO> taskList = this.storeTask(uid, config);

        for (DmAsyncTaskDO task : taskList) {
            DmGlobalEventBus.triggerDmAsyncEvent(task);
        }

        this.activateTask(config.getDelayActivate(), taskList);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
    public List<DmAsyncTaskDO> storeTask(String uid, AsyncTaskConfig config) {
        List<DmAsyncTaskDO> taskList = new ArrayList<>();

        DmAsyncTaskDO rootTask = genAsyncTaskConfig(uid, config, null);
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

        for (DmAsyncTaskDO task : taskList) {
            this.dmAsyncTaskMapper.insert(task);
        }

        return taskList;
    }

    private void activateTask(int delay, List<DmAsyncTaskDO> taskList) {
        Runnable supplier = () -> {
            for (DmAsyncTaskDO task : taskList) {
                this.dmAsyncTaskMapper.activateTask(task.getId(), getHostIp());
            }
            this.asyncTaskScheduleService.trigger();
        };

        if (delay > 0) {
            RdpTimerUtils.onTimeout(t -> supplier.run(), delay, TimeUnit.MICROSECONDS);
        } else {
            supplier.run();
        }
    }

    private String getHostIp() {
        if (this.dmConfig.getDmMode() == DmMode.desktop) {
            return "127.0.0.1";
        } else {
            return HostUtil.getHostIp();
        }
    }

    private static DmAsyncTaskDO genAsyncTaskConfig(String uid, AsyncTaskConfig config, AsyncTaskConfig depend) {
        DmAsyncTaskDO taskDO = new DmAsyncTaskDO();
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
        taskDO.setProcessType(config.getProcessType() == null ? DmAsyncTaskProcessType.SCROLL : config.getProcessType());
        taskDO.setFastFail(config.isFastFail());
        taskDO.setStatus(DmAsyncTaskStatus.INIT);
        taskDO.setStatusMsg("");
        return taskDO;
    }

    @Override
    public List<DmAsyncTaskDO> listDockList(String uid) {
        int dockSize = this.dmConfig.getAsyncTaskDockSize();

        List<DmAsyncTaskDO> taskList = this.dmAsyncTaskMapper.queryRunListByOwner(uid, true, dockSize);
        if (taskList.size() < dockSize) {
            //List<DmAsyncTaskDO> appendList = this.asyncTaskMapper.queryFinishListByOwner(uid, true, dockSize - taskList.size());
            //taskList.addAll(appendList);
        }

        return taskList;
    }

    @Override
    public DmAsyncTaskDO queryAsyncTaskByBizId(String bizId, String bizType) {
        return this.dmAsyncTaskMapper.queryByBiz(bizId, bizType);
    }

    @Override
    public void pauseTask(String bizId, String bizType, String reasons) {
        DmAsyncTaskDO taskDO = this.dmAsyncTaskMapper.queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.asyncTaskScheduleService.pauseTask(taskDO.getId(), reasons);
        }
    }

    @Override
    public void cancelTask(String bizId, String bizType, String reasons) {
        DmAsyncTaskDO taskDO = this.dmAsyncTaskMapper.queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.asyncTaskScheduleService.cancelTask(taskDO.getId(), reasons);
        }
    }

    @Override
    public void resumeTask(String bizId, String bizType, String reasons) {
        DmAsyncTaskDO taskDO = this.dmAsyncTaskMapper.queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.asyncTaskScheduleService.resumeTask(taskDO.getId(), reasons);
        }
    }

    @Override
    public void retryTask(String bizId, String bizType, String reasons) {
        DmAsyncTaskDO taskDO = this.dmAsyncTaskMapper.queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.asyncTaskScheduleService.retryTask(taskDO.getId(), reasons);
        }
    }
}
