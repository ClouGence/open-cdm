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

import java.io.Closeable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

import com.clougence.utils.ThreadUtils;
import com.clougence.utils.future.CgFuture;
import com.clougence.utils.timer.HashedWheelTimer;
import com.clougence.utils.timer.Timeout;

import lombok.extern.slf4j.Slf4j;

/**
 * Low-latency task Dispatcher
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-10-09
 */
@Slf4j
public class ScheduleExecutor implements Closeable {

    private final HashedWheelTimer  timer;
    private final Queue<TaskWorker> tasks;
    //
    private final AtomicBoolean     runTag;
    private final Thread[]          workerThreads;

    public ScheduleExecutor(int taskThreads, ThreadFactory timerTF, ThreadFactory workerTF){
        this.timer = new HashedWheelTimer(timerTF);
        this.tasks = new ConcurrentLinkedQueue<>();
        this.runTag = new AtomicBoolean(false);
        this.workerThreads = new Thread[taskThreads];

        if (this.runTag.compareAndSet(false, true)) {
            for (int i = 0; i < taskThreads; i++) {
                this.workerThreads[i] = workerTF.newThread(this::doWork);
                this.workerThreads[i].start();
            }
        }
    }

    public int getQueueSize() { return this.tasks.size(); }

    public void close() {
        this.runTag.set(false);

        long t = System.currentTimeMillis();
        while (true) {
            int terminated = 0;

            for (Thread thread : this.workerThreads) {
                if (!thread.isInterrupted()) {
                    thread.interrupt();
                }
                if (thread.getState() == Thread.State.WAITING) {
                    LockSupport.unpark(thread);
                }

                if (thread.getState() == Thread.State.TERMINATED) {
                    terminated++;
                }
            }

            if (terminated == this.workerThreads.length) {
                break;
            }

            long cost = System.currentTimeMillis() - t;
            if (cost > 3000) {
                t = System.currentTimeMillis();
                log.info("wait workerThread close...");
            }

            ThreadUtils.sleep(100);
        }

        log.info("workerThread closed.");
    }

    private void doWork() {
        while (this.runTag.get()) {
            TaskWorker poll = this.tasks.poll();
            if (poll != null) {
                poll.run();
            } else {
                if (this.tasks.isEmpty()) {
                    LockSupport.park();
                    if (Thread.currentThread().isInterrupted()) {
                        log.warn("task thread interrupted, (" + Thread.currentThread().getName() + ")");
                        return;
                    }
                }
            }
        }
        log.info("task thread exit, (" + Thread.currentThread().getName() + ")");
    }

    public CgFuture<Object> submitTask(ScheduleTask task, CgFuture<Object> future) {
        TaskWorker worker = new TaskWorker(this, task, future);

        int delayTime = task.getDelayTime();
        if (delayTime > 0) {
            Timeout timeout = this.timer.newTimeout(t -> {
                this.tasks.add(worker);
                wakeUp();
            }, delayTime, task.getDelayUnit());

        } else {
            this.tasks.add(worker);
            wakeUp();
        }

        return future;
    }

    private void wakeUp() {
        for (Thread workerThread : this.workerThreads) {
            if (workerThread.getState() == Thread.State.WAITING) {
                LockSupport.unpark(workerThread);
                break;
            }
        }
    }

    private record TaskWorker(ScheduleExecutor executor, ScheduleTask task, CgFuture<Object> future) implements Runnable {

        @Override
        public void run() {
            try {
                this.task.run();

                if (!this.future.isDone()) {
                    ScheduleTask.TaskStatus taskStatus = this.task.getStatus();
                    taskStatus = taskStatus == null ? ScheduleTask.TaskStatus.Finish : taskStatus;
                    switch (taskStatus) {
                        case Delay:
                            this.executor.submitTask(this.task, this.future);
                            break;
                        case Finish:
                            this.future.completed(this.task.getResult());
                            break;
                        case Failed:
                            this.future.failed(this.task.getCause());
                            break;
                        default:
                            this.future.failed(new IllegalStateException("Unknown end state."));
                            break;
                    }
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                this.future.failed(e);
            }
        }
    }
}
