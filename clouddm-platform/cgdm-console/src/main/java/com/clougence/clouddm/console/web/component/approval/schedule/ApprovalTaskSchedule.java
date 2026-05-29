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
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStatus;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalType;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalTaskSchedule {
    @Resource
    private DmConsoleConfig             consoleConfig;
    @Resource
    private ApprovalDal                 approvalDal;
    @Resource
    private ApprovalFlowService         approvalFlowService;
    @Resource
    private ApprovalTaskScheduleProcess scheduleProcess;
    @Resource
    private RdpDsService                rdpDsService;
    @Resource
    private ApprovalProviderServiceImpl approvalProviderServiceImpl;
    @Resource
    private ApplicationContext          applicationContext;

    ThreadPoolExecutor                  threadPoolExecutor;
    private Thread                      scheduleWorkThread;
    private Set<Long>                   taskInQueueSet;

    public void start() {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(this.consoleConfig.getRdpAsyncTaskQueueSize());
        ThreadFactory threadFactory = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "Ticket-task-%s");
        // if queue is full, ignore the latest additions
        this.threadPoolExecutor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, queue, threadFactory, new ThreadPoolExecutor.AbortPolicy());
        ClassLoader classLoader = this.applicationContext.getClassLoader();
        this.taskInQueueSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.scheduleWorkThread = ThreadUtils.daemonThread(classLoader, this::loopSchedule);
        this.scheduleWorkThread.setName("TicketTask-Dispatcher");
        this.scheduleWorkThread.start();
        log.info("TicketTaskScheduleServiceImpl started");
    }

    private void loopSchedule() {
        while (true) {
            try {
                doSchedule();
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("[TicketTask] thread exit, (" + Thread.currentThread().getName() + ")");
                    return;
                }
                ThreadUtils.safeSleep(1000);
            } catch (Throwable e) {
                log.error("[TicketTask] error " + e.getMessage(), e);
            }
        }
    }

    private void doSchedule() {

        do {
            Date date = new Date();
            date = new Date(date.getTime() - 5 * 1000);

            List<Long> doList = this.approvalDal.approvalMapper().listUnFinishTicketIdList(date);

            // there is nothing to do.
            if (doList.isEmpty()) {
                ThreadUtils.sleep(5, TimeUnit.SECONDS);
                return;
            }

            log.info("[Rdp TicketTask] have " + doList.size() + " task to submit.");

            // schedule task
            for (Long id : doList) {
                submitTask(id);
            }
        } while (true);
    }

    private void submitTask(Long id) {
        try {
            // is running or on queue， avoid repeat ticket task
            if (!this.taskInQueueSet.add(id)) {
                return;
            }
            this.approvalDal.approvalMapper().updateModified(id);
            this.threadPoolExecutor.submit(() -> {
                try {
                    run(id);
                } finally {
                    this.taskInQueueSet.remove(id);
                }
            });
        } catch (RejectedExecutionException e) {
            // queue full
            this.taskInQueueSet.remove(id);
        }
    }

    private void run(Long ticketId) {
        DmApprovalDO approvalDO = this.approvalDal.approvalMapper().queryById(ticketId);
        String puid = approvalDO.getPrimaryUid();
        String uid = approvalDO.getOwnerUid();
        DmApprovalDO afterCheck = this.processCheck(approvalDO, puid);
        if (afterCheck == null) {
            //            this.finishTask(FINISH_MSG);
            return;
        }

        this.approvalDal.approvalMapper().updateModified(afterCheck.getId());

        switch (afterCheck.getTicketStatus()) {
            case PRE_INIT: {
                try {
                    this.scheduleProcess.processPreInit(afterCheck);
                    // this.delayTask(2, TimeUnit.SECONDS);
                } catch (Exception e) {
                    boolean isRpcTimeout = e instanceof RemoteInvokeTimeoutException;
                    if (isRpcTimeout) {
                        // rsocket error need retry ignore
                    } else {
                        Throwable rootException = ExceptionUtils.getRootCause(e);
                        log.error("processExplain failed msg:" + rootException.getMessage(), rootException);
                        String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_EXPLAIN_FAILED_MESSAGE.name()) + rootException.getMessage();
                        this.approvalFlowService.closeTicket(afterCheck.getId(), message, puid);
                        // this.finishTask(FINISH_MSG);
                    }
                }
                break;
            }
            case WAIT_APPROVAL: {
                try {
                    this.scheduleProcess.processWaitApproval(afterCheck);
                    this.scheduleProcess.processApprovalPerson(puid, uid, afterCheck);
                } catch (ThirdPartyApiException e) {
                    if (e.getErrorType() == ThirdPartyApiErrorType.APPROVAL_TEMPLATE_NOT_EXISTS) {
                        this.approvalFlowService.failTicket(ticketId, DmI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs()), approvalDO.getPrimaryUid());
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
                    this.scheduleProcess.processWaitExec(afterCheck);
                } catch (Exception e) {
                    log.error("processWaitApproval failed msg:" + e.getMessage(), e);
                }
                break;
            }
            case WAIT_CONFIRM: {
                this.scheduleProcess.processWaitConfirm(afterCheck);
                break;
            }
            case RUNNING:
            case EXEC_PAUSE:
            case FAILED: {
                this.scheduleProcess.processRunningCheck(afterCheck);
                break;
            }
            case REJECTED: {
                this.scheduleProcess.processReject(afterCheck);
                break;
            }
            case CANCELED: {
                this.scheduleProcess.processCanceled(afterCheck);
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

    private DmApprovalDO processCheck(DmApprovalDO ticketDO, String puid) {
        DmDsDO dataSourceDO = this.rdpDsService.queryById(ticketDO.getBindDsId());
        if ((dataSourceDO == null || dataSourceDO.getLifeCycleState() == LifeCycleState.DELETED) && ticketDO.getApproBiz() != ApprovalBiz.DATA_SOURCE_AUTH) {
            // ds is deleted
            this.approvalFlowService.failTicket(ticketDO.getId(), DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_DS_IS_DELETE.name()), puid);
            return null;
        }

        if (ticketDO.getApproType() != ApprovalType.Internal
            && (ticketDO.getTicketStatus() == ApprovalStatus.PRE_INIT || ticketDO.getTicketStatus() == ApprovalStatus.WAIT_APPROVAL)) {
            if (!this.approvalProviderServiceImpl.checkEnableApproval(puid, ticketDO.getApproType().getProviderType())) {
                String failMsg = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), ticketDO.getApproType().name());
                this.approvalFlowService.failTicket(ticketDO.getId(), failMsg, puid);
                return null;
            }
        }

        if (ApprovalStatus.isEndStatus(ticketDO.getTicketStatus())) {
            return null;
        }

        return ticketDO;
    }

}
