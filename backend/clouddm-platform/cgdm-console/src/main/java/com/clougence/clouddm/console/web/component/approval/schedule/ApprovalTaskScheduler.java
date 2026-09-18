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
package com.clougence.clouddm.console.web.component.approval.schedule;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.service.datasource.DmDsWebService;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStatus;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalType;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalTaskScheduler {
    private static final int            THREAD_COUNT_MULTIPLIER = 2;
    private static final int            QUEUE_SIZE_MULTIPLIER   = 3;
    private static final int            SCAN_BATCH_SIZE         = 20;

    private final ApprovalScanCursor    activeScan              = new ApprovalScanCursor(false, TimeUnit.SECONDS.toMillis(5));
    private final ApprovalScanCursor    idleScan                = new ApprovalScanCursor(true, TimeUnit.SECONDS.toMillis(60));

    @Resource
    private ApprovalDal                 approvalDal;
    @Resource
    private ApprovalFlowService         approvalFlowService;
    @Resource
    private ApprovalTaskProcessor       taskProcessor;
    @Resource
    private DmDsWebService              dsService;
    @Resource
    private ApprovalProviderServiceImpl approvalProviderServiceImpl;
    @Resource
    private ApplicationContext          applicationContext;

    private ThreadPoolExecutor          threadPoolExecutor;
    private Thread                      scheduleWorkThread;
    private Set<Long>                   taskInQueueSet;
    private Set<Long>                   controlTaskInQueueSet;

    public void start() {
        int threadCount = Runtime.getRuntime().availableProcessors() * THREAD_COUNT_MULTIPLIER;
        this.threadPoolExecutor = createExecutor(threadCount);
        ClassLoader classLoader = this.applicationContext.getClassLoader();
        this.taskInQueueSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.controlTaskInQueueSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.scheduleWorkThread = ThreadUtils.daemonThread(classLoader, this::loopSchedule);
        this.scheduleWorkThread.setName("TicketTask-Dispatcher");
        this.scheduleWorkThread.start();
        log.info("TicketTaskScheduleServiceImpl started");
    }

    private void loopSchedule() {
        while (true) {
            try {
                doSchedule(this.activeScan);
                doSchedule(this.idleScan);
            } catch (Throwable e) {
                log.error("Approval scan failed", e);
            }
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            if (!ThreadUtils.safeSleep(1000)) {
                return;
            }
        }
    }

    private void doSchedule(ApprovalScanCursor scan) {
        if (System.currentTimeMillis() < scan.getNextScanTime()) {
            return;
        }
        if (scan.getUpperId() == 0) {
            // A finite sweep lets older tickets run again even while new tickets are created.
            scan.setUpperId(this.approvalDal.approvalMapper().queryScheduleUpperId());
        }
        List<Long> ticketList = this.approvalDal.approvalMapper().listScheduleTicketIds(scan.getAfterId(), scan.getUpperId(), scan.isIdle(), SCAN_BATCH_SIZE);
        for (Long ticketId : ticketList) {
            if (!this.taskInQueueSet.contains(ticketId) && !trySchedule(ticketId)) {
                // Resume at the first unsubmitted ticket after the executor has capacity.
                return;
            }
            scan.setAfterId(ticketId);
        }
        if (ticketList.size() < SCAN_BATCH_SIZE || scan.getAfterId() == scan.getUpperId()) {
            scan.setAfterId(0);
            scan.setUpperId(0);
            scan.setNextScanTime(System.currentTimeMillis() + scan.getIntervalMillis());
        }
    }

    public boolean trySchedule(Long approvalId) {
        ThreadPoolExecutor executor = this.threadPoolExecutor;
        if (executor == null || this.taskInQueueSet == null) {
            return false;
        }
        try {
            // is running or on queue， avoid repeat ticket task
            if (!this.taskInQueueSet.add(approvalId)) {
                return false;
            }
            executor.execute(() -> {
                try {
                    runApproval(approvalId);
                } catch (Exception e) {
                    log.error("Approval task failed, ticketId={}", approvalId, e);
                } finally {
                    this.taskInQueueSet.remove(approvalId);
                }
            });
            return true;
        } catch (RejectedExecutionException e) {
            // queue full
            this.taskInQueueSet.remove(approvalId);
            return false;
        }
    }

    public boolean submitControlTask(Long approvalId, Runnable task) {
        ThreadPoolExecutor executor = this.threadPoolExecutor;
        if (executor == null || this.controlTaskInQueueSet == null) {
            return false;
        }
        if (!this.controlTaskInQueueSet.add(approvalId)) {
            return false;
        }
        try {
            executor.execute(() -> {
                try {
                    task.run();
                } finally {
                    this.controlTaskInQueueSet.remove(approvalId);
                }
            });
            return true;
        } catch (RejectedExecutionException e) {
            this.controlTaskInQueueSet.remove(approvalId);
            return false;
        }
    }

    private void submitTask(Runnable task) {
        ThreadPoolExecutor executor = this.threadPoolExecutor;
        if (executor == null) {
            return;
        }
        try {
            executor.execute(task);
        } catch (RejectedExecutionException e) {
            // The PRE_INIT activity remains INIT and is submitted again by the next scheduler scan.
        }
    }

    private ThreadPoolExecutor createExecutor(int threadCount) {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(threadCount * QUEUE_SIZE_MULTIPLIER);
        ThreadFactory threadFactory = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "Ticket-task-%s");
        return new ThreadPoolExecutor(threadCount, threadCount, 1, TimeUnit.MINUTES, queue, threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    private void runApproval(Long approvalId) {
        DmApprovalDO approvalDO = this.approvalDal.approvalMapper().queryScheduleInfo(approvalId);
        if (approvalDO == null || ApprovalStatus.isEndStatus(approvalDO.getTicketStatus())) {
            return;
        }
        if (approvalDO.getTicketStatus() == ApprovalStatus.EXEC_FAIL
            || (approvalDO.getTicketStatus() == ApprovalStatus.WAIT_APPROVAL && approvalDO.getApproType() == ApprovalType.Internal)) {
            // Idle checks do not need the potentially large SQL payload.
            this.approvalCheck(approvalDO, approvalDO.getPrimaryUid());
            return;
        }
        approvalDO = this.approvalDal.approvalMapper().queryById(approvalId);
        if (approvalDO == null) {
            return;
        }
        String puid = approvalDO.getPrimaryUid();
        DmApprovalDO afterCheck = this.approvalCheck(approvalDO, puid);
        if (afterCheck == null) {
            return;
        }

        switch (afterCheck.getTicketStatus()) {
            case PRE_INIT_WAIT: {
                try {
                    if (this.taskProcessor.preparePreInit(afterCheck.getId())) {
                        DmApprovalDO ado = this.approvalDal.approvalMapper().queryById(afterCheck.getId());
                        this.taskProcessor.submitPreInitChildren(ado, this::submitTask);
                    }
                } catch (Exception e) {
                    Throwable rootException = ExceptionUtils.getRootCause(e);
                    log.error("processExplain failed msg:" + rootException.getMessage(), rootException);
                    String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_EXPLAIN_FAILED_MESSAGE.name()) + rootException.getMessage();
                    this.approvalFlowService.failTicket(afterCheck.getId(), message, puid);
                }
                break;
            }
            case PRE_INIT_RUN: {
                try {
                    this.taskProcessor.submitPreInitChildren(afterCheck, this::submitTask);
                    this.taskProcessor.processPreInitRun(afterCheck.getId());
                } catch (Exception e) {
                    Throwable rootException = ExceptionUtils.getRootCause(e);
                    log.error("processExplain check failed msg:" + rootException.getMessage(), rootException);
                    String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_EXPLAIN_FAILED_MESSAGE.name()) + rootException.getMessage();
                    this.approvalFlowService.failTicket(afterCheck.getId(), message, puid);
                }
                break;
            }
            case WAIT_APPROVAL: {
                try {
                    this.taskProcessor.processWaitApproval(afterCheck);
                } catch (ThirdPartyApiException e) {
                    if (e.getErrorType() == ThirdPartyApiErrorType.APPROVAL_TEMPLATE_NOT_EXISTS) {
                        this.approvalFlowService.failTicket(approvalId, DmI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs()), approvalDO.getPrimaryUid());
                        this.approvalDal.templateMapper().deleteByPrimaryUid(approvalDO.getPrimaryUid(), approvalDO.getApproType());
                    } else {
                        this.approvalFlowService.failTicket(approvalDO.getId(), DmI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs()), puid);
                    }
                    log.error(e.getMessage());
                } catch (Exception e) {
                    this.approvalFlowService
                        .failTicket(approvalDO.getId(), DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), approvalDO.getApproType().name()), puid);
                    log.error("processWaitApproval failed msg:" + e.getMessage(), e);
                }
                break;
            }
            case WAIT_EXEC: {
                try {
                    this.taskProcessor.processWaitExec(afterCheck);
                } catch (Exception e) {
                    log.error("processWaitApproval failed msg:" + e.getMessage(), e);
                }
                break;
            }
            case WAIT_CONFIRM: {
                this.taskProcessor.processWaitConfirm(afterCheck);
                break;
            }
            case RUNNING:
            case EXEC_PAUSE:
            case FAILED: {
                this.taskProcessor.processRunningCheck(afterCheck);
                break;
            }
            case REJECTED: {
                this.taskProcessor.processReject(afterCheck);
                break;
            }
            case CANCELED: {
                this.taskProcessor.processCanceled(afterCheck);
                break;
            }
            case EXEC_FAIL:
            case CLOSED:
            case FINISHED: {
                break;
            }
            default:
                String msg = "processWorker ticket status '" + afterCheck.getTicketStatus() + "' unsupport.";
                log.error(msg);
                throw new IllegalStateException(msg);
        }
    }

    //
    private DmApprovalDO approvalCheck(DmApprovalDO ticketDO, String puid) {
        if (ApprovalStatus.isEndStatus(ticketDO.getTicketStatus())) {
            return null;
        }
        DmDsDO dataSourceDO = this.dsService.queryById(ticketDO.getBindDsId());
        if ((dataSourceDO == null || dataSourceDO.getLifeCycleState() == LifeCycleState.DELETED) && ticketDO.getApproBiz() != ApprovalBiz.DATA_SOURCE_AUTH) {
            // ds is deleted
            this.approvalFlowService.failTicket(ticketDO.getId(), DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_DS_IS_DELETE.name()), puid);
            return null;
        }

        boolean externalApproval = ticketDO.getApproType() != ApprovalType.Internal;
        boolean waitingForApproval = ticketDO.getTicketStatus() == ApprovalStatus.PRE_INIT_WAIT || //
                                     ticketDO.getTicketStatus() == ApprovalStatus.PRE_INIT_RUN ||  //
                                     ticketDO.getTicketStatus() == ApprovalStatus.WAIT_APPROVAL;
        if (externalApproval && waitingForApproval) {
            if (!this.approvalProviderServiceImpl.checkEnableApproval(puid, ticketDO.getApproType().getProviderType())) {
                String failMsg = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), ticketDO.getApproType().name());
                this.approvalFlowService.failTicket(ticketDO.getId(), failMsg, puid);
                return null;
            }
        }

        return ticketDO;
    }
}
