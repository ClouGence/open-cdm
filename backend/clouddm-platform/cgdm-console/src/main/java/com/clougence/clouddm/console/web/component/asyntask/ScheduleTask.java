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
package com.clougence.clouddm.console.web.component.asyntask;

import java.util.concurrent.TimeUnit;

import org.slf4j.MDC;

/**
 * default Task
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-09-24
 */
public abstract class ScheduleTask implements Runnable {

    public enum TaskStatus {
        Finish,
        Failed,
        Delay
    }

    private TaskStatus status;
    private int        delayTime;
    private TimeUnit   delayUnit;
    private Exception  cause;
    private int        retryCnt;
    private Object     result;

    Throwable getCause() { return this.cause; }

    TaskStatus getStatus() { return this.status; }

    int getDelayTime() { return this.delayTime; }

    TimeUnit getDelayUnit() { return this.delayUnit; }

    Object getResult() { return this.result; }

    protected void delayTask(int delay, TimeUnit unit) {
        this.delayTime = delay;
        this.delayUnit = unit;
        this.cause = null;
        this.status = TaskStatus.Delay;
    }

    protected void finishTask(Object result) {
        this.delayTime = 0;
        this.cause = null;
        this.status = TaskStatus.Finish;
        this.result = result;
    }

    protected void failedTask(Exception e) {
        this.delayTime = 0;
        this.cause = e;
        this.status = TaskStatus.Failed;
    }

    @Override
    public final void run() {
        MDC.put("module", "async_task");
        this.doWork(this.retryCnt);
        this.retryCnt++;
    }

    protected abstract void doWork(int doCnt);
}
