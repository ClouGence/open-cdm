package com.clougence.clouddm.console.web.service.faker.asyntask;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.asyntask.AsyncTask;
import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.service.faker.FakerService;
import com.clougence.clouddm.sdk.model.faker.FakerRunStatus;
import com.clougence.clouddm.sdk.model.faker.FakerStatusDTO;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * default Task
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-09-24
 */
@Slf4j
@Service
@Scope("prototype")
public class FakerAsyncTask extends AsyncTask {

    @Resource
    private FakerService    fakerService;
    @Resource
    private DmConsoleConfig dmConfig;

    @Override
    protected void executeTask(int retryCnt, String configData) {
        FakerAsyncTaskConfig taskConfig = JsonUtils.toObj(configData, FakerAsyncTaskConfig.class);

        boolean hasInstance = this.fakerService.hasInstanceById(taskConfig.getUserId(), taskConfig.getSessionId());
        if (!hasInstance) {
            if (retryCnt == 0) {
                this.updateMessage("the faker process is not exist.");
            }
            this.finishTask(null);
            return;
        }

        FakerStatusDTO statusDTO = this.fakerService.tailStatus(taskConfig.getUserId(), taskConfig.getSessionId());

        FakerRunStatus status = statusDTO.getStatus();
        if (status.equals(FakerRunStatus.COMPLETE)) {
            this.finishTask(null);
            return;
        }

        if (this.isInterrupted()) {
            if (this.isPause()) {
                this.fakerService.pause(taskConfig.getUserId(), taskConfig.getSessionId());
                this.updateMessage("interrupt by Pause");
            } else {
                this.fakerService.close(taskConfig.getUserId(), taskConfig.getSessionId());
                this.updateMessage("interrupt by Cancel");
            }

            this.updateProcessAndMessage(taskConfig);
            this.notifyAsyncEvent();
            this.finishTask(null);
            return;
        }

        if (retryCnt == 0 && status.equals(FakerRunStatus.PAUSE)) {
            this.fakerService.resume(taskConfig.getUserId(), taskConfig.getSessionId());
        }

        this.updateProcessAndMessage(taskConfig);
        this.notifyAsyncEvent();
        this.delayTask();
    }

    protected void updateProcessAndMessage(FakerAsyncTaskConfig taskConfig) {
        try {
            FakerStatusDTO statusDTO = this.fakerService.tailStatus(taskConfig.getUserId(), taskConfig.getSessionId());
            if (statusDTO == null) {
                return;
            }

            if (statusDTO.isUseProgress()) {
                this.updateMessage(statusDTO.getMessage());
                this.updateProcess(statusDTO.getCurValue(), statusDTO.getMaxValue());
            } else {
                this.updateMessage(statusDTO.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void delayTask() {
        if (this.dmConfig.getDmMode() == DmMode.desktop) {
            this.delayTask(500, TimeUnit.MILLISECONDS);
        } else {
            this.delayTask(1, TimeUnit.SECONDS);
        }
    }
}
