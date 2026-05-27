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
package com.clougence.clouddm.console.web.component.project.impl;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.component.project.action.*;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.ProjectDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.project.DmProjectChangeDO;
import com.clougence.clouddm.platform.dal.model.project.ProjectChangeStatus;
import com.clougence.clouddm.platform.dal.model.project.ProjectChangeStep;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeScheduleServiceImpl implements UnifiedPostConstruct {

    @Resource
    private SystemDal                            systemDal;
    @Resource
    private ProjectDal                           projectDal;
    @Resource
    private DmConsoleConfig                      dmConfig;
    @Resource
    private ApplicationContext                   applicationContext;
    @Resource
    protected ImSenderService                    senderService;

    private Set<Long>                            taskInQueueSet;
    private ThreadPoolExecutor                   threadPoolExecutor;
    private ScheduledThreadPoolExecutor          scheduledThreadPoolExecutor;
    private final AtomicBoolean                  inited = new AtomicBoolean();
    private Map<ProjectChangeStep, ChangeAction> actionMap;

    @Override
    public void init() {
        if (!inited.compareAndSet(false, true)) {
            return;
        }
        this.taskInQueueSet = new HashSet<>();

        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(this.dmConfig.getAsyncTaskQueueSize());
        ThreadFactory workerTF = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "change-worker-%s");
        // if queue is full, ignore the latest additions
        this.threadPoolExecutor = new ThreadPoolExecutor(3, 10, 1, TimeUnit.MINUTES, queue, workerTF, new ThreadPoolExecutor.AbortPolicy());

        ThreadFactory scheduledTF = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "change-scheduled-%s");
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, scheduledTF);
        this.scheduledThreadPoolExecutor.scheduleWithFixedDelay(this::scanPendingJob, 5, 5, TimeUnit.SECONDS);
        log.info("changeScheduleService started");

        this.actionMap = new HashMap<>();
        this.actionMap.put(ProjectChangeStep.INIT, this.applicationContext.getBean(ChangeActionForInit.class));
        this.actionMap.put(ProjectChangeStep.CHECK, this.applicationContext.getBean(ChangeActionForCheck.class));
        this.actionMap.put(ProjectChangeStep.APPROVAL, this.applicationContext.getBean(ChangeActionForApproval.class));
        this.actionMap.put(ProjectChangeStep.EXECUTE, this.applicationContext.getBean(ChangeActionForExecute.class));
        this.actionMap.put(ProjectChangeStep.FINISH, this.applicationContext.getBean(ChangeActionForFinish.class));
        this.actionMap.put(ProjectChangeStep.INIT_SNAPSHOT, this.applicationContext.getBean(ChangeActionForInitSnapshot.class));
    }

    @Override
    public void stop() {

    }

    private void scanPendingJob() {
        Date date = new Date();
        date = new Date(date.getTime() - 5 * 1000);

        try {
            List<DmProjectChangeDO> changeList = this.projectDal.changeMapper().queryReadyChangeListByDate(date, 50);
            for (DmProjectChangeDO change : changeList) {
                submitTask(change);
            }
        } catch (Exception e) {
            log.warn("changeSchedule scanPendingJob and submit failed,msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    private void submitTask(DmProjectChangeDO change) {
        Long changeId = change.getId();
        try {
            // is running or on queue， avoid repeat ticket task
            if (this.taskInQueueSet.contains(changeId)) {
                return;
            }

            int res = this.projectDal.changeMapper().assignReadyChange(changeId, change.getVersion());
            if (res == 0) {
                return;
            }

            this.taskInQueueSet.add(changeId);
            this.threadPoolExecutor.execute(() -> {
                try {
                    this.doChange(change);
                } finally {
                    this.taskInQueueSet.remove(changeId);
                }
            });
        } catch (RejectedExecutionException e) {
            log.info("changeSchedule reject changeId:" + changeId + ",queue full.");
            this.taskInQueueSet.remove(changeId);
        }
    }

    private void doChange(DmProjectChangeDO change) {
        ProjectChangeStep step = change.getCurrentStep();
        try {
            this.actionMap.get(step).doAction(change);
        } catch (Throwable e) {
            log.error("changeAction[" + change.getId() + "] " + step + " failed " + e.getMessage(), e);
            DmProjectChangeDO changeDO = this.projectDal.changeMapper().queryChangeById(change.getOwnerUid(), change.getId());
            this.projectDal.changeMapper().increTryTimes(change.getId(), changeDO.getVersion(), e.getMessage());

            int maxFailedTimes = maxFailedTimes(change.getOwnerUid());
            if (change.getTryTimes() >= maxFailedTimes) {
                String language = this.senderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
                Locale locale = I18nUtils.getLocale(language);

                String errorMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SCM_INIT_MULTIPLE_RETRIES_ERROR.name(), locale, change.getChangeName(), maxFailedTimes);
                this.senderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, errorMsg);
                this.projectDal.changeMapper().updateStatusTo(change.getId(), changeDO.getVersion() + 1, ProjectChangeStatus.FAILED, errorMsg);
            }
        }
    }

    private int maxFailedTimes(String ownerUid) {
        DmSysUserConfDO currentConfig = this.systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, UserDefinedConfig.Fields.scmMaxFailedTimes);
        if (currentConfig == null || StringUtils.isBlank(currentConfig.getConfigValue())) {
            return 3;
        } else {
            return Integer.parseInt(currentConfig.getConfigValue());
        }
    }
}
