package com.clougence.rdp.component.asyntask;

import java.util.concurrent.TimeUnit;

import org.slf4j.MDC;

/**
 * default Task
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-09-24
 */
public abstract class RdpScheduleTask implements Runnable {

    public enum RdpTaskStatus {
        Finish,
        Failed,
        Delay
    }

    private RdpTaskStatus status;
    private int           delayTime;
    private TimeUnit      delayUnit;
    private Exception     cause;
    private int           retryCnt;
    private Object        result;

    Throwable getCause() { return this.cause; }

    RdpTaskStatus getStatus() { return this.status; }

    int getDelayTime() { return this.delayTime; }

    TimeUnit getDelayUnit() { return this.delayUnit; }

    Object getResult() { return this.result; }

    protected void delayTask(int delay, TimeUnit unit) {
        this.delayTime = delay;
        this.delayUnit = unit;
        this.cause = null;
        this.status = RdpTaskStatus.Delay;
    }

    protected void finishTask(Object result) {
        this.delayTime = 0;
        this.cause = null;
        this.status = RdpTaskStatus.Finish;
        this.result = result;
    }

    protected void failedTask(Exception e) {
        this.delayTime = 0;
        this.cause = e;
        this.status = RdpTaskStatus.Failed;
    }

    @Override
    public final void run() {
        MDC.put("module", "async_task");
        this.doWork(this.retryCnt);
        this.retryCnt++;
    }

    protected abstract void doWork(int doCnt);
}
