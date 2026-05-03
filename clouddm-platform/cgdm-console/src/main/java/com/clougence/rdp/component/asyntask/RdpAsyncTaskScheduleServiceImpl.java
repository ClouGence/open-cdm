package com.clougence.rdp.component.asyntask;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.rdp.dal.enumeration.RdpAsyncTaskStatus;
import com.clougence.rdp.dal.mapper.RdpAsyncTaskMapper;
import com.clougence.rdp.dal.model.RdpAsyncTaskDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.util.NamedThreadFactory;
import com.clougence.rdp.util.RdpHostUtil;
import com.clougence.rdp.util.RdpTimerUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;
import com.clougence.utils.future.CgFuture;
import com.clougence.utils.future.CgFutureObj;

import lombok.extern.slf4j.Slf4j;

/**
 * Low-latency task distribution
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-10-09
 */
@Slf4j
@Service
public class RdpAsyncTaskScheduleServiceImpl implements RdpAsyncTaskScheduleService, UnifiedPostConstruct {

    @Resource
    private RdpAsyncTaskMapper      rdpAsyncTaskMapper;
    @Resource
    private RdpConsoleConfig        dmConfig;
    @Resource
    private ApplicationContext      applicationContext;

    private RdpScheduleExecutor     rdpScheduleExecutor;
    private Map<Long, RdpAsyncTask> scheduleTaskMap;
    private AtomicInteger           requestSchedule;
    private Thread                  scheduleWorkThread;

    @Override
    public void init() throws Exception {
        this.rdpAsyncTaskMapper.resetAsyncTaskStatus(getHostIp());
        this.rdpAsyncTaskMapper.resetInitAsyncTaskStatus(getHostIp());
        this.rdpAsyncTaskMapper.resetCancelingAsyncTaskStatus(getHostIp());
        this.rdpAsyncTaskMapper.resetPausingAsyncTaskStatus(getHostIp());
        this.requestSchedule = new AtomicInteger();
        this.scheduleTaskMap = new ConcurrentHashMap<>();

        ClassLoader classLoader = this.applicationContext.getClassLoader();
        ThreadFactory timerTF = ThreadUtils.daemonThreadFactory(classLoader, "AsyncTask-Timer-%s");
        ThreadFactory workerTF = ThreadUtils.daemonThreadFactory(classLoader, "AsyncTask-Process-%s");
        this.rdpScheduleExecutor = new RdpScheduleExecutor(this.dmConfig.getAsyncThreadCount(), timerTF, workerTF);

        this.scheduleWorkThread = ThreadUtils.daemonThread(classLoader, this::loopSchedule);
        this.scheduleWorkThread.setName("AsyncTask-Dispatcher");
        this.scheduleWorkThread.start();

        ScheduledExecutorService verifyCleanExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("AsyncTask-AutoClean", true));
        verifyCleanExecutor.scheduleAtFixedRate(() -> {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, -10);
                rdpAsyncTaskMapper.deleteOldData(calendar.getTime());
                log.info("[Rdp AsyncTask] Async task cleaned.");
            } catch (Throwable e) {
                log.error("[Rdp AsyncTask] Clean async task failed, but ignore. msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }, 2, 10, TimeUnit.MINUTES);
    }

    @Override
    public void stop() {

    }

    private String getHostIp() { return RdpHostUtil.getHostIp(); }

    //-------------------------------------------------------------------------
    //                                                        Process AsyncTask
    //-------------------------------------------------------------------------

    // A new task comes in.
    public void trigger() {
        if (this.requestSchedule == null || this.scheduleWorkThread == null) {
            log.warn("[AsyncTask] trigger() called before init.");
            return;
        }

        this.requestSchedule.incrementAndGet();

        Thread t = this.scheduleWorkThread;
        if (t.getState() == Thread.State.TIMED_WAITING || t.getState() == Thread.State.WAITING) {
            LockSupport.unpark(this.scheduleWorkThread);
        }

        RdpTimerUtils.onTimeout(timeout -> {
            if (this.requestSchedule.get() > 0) {
                if (t.getState() == Thread.State.TIMED_WAITING || t.getState() == Thread.State.WAITING) {
                    LockSupport.unpark(this.scheduleWorkThread);
                }
            }
        }, 1000, TimeUnit.MICROSECONDS);
    }

    //-------------------------------------------------------------------------
    //                                                        Process AsyncTask
    //
    //  INIT   ─>   BLOCK
    //    │           ↑
    //    │           ↓                   (task FastFail or finish run)
    //    ╰─────> WAIT_START ──> RUNNING ──>  FAILURE or COMPLETE
    //                              ↓
    //                            PAUSE
    //                         (task Fail)
    //-------------------------------------------------------------------------

    protected void loopSchedule() {
        MDC.put("module", "async_task");
        while (true) {
            try {
                doSchedule();
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("[AsyncTask] thread exit, (" + Thread.currentThread().getName() + ")");
                    return;
                }
                ThreadUtils.safeSleep(1000);
            } catch (Throwable e) {
                log.error("[AsyncTask] error " + e.getMessage(), e);
            }
        }
    }

    private void doSchedule() {
        // wait 30% empty slots, avoid frequent queries.
        double freeSlotDouble = freeSlot();
        double queueSizeDouble = this.dmConfig.getAsyncTaskQueueSize();
        if ((freeSlotDouble / queueSizeDouble) < 0.7d) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
            return;
        }

        do {
            this.requestSchedule.set(0);// clear request cnt, and query todo task.
            List<RdpAsyncTaskDO> doList = this.rdpAsyncTaskMapper.queryWaitTask(freeSlot(), getHostIp());

            // there is nothing to do.
            if (doList.isEmpty() && this.requestSchedule.get() == 0) {
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
                return;
            }

            // schedule task
            for (RdpAsyncTaskDO t : doList) {
                doScheduleOneTask(t);
            }

            // queue is full.
            if (freeSlot() == 0) {
                log.info(String.format("[AsyncTask] task schedule queue is full, limit is %s", this.dmConfig.getAsyncTaskQueueSize()));
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
                return;
            }
        } while (true);
    }

    private void doScheduleOneTask(RdpAsyncTaskDO t) {
        // check depend task
        if (StringUtils.isNotBlank(t.getDependOnBizId())) {
            RdpAsyncTaskDO depTaskDO = this.rdpAsyncTaskMapper.queryByBiz(t.getDependOnBizId(), t.getDependOnBizType());
            if (depTaskDO.getStatus() != RdpAsyncTaskStatus.COMPLETE) {
                int r = this.rdpAsyncTaskMapper.updateFromWaitTo(t.getId(), RdpAsyncTaskStatus.BLOCK.name(), "The dependent task is not completed.");
                if (r > 0) {
                    depTaskDO.setStatus(RdpAsyncTaskStatus.BLOCK);
                    //RdpGlobalEventBus.triggerRdpAsyncEvent(depTaskDO);
                    log.info(String.format("[AsyncTask] The task [%s]%s update status WAIT_START -> BLOCK", t.getBizType(), t.getBizId()));
                } else {
                    log.warn(String.format("[AsyncTask] The task [%s]%s update status WAIT_START -> BLOCK, failed.", t.getBizType(), t.getBizId()));
                }
                return;
            }
        }

        // schedule [WAIT_START -> RUNNING]
        int res2 = this.rdpAsyncTaskMapper.updateFromWaitTo(t.getId(), RdpAsyncTaskStatus.RUNNING.name(), "start");
        if (res2 <= 0) {
            log.warn(String.format("[AsyncTask] The task [%s]%s update status WAIT_START -> RUNNING failed, maybe have another worker running it.", t.getBizType(), t.getBizId()));
            return;
        } else {
            t.setStatus(RdpAsyncTaskStatus.RUNNING);
            //RdpGlobalEventBus.triggerRdpAsyncEvent(t);
            log.info(String.format("[AsyncTask] The task [%s]%s update status WAIT_START -> RUNNING", t.getBizType(), t.getBizId()));
        }

        RdpAsyncTask theTask = getTask(t);
        CgFutureObj<Object> theFuture = theTask.getFuture();
        theFuture.onFailed(f -> {
            // on failed, [RUNNING -> FAILURE or PAUSE]
            taskFutureCallBackOnFailed(f, theTask);
        }).onCompleted(f -> {
            // on finish, [RUNNING -> COMPLETE]
            taskFutureCallBackOnCompleted(f, theTask);
        }).onCancel(f -> {
            // on cancel, [PAUSING,CANCELING -> PAUSE,CANCEL]
            taskFutureCallBackOnCancel(f, theTask);
        }).onFinal(f -> {
            this.scheduleTaskMap.remove(theTask.getTaskDO().getId());
        });

        log.info(String.format("[AsyncTask] The task [%s]%s submit to run.", t.getBizType(), t.getBizId()));
        this.scheduleTaskMap.put(theTask.getTaskDO().getId(), theTask);
        this.rdpScheduleExecutor.submitTask(theTask, theFuture);
    }

    private void taskFutureCallBackOnFailed(CgFuture<?> f, RdpAsyncTask theTask) {
        RdpAsyncTaskDO t = theTask.getTaskDO();
        String message = "Error: " + f.getCause().getMessage();
        log.error(String.format("[AsyncTask] The task [%s]%s finish, failed: " + message, t.getBizType(), t.getBizId()), f.getCause());

        RdpAsyncTaskStatus toStatus = (theTask.isFastFail()) ? RdpAsyncTaskStatus.FAILURE : RdpAsyncTaskStatus.PAUSE;
        int r = this.rdpAsyncTaskMapper.updateStatusTo(t.getId(), toStatus.name(), message);
        if (r > 0) {
            t.setStatus(toStatus);
            //RdpGlobalEventBus.triggerRdpAsyncEvent(t);
            log.info(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> " + toStatus, t.getBizType(), t.getBizId()));
        } else {
            log.info(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> " + toStatus + ", failed.", t.getBizType(), t.getBizId()));
        }
    }

    private void taskFutureCallBackOnCompleted(CgFuture<?> f, RdpAsyncTask theTask) {
        RdpAsyncTaskDO t = theTask.getTaskDO();
        String result = f.getResult() == null ? "Finish" : f.getResult().toString();
        log.info(String.format("[AsyncTask] The task [%s]%s finish, " + result, t.getBizType(), t.getBizId()), f.getCause());

        int r = this.rdpAsyncTaskMapper.updateStatusTo(t.getId(), RdpAsyncTaskStatus.COMPLETE.name(), result);
        if (r > 0) {
            t.setStatus(RdpAsyncTaskStatus.COMPLETE);
            //RdpGlobalEventBus.triggerRdpAsyncEvent(t);
            log.info(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> COMPLETE", t.getBizType(), t.getBizId()));
        } else {
            log.error(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> COMPLETE, failed.", t.getBizType(), t.getBizId()));
        }

        this.wakeUpDependTask(t);
        this.trigger();
    }

    private void taskFutureCallBackOnCancel(CgFuture<?> f, RdpAsyncTask theTask) {
        RdpAsyncTaskDO t = theTask.getTaskDO();
        RdpInterruptedType rdpInterruptedType = theTask.getInterruptedType();
        log.info(String.format("[AsyncTask] The task [%s]%s cancel by %s.", t.getBizType(), t.getBizId(), rdpInterruptedType.name()), f.getCause());

        RdpAsyncTaskStatus updateTo = rdpInterruptedType == RdpInterruptedType.PAUSE ? RdpAsyncTaskStatus.PAUSE : RdpAsyncTaskStatus.CANCEL;

        int r = this.rdpAsyncTaskMapper.updateStatusTo(t.getId(), updateTo.name(), "by " + rdpInterruptedType.name());
        if (r > 0) {
            t.setStatus(updateTo);
            //RdpGlobalEventBus.triggerRdpAsyncEvent(t);
            log.info(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> %s", t.getBizType(), t.getBizId(), updateTo.name()));
        } else {
            log.error(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> %s, failed.", t.getBizType(), t.getBizId(), updateTo.name()));
        }
    }

    /** BLOCK ─> WAIT_START */
    private void wakeUpDependTask(RdpAsyncTaskDO taskDO) {
        List<RdpAsyncTaskDO> dependTask = this.rdpAsyncTaskMapper.queryDepends(taskDO.getBizId(), taskDO.getBizType());
        if (dependTask.isEmpty()) {
            return;
        }

        List<Long> ids = dependTask.stream().map(RdpAsyncTaskDO::getId).collect(Collectors.toList());
        int r = this.rdpAsyncTaskMapper.batchResumeFromBlock(ids, "depends Task finish.");

        String idsStr = StringUtils.join(dependTask.stream().map(t -> String.format("[%s]%s", t.getBizType(), t.getBizId())).toArray(), ",");
        log.info(String.format("[AsyncTask] Task (%s) update status BLOCK -> WAIT_START, result is " + r, idsStr));

        for (RdpAsyncTaskDO task : dependTask) {
            task.setStatus(RdpAsyncTaskStatus.WAIT_START);
            //RdpGlobalEventBus.triggerRdpAsyncEvent(task);
        }
    }

    private int freeSlot() {
        int cfgMaxQueueSize = this.dmConfig.getAsyncTaskQueueSize();
        int smtQueueSize = this.rdpScheduleExecutor.getQueueSize();
        return cfgMaxQueueSize - smtQueueSize;
    }

    private RdpAsyncTask getTask(RdpAsyncTaskDO taskDO) {
        RdpAsyncTask resultTask;
        String handlerName = taskDO.getHandlerName();
        if (StringUtils.isBlank(handlerName)) {
            log.error(String.format("[AsyncTask] The task [%s]%s handlerName is undefined.", taskDO.getBizType(), taskDO.getBizId()));
            resultTask = new RdpAsyncTask() {

                @Override
                protected void executeTask(int doCnt, String configData) {
                    this.failedTask(new IllegalStateException("No handler defined."));
                }
            };
        } else {
            resultTask = this.applicationContext.getBean(handlerName, RdpAsyncTask.class);
        }

        resultTask.setTaskDO(taskDO);
        return resultTask;
    }

    //-------------------------------------------------------------------------
    //                                                        Manager AsyncTask
    //-------------------------------------------------------------------------

    private boolean cancelSchedule(long asyncTaskId, String reasons, RdpInterruptedType type) {
        RdpAsyncTask asyncTask = this.scheduleTaskMap.get(asyncTaskId);
        if (asyncTask != null) {
            CgFutureObj<Object> f = asyncTask.getFuture();
            if (!f.isDone()) {
                // from running to canceling
                RdpAsyncTaskDO t = asyncTask.getTaskDO();
                RdpAsyncTaskStatus toStatus = type == RdpInterruptedType.PAUSE ? RdpAsyncTaskStatus.PAUSING : RdpAsyncTaskStatus.CANCELING;
                int r = this.rdpAsyncTaskMapper.updateFromRunningTo(asyncTaskId, toStatus.name(), reasons);
                if (r > 0) {
                    t.setStatus(toStatus);
                    t.setStatusMsg(reasons);
                    //RdpGlobalEventBus.triggerRdpAsyncEvent(t);
                    log.info(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> %s", t.getBizType(), t.getBizId(), toStatus));
                } else {
                    log.error(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> %s, failed.", t.getBizType(), t.getBizId(), toStatus));
                    return false;
                }

                // from running to cancel
                asyncTask.setInterrupted(type);
                f.cancel();
            }
        }
        return false;
    }

    @Override
    public void pauseTask(long asyncTaskId, String reasons) {
        if (this.cancelSchedule(asyncTaskId, reasons, RdpInterruptedType.PAUSE)) {
            // [RUNNING -> PAUSE]
            return;
        }

        RdpAsyncTaskDO t = this.rdpAsyncTaskMapper.queryById(asyncTaskId);
        switch (t.getStatus()) {
            case BLOCK:
                // [BLOCK -> CANCEL]
                this.rdpAsyncTaskMapper.updateFromBlockTo(asyncTaskId, RdpAsyncTaskStatus.PAUSE.name(), reasons);
                t.setStatus(RdpAsyncTaskStatus.PAUSE);
                //RdpGlobalEventBus.triggerRdpAsyncEvent(t);
                break;
            case RUNNING:
                throw new IllegalStateException("the pause failed, may be the task running on another console.");
            default:
                break;
        }
    }

    @Override
    public void cancelTask(long asyncTaskId, String reasons) {
        if (this.cancelSchedule(asyncTaskId, reasons, RdpInterruptedType.CANCEL)) {
            // [RUNNING -> CANCEL]
            return;
        }

        RdpAsyncTaskDO t = this.rdpAsyncTaskMapper.queryById(asyncTaskId);
        switch (t.getStatus()) {
            case INIT:
                this.rdpAsyncTaskMapper.updateFromInitTo(asyncTaskId, RdpAsyncTaskStatus.CANCEL.name(), reasons);
                t.setStatus(RdpAsyncTaskStatus.CANCEL);
                //RdpGlobalEventBus.triggerRdpAsyncEvent(t);
                break;
            case BLOCK:
                // [BLOCK -> CANCEL]
                this.rdpAsyncTaskMapper.updateFromBlockTo(asyncTaskId, RdpAsyncTaskStatus.CANCEL.name(), reasons);
                t.setStatus(RdpAsyncTaskStatus.CANCEL);
                //RdpGlobalEventBus.triggerRdpAsyncEvent(t);
                break;
            case RUNNING:
                throw new IllegalStateException("the pause failed, may be the task running on another console.");
            case PAUSE:
                // [PAUSE -> CANCEL]
                this.rdpAsyncTaskMapper.updateFromPauseTo(asyncTaskId, RdpAsyncTaskStatus.CANCEL.name(), reasons);
                t.setStatus(RdpAsyncTaskStatus.CANCEL);
                //RdpGlobalEventBus.triggerRdpAsyncEvent(t);
                break;
            default:
                break;
        }
    }

    @Override
    public void retryTask(long asyncTaskId, String reasons) {
        // [CANCEL/FAILURE -> WAIT_START]
        RdpAsyncTaskDO taskDO = this.rdpAsyncTaskMapper.queryById(asyncTaskId);
        if (taskDO.getStatus() == RdpAsyncTaskStatus.FAILURE || taskDO.getStatus() == RdpAsyncTaskStatus.CANCEL) {
            this.rdpAsyncTaskMapper.retryFailureOrCancelTask(asyncTaskId, reasons, getHostIp());
            taskDO.setStatus(RdpAsyncTaskStatus.WAIT_START);
            //RdpGlobalEventBus.triggerRdpAsyncEvent(taskDO);
            this.trigger();
        }
    }

    @Override
    public void resumeTask(long asyncTaskId, String reasons) {
        // [PAUSE -> WAIT_START]
        RdpAsyncTaskDO taskDO = this.rdpAsyncTaskMapper.queryById(asyncTaskId);
        if (taskDO.getStatus() == RdpAsyncTaskStatus.PAUSE) {
            this.rdpAsyncTaskMapper.resumePauseTask(asyncTaskId, reasons, getHostIp());
            taskDO.setStatus(RdpAsyncTaskStatus.WAIT_START);
            //RdpGlobalEventBus.triggerRdpAsyncEvent(taskDO);
            this.trigger();
        }
    }
}
