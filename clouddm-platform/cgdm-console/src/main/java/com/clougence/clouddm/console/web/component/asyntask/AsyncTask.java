package com.clougence.clouddm.console.web.component.asyntask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import com.clougence.clouddm.console.web.dal.enumeration.DmAsyncTaskProcessType;
import com.clougence.clouddm.console.web.dal.mapper.DmAsyncTaskMapper;
import com.clougence.clouddm.console.web.dal.model.DmAsyncTaskDO;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.utils.future.CgFutureObj;

import lombok.extern.slf4j.Slf4j;

/**
 * default Task
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-09-24
 */
@Slf4j
public abstract class AsyncTask extends ScheduleTask {

    @Resource
    protected DmAsyncTaskMapper                    asyncTaskMapper;
    private DmAsyncTaskDO                          taskDO;
    private boolean                                fastFail;
    private final CgFutureObj<Object>              future      = new CgFutureObj<>();
    private final AtomicReference<InterruptedType> interrupted = new AtomicReference<>();

    DmAsyncTaskDO getTaskDO() { return taskDO; }

    void setTaskDO(DmAsyncTaskDO taskDO) { this.taskDO = taskDO; }

    void setInterrupted(InterruptedType interruptedType) {
        this.interrupted.set(interruptedType);
    }

    CgFutureObj<Object> getFuture() { return future; }

    InterruptedType getInterruptedType() { return this.interrupted.get(); }

    boolean isFastFail() { return this.fastFail || this.taskDO.isFastFail(); }

    @Override
    protected final void doWork(int doCnt) {
        this.executeTask(doCnt, this.taskDO.getConfigData());
    }
    // -------------------------------------------------
    //                      for impl async task methods.
    // -------------------------------------------------

    protected void failedTaskDoNotPause(Exception e) {
        super.failedTask(e);
        this.fastFail = true;
    }

    protected boolean isInterrupted() { return this.interrupted.get() != null; }

    protected boolean isPause() { return this.interrupted.get() == InterruptedType.PAUSE; }

    protected boolean isCancel() { return this.interrupted.get() == InterruptedType.CANCEL; }

    protected void updateMessage(String date) {
        this.asyncTaskMapper.updateStatusMessage(this.taskDO.getId(), date);

        this.taskDO.setStatusMsg(date);
    }

    protected void updateProcess(long curValue, long maxValue) {
        BigDecimal currentCount = new BigDecimal(curValue);
        BigDecimal totalCount = new BigDecimal(maxValue);
        BigDecimal divide = currentCount.divide(totalCount, 2, RoundingMode.HALF_UP);
        long value = divide.multiply(new BigDecimal(100)).longValue();
        this.asyncTaskMapper.updateProcess(this.taskDO.getId(), DmAsyncTaskProcessType.PROGRESS.name(), String.valueOf(value));

        this.taskDO.setProcessType(DmAsyncTaskProcessType.PROGRESS);
        this.taskDO.setProcessValue(String.valueOf(value));
    }

    protected void notifyAsyncEvent() {
        if (this.taskDO.isShowInDock()) {
            DmGlobalEventBus.triggerDmAsyncEvent(this.taskDO);
        }
    }

    protected abstract void executeTask(int doCnt, String configData);
}
