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
package com.clougence.clouddm.console.web.component.autoexec.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecManager;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.notify.DmWorkerRegisterNotify;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecTaskStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoTaskDO;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.model.monitor.Loglevel;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecScheduleServiceImpl implements UnifiedPostConstruct, DmWorkerRegisterNotify {

    @Resource
    private MonitorDal                  monitorDal;
    @Resource
    private ExecutionDal                executionDal;
    @Resource
    private DmConsoleConfig             dmConfig;
    @Resource
    private AutoExecManager             autoExecManager;

    private ThreadPoolExecutor          threadPoolExecutor;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private Set<Long>                   taskInQueueSet;
    private final AtomicBoolean         inited = new AtomicBoolean();

    @Override
    public void init() {
        if (!inited.compareAndSet(false, true)) {
            return;
        }

        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(this.dmConfig.getAsyncTaskQueueSize());
        ThreadFactory threadFactory = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "AutoExec-job-%s");
        // if queue is full, ignore the latest additions
        this.threadPoolExecutor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, queue, threadFactory, new ThreadPoolExecutor.AbortPolicy());

        this.taskInQueueSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2, threadFactory);
        this.scheduledThreadPoolExecutor.scheduleWithFixedDelay(this::scanPendingJob, 5, 5, TimeUnit.SECONDS);
        this.scheduledThreadPoolExecutor.scheduleWithFixedDelay(this::updateOverOutJob, 0, 1, TimeUnit.MINUTES);
        log.info("TicketTaskScheduleServiceImpl started");
    }

    @Override
    public void stop() {

    }

    private void updateOverOutJob() {
        try {
            Date date = new Date(new Date().getTime() - 5 * 60 * 1000);
            this.executionDal.autoJobMapper().updateOverOutJob(date);
        } catch (Exception e) {
            log.error("updateOverOutJob failed, msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    private void scanPendingJob() {
        Date date = new Date();
        date = new Date(date.getTime() - 5 * 1000);

        try {
            List<Long> doList = this.executionDal.autoJobMapper().listUnFinishJobIdList(date);

            // schedule task
            for (Long id : doList) {
                submitTask(id);
            }
        } catch (Exception e) {
            log.warn("scan and submit failed,msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    private void submitTask(Long id) {
        try {
            // is running or on queue， avoid repeat ticket task
            if (!this.taskInQueueSet.add(id)) {
                return;
            }
            threadPoolExecutor.execute(() -> {
                try {
                    autoExecManager.dispatchJob(id);
                } finally {
                    this.taskInQueueSet.remove(id);
                }
            });
        } catch (RejectedExecutionException e) {
            // queue full
            log.warn("reject job id:" + id + ", msg:" + ExceptionUtils.getRootCauseMessage(e));
            this.taskInQueueSet.remove(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void notifyRegister(String wsn) {
        List<DmExecAutoJobDO> jobList = this.executionDal.autoJobMapper().queryErrorJob(wsn);
        for (DmExecAutoJobDO jobDO : jobList) {
            DmExecAutoTaskDO execTaskDO = this.executionDal.autoTaskMapper().queryOneByJobIdAndStatus(jobDO.getId(), AutoExecTaskStatus.EXECUTING);
            if (execTaskDO == null) {
                continue;
            }

            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_PAUSE_BY_WORKER_RESTART.name(), execTaskDO.getExecOrder());
            DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.ERROR, message, LogDependBizType.AUTO_EXEC_JOB, jobDO.getBizId());
            this.monitorDal.bizLogMapper().insert(logDO);
        }

        this.executionDal.autoJobMapper().updateWorkerErrorJob(wsn);
        this.executionDal.autoJobMapper().updateWorkerWaitExecuteJob(wsn);
    }
}
