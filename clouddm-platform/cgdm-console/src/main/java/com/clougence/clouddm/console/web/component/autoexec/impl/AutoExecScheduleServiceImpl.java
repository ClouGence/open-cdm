package com.clougence.clouddm.console.web.component.autoexec.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecManager;
import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.AutoExecTaskStatus;
import com.clougence.clouddm.console.web.dal.enumeration.DmLogDependBizType;
import com.clougence.clouddm.console.web.dal.enumeration.Loglevel;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecJobMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecTaskMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmBizLogMapper;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecTaskDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmBizLogDO;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.notify.DmWorkerRegisterNotify;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecScheduleServiceImpl implements UnifiedPostConstruct, DmWorkerRegisterNotify {

    @Resource
    private DmConsoleConfig             dmConfig;
    @Resource
    private DmAutoExecJobMapper         jobMapper;
    @Resource
    private DmAutoExecTaskMapper        taskMapper;
    @Resource
    private DmBizLogMapper              dmBizLogMapper;
    @Resource
    private AutoExecManager             autoExecManager;

    private ThreadPoolExecutor          threadPoolExecutor;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private Set<Long>                   taskInQueueSet;
    private final AtomicBoolean         inited = new AtomicBoolean();

    @Override
    public void init() {
        if (dmConfig.getDmMode() == DmMode.desktop || !inited.compareAndSet(false, true)) {
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
            this.jobMapper.updateOverOutJob(date);
        } catch (Exception e) {
            log.error("updateOverOutJob failed, msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    private void scanPendingJob() {
        Date date = new Date();
        date = new Date(date.getTime() - 5 * 1000);

        try {
            List<Long> doList = this.jobMapper.listUnFinishJobIdList(date);

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
        List<DmAutoExecJobDO> jobList = this.jobMapper.queryErrorJob(wsn);
        for (DmAutoExecJobDO jobDO : jobList) {
            DmAutoExecTaskDO execTaskDO = this.taskMapper.queryOneByJobIdAndStatus(jobDO.getId(), AutoExecTaskStatus.EXECUTING);
            if (execTaskDO == null) {
                continue;
            }

            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_PAUSE_BY_WORKER_RESTART.name(), execTaskDO.getExecOrder());
            DmBizLogDO logDO = new DmBizLogDO(Loglevel.ERROR, message, DmLogDependBizType.AUTO_EXEC_JOB, jobDO.getBizId());
            this.dmBizLogMapper.insert(logDO);
        }

        this.jobMapper.updateWorkerErrorJob(wsn);
        this.jobMapper.updateWorkerWaitExecuteJob(wsn);
    }
}
