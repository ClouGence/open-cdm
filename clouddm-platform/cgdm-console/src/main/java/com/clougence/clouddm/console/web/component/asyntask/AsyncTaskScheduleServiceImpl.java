package com.clougence.clouddm.console.web.component.asyntask;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.dal.enumeration.DmAsyncTaskStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmAsyncTaskMapper;
import com.clougence.clouddm.console.web.dal.model.DmAsyncTaskDO;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.rdp.util.RdpTimerUtils;
import com.clougence.utils.HostUtil;
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
public class AsyncTaskScheduleServiceImpl implements AsyncTaskScheduleService, UnifiedPostConstruct {

    @Resource
    private DmAsyncTaskMapper    dmAsyncTaskMapper;
    @Resource
    private DmConsoleConfig      dmConfig;
    @Resource
    private ApplicationContext   applicationContext;

    private ScheduleExecutor     scheduleExecutor;
    private Map<Long, AsyncTask> scheduleTaskMap;
    private AtomicInteger        requestSchedule;
    private Thread               scheduleWorkThread;

    @Override
    public void init() throws Exception {
        this.dmAsyncTaskMapper.resetAsyncTaskStatus(getHostIp());
        this.dmAsyncTaskMapper.resetInitAsyncTaskStatus(getHostIp());
        this.dmAsyncTaskMapper.resetCancelingAsyncTaskStatus(getHostIp());
        this.dmAsyncTaskMapper.resetPausingAsyncTaskStatus(getHostIp());
        this.requestSchedule = new AtomicInteger();
        this.scheduleTaskMap = new ConcurrentHashMap<>();

        ClassLoader classLoader = this.applicationContext.getClassLoader();
        ThreadFactory timerTF = ThreadUtils.daemonThreadFactory(classLoader, "AsyncTask-Timer-%s");
        ThreadFactory workerTF = ThreadUtils.daemonThreadFactory(classLoader, "AsyncTask-Process-%s");
        this.scheduleExecutor = new ScheduleExecutor(this.dmConfig.getAsyncThreadCount(), timerTF, workerTF);

        this.scheduleWorkThread = ThreadUtils.daemonThread(classLoader, this::loopSchedule);
        this.scheduleWorkThread.setName("AsyncTask-Dispatcher");
        this.scheduleWorkThread.start();
    }

    @Override
    public void stop() {

    }

    private String getHostIp() {
        if (this.dmConfig.getDmMode() == DmMode.desktop) {
            return "127.0.0.1";
        } else {
            return HostUtil.getHostIp();
        }
    }

    //-------------------------------------------------------------------------
    //                                                        Process AsyncTask
    //-------------------------------------------------------------------------

    // A new task comes in.
    public void trigger() {
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
                ThreadUtils.sleep(1000);
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
            List<DmAsyncTaskDO> doList = this.dmAsyncTaskMapper.queryWaitTask(freeSlot(), getHostIp());

            // there is nothing to do.
            if (doList.isEmpty() && this.requestSchedule.get() == 0) {
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
                return;
            }

            // schedule task
            for (DmAsyncTaskDO t : doList) {
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

    private void doScheduleOneTask(DmAsyncTaskDO t) {
        // check depend task
        if (StringUtils.isNotBlank(t.getDependOnBizId())) {
            DmAsyncTaskDO depTaskDO = this.dmAsyncTaskMapper.queryByBiz(t.getDependOnBizId(), t.getDependOnBizType());
            if (depTaskDO.getStatus() != DmAsyncTaskStatus.COMPLETE) {
                int r = this.dmAsyncTaskMapper.updateFromWaitTo(t.getId(), DmAsyncTaskStatus.BLOCK.name(), "The dependent task is not completed.");
                if (r > 0) {
                    depTaskDO.setStatus(DmAsyncTaskStatus.BLOCK);
                    DmGlobalEventBus.triggerDmAsyncEvent(depTaskDO);
                    log.info(String.format("[AsyncTask] The task [%s]%s update status WAIT_START -> BLOCK", t.getBizType(), t.getBizId()));
                } else {
                    log.warn(String.format("[AsyncTask] The task [%s]%s update status WAIT_START -> BLOCK, failed.", t.getBizType(), t.getBizId()));
                }
                return;
            }
        }

        // schedule [WAIT_START -> RUNNING]
        int res2 = this.dmAsyncTaskMapper.updateFromWaitTo(t.getId(), DmAsyncTaskStatus.RUNNING.name(), "start");
        if (res2 <= 0) {
            log.warn(String.format("[AsyncTask] The task [%s]%s update status WAIT_START -> RUNNING failed, maybe have another worker running it.", t.getBizType(), t.getBizId()));
            return;
        } else {
            t.setStatus(DmAsyncTaskStatus.RUNNING);
            DmGlobalEventBus.triggerDmAsyncEvent(t);
            log.info(String.format("[AsyncTask] The task [%s]%s update status WAIT_START -> RUNNING", t.getBizType(), t.getBizId()));
        }

        AsyncTask theTask = getTask(t);
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
        this.scheduleExecutor.submitTask(theTask, theFuture);
    }

    private void taskFutureCallBackOnFailed(CgFuture<?> f, AsyncTask theTask) {
        DmAsyncTaskDO t = theTask.getTaskDO();
        String message = "Error: " + f.getCause().getMessage();
        log.error(String.format("[AsyncTask] The task [%s]%s finish, failed: " + message, t.getBizType(), t.getBizId()), f.getCause());

        DmAsyncTaskStatus toStatus = (theTask.isFastFail()) ? DmAsyncTaskStatus.FAILURE : DmAsyncTaskStatus.PAUSE;
        int r = this.dmAsyncTaskMapper.updateStatusTo(t.getId(), toStatus.name(), message);
        if (r > 0) {
            t.setStatus(toStatus);
            DmGlobalEventBus.triggerDmAsyncEvent(t);
            log.info(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> " + toStatus, t.getBizType(), t.getBizId()));
        } else {
            log.info(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> " + toStatus + ", failed.", t.getBizType(), t.getBizId()));
        }
    }

    private void taskFutureCallBackOnCompleted(CgFuture<?> f, AsyncTask theTask) {
        DmAsyncTaskDO t = theTask.getTaskDO();
        String result = f.getResult() == null ? "Finish" : f.getResult().toString();
        log.info(String.format("[AsyncTask] The task [%s]%s finish, " + result, t.getBizType(), t.getBizId()), f.getCause());

        int r = this.dmAsyncTaskMapper.updateStatusTo(t.getId(), DmAsyncTaskStatus.COMPLETE.name(), result);
        if (r > 0) {
            t.setStatus(DmAsyncTaskStatus.COMPLETE);
            DmGlobalEventBus.triggerDmAsyncEvent(t);
            log.info(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> COMPLETE", t.getBizType(), t.getBizId()));
        } else {
            log.error(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> COMPLETE, failed.", t.getBizType(), t.getBizId()));
        }

        this.wakeUpDependTask(t);
        this.trigger();
    }

    private void taskFutureCallBackOnCancel(CgFuture<?> f, AsyncTask theTask) {
        DmAsyncTaskDO t = theTask.getTaskDO();
        InterruptedType interruptedType = theTask.getInterruptedType();
        log.info(String.format("[AsyncTask] The task [%s]%s cancel by %s.", t.getBizType(), t.getBizId(), interruptedType.name()), f.getCause());

        DmAsyncTaskStatus updateTo = interruptedType == InterruptedType.PAUSE ? DmAsyncTaskStatus.PAUSE : DmAsyncTaskStatus.CANCEL;

        int r = this.dmAsyncTaskMapper.updateStatusTo(t.getId(), updateTo.name(), "by " + interruptedType.name());
        if (r > 0) {
            t.setStatus(updateTo);
            DmGlobalEventBus.triggerDmAsyncEvent(t);
            log.info(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> %s", t.getBizType(), t.getBizId(), updateTo.name()));
        } else {
            log.error(String.format("[AsyncTask] The task [%s]%s update status RUNNING -> %s, failed.", t.getBizType(), t.getBizId(), updateTo.name()));
        }
    }

    /** BLOCK ─> WAIT_START */
    private void wakeUpDependTask(DmAsyncTaskDO taskDO) {
        List<DmAsyncTaskDO> dependTask = this.dmAsyncTaskMapper.queryDepends(taskDO.getBizId(), taskDO.getBizType());
        if (dependTask.isEmpty()) {
            return;
        }

        List<Long> ids = dependTask.stream().map(DmAsyncTaskDO::getId).collect(Collectors.toList());
        int r = this.dmAsyncTaskMapper.batchResumeFromBlock(ids, "depends Task finish.");

        String idsStr = StringUtils.join(dependTask.stream().map(t -> String.format("[%s]%s", t.getBizType(), t.getBizId())).toArray(), ",");
        log.info(String.format("[AsyncTask] Task (%s) update status BLOCK -> WAIT_START, result is " + r, idsStr));

        for (DmAsyncTaskDO task : dependTask) {
            task.setStatus(DmAsyncTaskStatus.WAIT_START);
            DmGlobalEventBus.triggerDmAsyncEvent(task);
        }
    }

    private int freeSlot() {
        int cfgMaxQueueSize = this.dmConfig.getAsyncTaskQueueSize();
        int smtQueueSize = this.scheduleExecutor.getQueueSize();
        return cfgMaxQueueSize - smtQueueSize;
    }

    private AsyncTask getTask(DmAsyncTaskDO taskDO) {
        AsyncTask resultTask;
        String handlerName = taskDO.getHandlerName();
        if (StringUtils.isBlank(handlerName)) {
            log.error(String.format("[AsyncTask] The task [%s]%s handlerName is undefined.", taskDO.getBizType(), taskDO.getBizId()));
            resultTask = new AsyncTask() {

                @Override
                protected void executeTask(int doCnt, String configData) {
                    this.failedTask(new IllegalStateException("No handler defined."));
                }
            };
        } else {
            resultTask = this.applicationContext.getBean(handlerName, AsyncTask.class);
        }

        resultTask.setTaskDO(taskDO);
        return resultTask;
    }

    //-------------------------------------------------------------------------
    //                                                        Manager AsyncTask
    //-------------------------------------------------------------------------

    private boolean cancelSchedule(long asyncTaskId, String reasons, InterruptedType type) {
        AsyncTask asyncTask = this.scheduleTaskMap.get(asyncTaskId);
        if (asyncTask != null) {
            CgFutureObj<Object> f = asyncTask.getFuture();
            if (!f.isDone()) {
                // from running to canceling
                DmAsyncTaskDO t = asyncTask.getTaskDO();
                DmAsyncTaskStatus toStatus = type == InterruptedType.PAUSE ? DmAsyncTaskStatus.PAUSING : DmAsyncTaskStatus.CANCELING;
                int r = this.dmAsyncTaskMapper.updateFromRunningTo(asyncTaskId, toStatus.name(), reasons);
                if (r > 0) {
                    t.setStatus(toStatus);
                    t.setStatusMsg(reasons);
                    DmGlobalEventBus.triggerDmAsyncEvent(t);
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
        if (this.cancelSchedule(asyncTaskId, reasons, InterruptedType.PAUSE)) {
            // [RUNNING -> PAUSE]
            return;
        }

        DmAsyncTaskDO t = this.dmAsyncTaskMapper.queryById(asyncTaskId);
        switch (t.getStatus()) {
            case BLOCK:
                // [BLOCK -> CANCEL]
                this.dmAsyncTaskMapper.updateFromBlockTo(asyncTaskId, DmAsyncTaskStatus.PAUSE.name(), reasons);
                t.setStatus(DmAsyncTaskStatus.PAUSE);
                DmGlobalEventBus.triggerDmAsyncEvent(t);
                break;
            case RUNNING:
                throw new IllegalStateException("the pause failed, may be the task running on another console.");
            default:
                break;
        }
    }

    @Override
    public void cancelTask(long asyncTaskId, String reasons) {
        if (this.cancelSchedule(asyncTaskId, reasons, InterruptedType.CANCEL)) {
            // [RUNNING -> CANCEL]
            return;
        }

        DmAsyncTaskDO t = this.dmAsyncTaskMapper.queryById(asyncTaskId);
        switch (t.getStatus()) {
            case INIT:
                this.dmAsyncTaskMapper.updateFromInitTo(asyncTaskId, DmAsyncTaskStatus.CANCEL.name(), reasons);
                t.setStatus(DmAsyncTaskStatus.CANCEL);
                DmGlobalEventBus.triggerDmAsyncEvent(t);
                break;
            case BLOCK:
                // [BLOCK -> CANCEL]
                this.dmAsyncTaskMapper.updateFromBlockTo(asyncTaskId, DmAsyncTaskStatus.CANCEL.name(), reasons);
                t.setStatus(DmAsyncTaskStatus.CANCEL);
                DmGlobalEventBus.triggerDmAsyncEvent(t);
                break;
            case RUNNING:
                throw new IllegalStateException("the pause failed, may be the task running on another console.");
            case PAUSE:
                // [PAUSE -> CANCEL]
                this.dmAsyncTaskMapper.updateFromPauseTo(asyncTaskId, DmAsyncTaskStatus.CANCEL.name(), reasons);
                t.setStatus(DmAsyncTaskStatus.CANCEL);
                DmGlobalEventBus.triggerDmAsyncEvent(t);
                break;
            default:
                break;
        }
    }

    @Override
    public void retryTask(long asyncTaskId, String reasons) {
        // [CANCEL/FAILURE -> WAIT_START]
        DmAsyncTaskDO taskDO = this.dmAsyncTaskMapper.queryById(asyncTaskId);
        if (taskDO.getStatus() == DmAsyncTaskStatus.FAILURE || taskDO.getStatus() == DmAsyncTaskStatus.CANCEL) {
            this.dmAsyncTaskMapper.retryFailureOrCancelTask(asyncTaskId, reasons, getHostIp());
            taskDO.setStatus(DmAsyncTaskStatus.WAIT_START);
            DmGlobalEventBus.triggerDmAsyncEvent(taskDO);
            this.trigger();
        }
    }

    @Override
    public void resumeTask(long asyncTaskId, String reasons) {
        // [PAUSE -> WAIT_START]
        DmAsyncTaskDO taskDO = this.dmAsyncTaskMapper.queryById(asyncTaskId);
        if (taskDO.getStatus() == DmAsyncTaskStatus.PAUSE) {
            this.dmAsyncTaskMapper.resumePauseTask(asyncTaskId, reasons, getHostIp());
            taskDO.setStatus(DmAsyncTaskStatus.WAIT_START);
            DmGlobalEventBus.triggerDmAsyncEvent(taskDO);
            this.trigger();
        }
    }
}
