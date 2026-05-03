package com.clougence.rdp.component.ticket;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.rdp.component.ticket.impl.RdpApproServiceImpl;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.dal.enumeration.LifeCycleState;
import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.enumeration.RdpTicketStatus;
import com.clougence.rdp.dal.mapper.RdpCacheApproTemplateMapper;
import com.clougence.rdp.dal.mapper.RdpTicketMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpTicketDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.exception.RemoteInvokeTimeoutException;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RdpApprovalTaskSchedule {

    @Resource
    private RdpConsoleConfig               dmConfig;
    @Resource
    private RdpTicketMapper                rdpTicketMapper;

    @Resource
    private RdpTicketService               rdpTicketService;

    @Resource
    private RdpCacheApproTemplateMapper    templateMapper;

    @Resource
    private RdpDsService                   rdpDsService;

    @Resource
    private RdpApproServiceImpl            rdpApproServiceImpl;

    @Resource
    private RdpApprovalTaskScheduleProcess ticketProcess;

    @Resource
    private ApplicationContext             applicationContext;

    ThreadPoolExecutor                     threadPoolExecutor;

    private Thread                         scheduleWorkThread;

    private Set<Long>                      taskInQueueSet;

    public void start() {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(this.dmConfig.getAsyncTaskQueueSize());
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

            List<Long> doList = this.rdpTicketMapper.listUnFinishTicketIdList(date);

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
            this.rdpTicketMapper.updateModified(id);
            threadPoolExecutor.submit(() -> {
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
        RdpTicketDO rdpTicketDO = this.rdpTicketMapper.queryById(ticketId);
        String puid = rdpTicketDO.getPrimaryUid();
        String uid = rdpTicketDO.getOwnerUid();
        RdpTicketDO afterCheck = this.processCheck(rdpTicketDO, puid);
        if (afterCheck == null) {
            //            this.finishTask(FINISH_MSG);
            return;
        }

        this.rdpTicketMapper.updateModified(afterCheck.getId());

        switch (afterCheck.getTicketStatus()) {
            case PRE_INIT: {
                try {
                    ticketProcess.processPreInit(afterCheck);
                    //                    this.delayTask(2, TimeUnit.SECONDS);
                } catch (Exception e) {
                    boolean isRpcTimeout = e instanceof RemoteInvokeTimeoutException;
                    if (isRpcTimeout) {
                        // rsocket error need retry ignore
                    } else {
                        Throwable rootException = ExceptionUtils.getRootCause(e);
                        log.error("processExplain failed msg:" + rootException.getMessage(), rootException);
                        String message = RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_EXPLAIN_FAILED_MESSAGE.name()) + rootException.getMessage();
                        this.rdpTicketService.closeTicket(afterCheck.getId(), message, puid);
                        //                        this.finishTask(FINISH_MSG);
                    }
                }
                break;
            }
            case WAIT_APPROVAL: {
                try {
                    ticketProcess.processWaitApproval(afterCheck);
                    ticketProcess.processApprovalPerson(puid, uid, afterCheck);
                } catch (ThirdPartyApiException e) {
                    if (e.getErrorType() == ThirdPartyApiErrorType.APPROVAL_TEMPLATE_NOT_EXISTS) {
                        rdpTicketService.failTicket(ticketId, RdpI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs()), rdpTicketDO.getPrimaryUid());
                        templateMapper.deleteByPrimaryUid(rdpTicketDO.getPrimaryUid(), rdpTicketDO.getApproType());
                    } else {
                        this.rdpTicketService.failTicket(rdpTicketDO.getId(), RdpI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs()), puid);
                    }
                    log.error(e.getMessage());
                } catch (Exception e) {
                    this.rdpTicketService
                        .failTicket(rdpTicketDO.getId(), RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), rdpTicketDO.getApproType().name()), puid);
                    log.error("processWaitApproval failed msg:" + e.getMessage(), e);
                }
                break;
            }
            case WAIT_EXEC: {
                try {
                    ticketProcess.processWaitExec(afterCheck);
                } catch (Exception e) {
                    log.error("processWaitApproval failed msg:" + e.getMessage(), e);
                }
                break;
            }
            case WAIT_CONFIRM: {
                ticketProcess.processWaitConfirm(afterCheck);
                break;
            }
            case RUNNING:
            case EXEC_PAUSE:
            case FAILED: {
                ticketProcess.processRunningCheck(afterCheck);
                break;
            }
            case REJECTED: {
                ticketProcess.processReject(afterCheck);
                break;
            }
            case CANCELED: {
                ticketProcess.processCanceled(afterCheck);
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

    private RdpTicketDO processCheck(RdpTicketDO ticketDO, String puid) {
        RdpDataSourceDO dataSourceDO = this.rdpDsService.queryById(ticketDO.getBindDsId());
        if ((dataSourceDO == null || dataSourceDO.getLifeCycleState() == LifeCycleState.DELETED) && ticketDO.getApproBiz() != RdpApprovalBiz.DATA_SOURCE_AUTH) {
            // ds is deleted
            this.rdpTicketService.failTicket(ticketDO.getId(), RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_DS_IS_DELETE.name()), puid);
            return null;
        }

        if (ticketDO.getApproType() != RdpApprovalType.Internal
            && (ticketDO.getTicketStatus() == RdpTicketStatus.PRE_INIT || ticketDO.getTicketStatus() == RdpTicketStatus.WAIT_APPROVAL)) {
            if (!this.rdpApproServiceImpl.checkEnableApproval(puid, ticketDO.getApproType().getProviderType())) {
                String failMsg = RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), ticketDO.getApproType().name());
                this.rdpTicketService.failTicket(ticketDO.getId(), failMsg, puid);
                return null;
            }
        }

        if (RdpTicketStatus.isEndStatus(ticketDO.getTicketStatus())) {
            return null;
        }

        return ticketDO;
    }

}
