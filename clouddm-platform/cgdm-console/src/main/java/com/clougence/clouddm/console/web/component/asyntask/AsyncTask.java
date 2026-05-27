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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;

import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskProcessType;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;
import com.clougence.utils.future.CgFutureObj;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * default Task
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-09-24
 */
@Slf4j
public abstract class AsyncTask extends ScheduleTask {

    @Resource
    protected ExecutionDal                         executionDal;
    private DmExecAsyncTaskDO                      taskDO;
    private boolean                                fastFail;
    private final CgFutureObj<Object>              future      = new CgFutureObj<>();
    private final AtomicReference<InterruptedType> interrupted = new AtomicReference<>();

    DmExecAsyncTaskDO getTaskDO() { return taskDO; }

    void setTaskDO(DmExecAsyncTaskDO taskDO) { this.taskDO = taskDO; }

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
        this.executionDal.asyncTaskMapper().updateStatusMessage(this.taskDO.getId(), date);

        this.taskDO.setStatusMsg(date);
    }

    protected void updateProcess(long curValue, long maxValue) {
        BigDecimal currentCount = new BigDecimal(curValue);
        BigDecimal totalCount = new BigDecimal(maxValue);
        BigDecimal divide = currentCount.divide(totalCount, 2, RoundingMode.HALF_UP);
        long value = divide.multiply(new BigDecimal(100)).longValue();
        this.executionDal.asyncTaskMapper().updateProcess(this.taskDO.getId(), AsyncTaskProcessType.PROGRESS.name(), String.valueOf(value));

        this.taskDO.setProcessType(AsyncTaskProcessType.PROGRESS);
        this.taskDO.setProcessValue(String.valueOf(value));
    }

    protected void notifyAsyncEvent() {
        if (this.taskDO.isShowInDock()) {
            DmGlobalEventBus.triggerDmAsyncEvent(this.taskDO);
        }
    }

    protected abstract void executeTask(int doCnt, String configData);
}
