package com.clougence.rdp.component.asyntask.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.rdp.component.asyntask.RdpAsyncTaskScheduleService;
import com.clougence.rdp.component.asyntask.RdpAsyncTaskService;
import com.clougence.rdp.component.asyntask.RdpAsyncTaskType;
import com.clougence.rdp.component.asyntask.model.RdpAsyncTaskConfig;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.dal.enumeration.RdpAsyncTaskProcessType;
import com.clougence.rdp.dal.enumeration.RdpAsyncTaskStatus;
import com.clougence.rdp.dal.mapper.RdpAsyncTaskMapper;
import com.clougence.rdp.dal.model.RdpAsyncTaskDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.util.RdpHostUtil;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.rdp.util.RdpInstanceUtil;
import com.clougence.rdp.util.RdpTimerUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Low-latency task distribution
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-10-09
 */
@Slf4j
@Service
public class RdpAsyncTaskServiceImpl implements RdpAsyncTaskService {

    @Resource
    private RdpAsyncTaskMapper          rdpAsyncTaskMapper;
    @Resource
    private RdpAsyncTaskScheduleService rdpAsyncTaskScheduleService;
    @Resource
    private RdpConsoleConfig            rdpConfig;

    @Override
    public void submitTask(String uid, RdpAsyncTaskConfig config) {
        List<RdpAsyncTaskDO> taskList = this.storeTask(uid, config);

        this.activateTask(config.getDelayActivate(), taskList);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
    public List<RdpAsyncTaskDO> storeTask(String uid, RdpAsyncTaskConfig config) {
        List<RdpAsyncTaskDO> taskList = new ArrayList<>();

        RdpAsyncTaskDO rootTask = genRdpAsyncTaskConfig(uid, config, null);
        taskList.add(rootTask);

        if (config.getSubTask() != null) {
            RdpAsyncTaskConfig depend = null;
            for (RdpAsyncTaskConfig subConf : config.getSubTask()) {
                taskList.add(genRdpAsyncTaskConfig(uid, subConf, depend));
                if (!Boolean.FALSE.equals(config.getParallel())) {
                    depend = subConf;
                }
            }
        }

        for (RdpAsyncTaskDO task : taskList) {
            this.rdpAsyncTaskMapper.insert(task);
        }

        return taskList;
    }

    private void activateTask(int delay, List<RdpAsyncTaskDO> taskList) {
        Runnable supplier = () -> {
            for (RdpAsyncTaskDO task : taskList) {
                this.rdpAsyncTaskMapper.activateTask(task.getId(), getHostIp());
            }
            this.rdpAsyncTaskScheduleService.trigger();
        };

        if (delay > 0) {
            RdpTimerUtils.onTimeout(t -> supplier.run(), delay, TimeUnit.MICROSECONDS);
        } else {
            supplier.run();
        }
    }

    private String getHostIp() { return RdpHostUtil.getHostIp(); }

    private static RdpAsyncTaskDO genRdpAsyncTaskConfig(String uid, RdpAsyncTaskConfig config, RdpAsyncTaskConfig depend) {
        RdpAsyncTaskDO taskDO = new RdpAsyncTaskDO();
        taskDO.setTitle(config.getTitle());
        taskDO.setDescription(config.getDescription());
        taskDO.setBizId(config.getBizId());
        taskDO.setBizType(config.getBizType());
        taskDO.setDependOnBizId(depend == null ? null : depend.getBizId());
        taskDO.setDependOnBizType(depend == null ? null : depend.getBizType());
        taskDO.setUid(uid);
        taskDO.setHandlerName(RdpInstanceUtil.getSpringServiceAnnotationValue(config.getHandlerType()));
        taskDO.setHandlerType(config.getHandlerType().getName());
        taskDO.setConfigData(config.getConfigData());
        taskDO.setShowInDock(config.isShowInDock());
        taskDO.setProcessType(config.getProcessType() == null ? RdpAsyncTaskProcessType.SCROLL : config.getProcessType());
        taskDO.setFastFail(config.isFastFail());
        taskDO.setStatus(RdpAsyncTaskStatus.INIT);
        taskDO.setStatusMsg("");
        return taskDO;
    }

    @Override
    public List<RdpAsyncTaskDO> listDockList(String uid) {
        int dockSize = this.rdpConfig.getAsyncTaskDockSize();

        List<RdpAsyncTaskDO> taskList = this.rdpAsyncTaskMapper.queryRunListByOwner(uid, true, dockSize);
        if (taskList.size() < dockSize) {
            //List<RdpAsyncTaskDO> appendList = this.asyncTaskMapper.queryFinishListByOwner(uid, true, dockSize - taskList.size());
            //taskList.addAll(appendList);
        }

        return taskList;
    }

    @Override
    public RdpAsyncTaskDO queryAsyncTaskByBizId(String bizId, RdpAsyncTaskType bizType) {
        return this.rdpAsyncTaskMapper.queryByBiz(bizId, bizType);
    }

    @Override
    public void pauseTask(String bizId, RdpAsyncTaskType bizType, String reasons) {
        RdpAsyncTaskDO taskDO = this.rdpAsyncTaskMapper.queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.rdpAsyncTaskScheduleService.pauseTask(taskDO.getId(), reasons);
        } else {
            String logMsg = "[" + bizType + "]" + bizId;
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TASK_NOT_EXIST_ERROR.name(), logMsg));
        }
    }

    @Override
    public void cancelTask(String bizId, RdpAsyncTaskType bizType, String reasons) {
        RdpAsyncTaskDO taskDO = this.rdpAsyncTaskMapper.queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.rdpAsyncTaskScheduleService.cancelTask(taskDO.getId(), reasons);
        } else {
            String logMsg = "[" + bizType + "]" + bizId;
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TASK_NOT_EXIST_ERROR.name(), logMsg));
        }
    }

    @Override
    public void resumeTask(String bizId, RdpAsyncTaskType bizType, String reasons) {
        RdpAsyncTaskDO taskDO = this.rdpAsyncTaskMapper.queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.rdpAsyncTaskScheduleService.resumeTask(taskDO.getId(), reasons);
        } else {
            String logMsg = "[" + bizType + "]" + bizId;
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TASK_NOT_EXIST_ERROR.name(), logMsg));
        }
    }

    @Override
    public void retryTask(String bizId, RdpAsyncTaskType bizType, String reasons) {
        RdpAsyncTaskDO taskDO = this.rdpAsyncTaskMapper.queryByBiz(bizId, bizType);
        if (taskDO != null) {
            this.rdpAsyncTaskScheduleService.retryTask(taskDO.getId(), reasons);
        } else {
            String logMsg = "[" + bizType + "]" + bizId;
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TASK_NOT_EXIST_ERROR.name(), logMsg));
        }
    }
}
