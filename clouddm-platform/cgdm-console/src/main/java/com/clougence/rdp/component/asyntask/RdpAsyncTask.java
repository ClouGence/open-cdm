package com.clougence.rdp.component.asyntask;

import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import com.clougence.rdp.dal.enumeration.RdpAsyncTaskProcessType;
import com.clougence.rdp.dal.mapper.RdpAsyncTaskMapper;
import com.clougence.rdp.dal.model.RdpAsyncTaskDO;
import com.clougence.utils.future.CgFutureObj;

import lombok.extern.slf4j.Slf4j;

/**
 * default Task
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-09-24
 */
@Slf4j
public abstract class RdpAsyncTask extends RdpScheduleTask {

    @Resource
    protected RdpAsyncTaskMapper                      asyncTaskMapper;
    private RdpAsyncTaskDO                            taskDO;
    private boolean                                   fastFail;
    private final CgFutureObj<Object>                 future      = new CgFutureObj<>();
    private final AtomicReference<RdpInterruptedType> interrupted = new AtomicReference<>();

    RdpAsyncTaskDO getTaskDO() { return taskDO; }

    void setTaskDO(RdpAsyncTaskDO taskDO) { this.taskDO = taskDO; }

    void setInterrupted(RdpInterruptedType rdpInterruptedType) {
        this.interrupted.set(rdpInterruptedType);
    }

    CgFutureObj<Object> getFuture() { return future; }

    RdpInterruptedType getInterruptedType() { return this.interrupted.get(); }

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

    protected boolean isPause() { return this.interrupted.get() == RdpInterruptedType.PAUSE; }

    protected boolean isCancel() { return this.interrupted.get() == RdpInterruptedType.CANCEL; }

    protected void updateMessage(String date) {
        this.asyncTaskMapper.updateStatusMessage(this.taskDO.getId(), date);

        this.taskDO.setStatusMsg(date);
    }

    protected void updateProcess(long curValue, long maxValue) {
        String val = String.format("{\"cur\":%s, \"max\":%s }", curValue, maxValue);
        this.asyncTaskMapper.updateProcess(this.taskDO.getId(), RdpAsyncTaskProcessType.PROGRESS.name(), val);

        this.taskDO.setProcessType(RdpAsyncTaskProcessType.PROGRESS);
        this.taskDO.setProcessValue(val);
    }

    protected void updateProcess(String value) {
        this.asyncTaskMapper.updateProcess(this.taskDO.getId(), RdpAsyncTaskProcessType.SCROLL.name(), value);

        this.taskDO.setProcessType(RdpAsyncTaskProcessType.SCROLL);
        this.taskDO.setProcessValue(value);
    }

    protected abstract void executeTask(int doCnt, String configData);
}
