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
package com.clougence.clouddm.console.web.service.editor.language;

import java.io.Closeable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleLanguageTaskExecutor implements Closeable {

    private final Queue<Runnable> tasks;
    private final AtomicBoolean   runTag;
    private final Thread[]        workerThreads;

    public ConsoleLanguageTaskExecutor(ClassLoader classLoader, int taskThreads){
        this.tasks = new ConcurrentLinkedQueue<>();
        this.runTag = new AtomicBoolean(false);
        this.workerThreads = new Thread[taskThreads];

        if (this.runTag.compareAndSet(false, true)) {
            ThreadFactory tf = ThreadUtils.daemonThreadFactory(classLoader, "Language-Execute-%s");
            for (int i = 0; i < taskThreads; i++) {
                this.workerThreads[i] = tf.newThread(this::doWork);
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
                log.info("wait language workerThread close...");
            }

            ThreadUtils.sleep(100);
        }

        log.info("language workerThread closed.");
    }

    public void submitTask(Runnable task) {
        this.tasks.add(task);
        wakeUp();
    }

    private void doWork() {
        while (this.runTag.get()) {
            Runnable poll = this.tasks.poll();
            if (poll != null) {
                try {
                    poll.run();
                } catch (Throwable e) {
                    log.error(e.toString(), e);
                }
            } else {
                if (this.tasks.isEmpty()) {
                    LockSupport.park();
                    if (Thread.currentThread().isInterrupted()) {
                        log.warn("language task thread interrupted, (" + Thread.currentThread().getName() + ")");
                        return;
                    }
                }
            }
        }
        log.info("language thread exit, (" + Thread.currentThread().getName() + ")");
    }

    private void wakeUp() {
        for (Thread workerThread : this.workerThreads) {
            if (workerThread.getState() == Thread.State.WAITING) {
                LockSupport.unpark(workerThread);
                break;
            }
        }
    }
}
