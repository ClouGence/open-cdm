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
package com.clougence.clouddm.faker.engine;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.MDC;

import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.generator.FakerRepository;
import com.clougence.clouddm.sdk.execute.tools.ToolUtils;
import com.clougence.clouddm.sdk.model.faker.FakerConfigDTO;
import com.clougence.clouddm.sdk.model.faker.FakerRunStatus;
import com.clougence.utils.ThreadUtils;

import lombok.Getter;

/**
 * 压力引擎
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class FakerEngine {

    private static final Logger                   logger = ToolUtils.getLoggerAppender();
    private final SessionFactory                  sessionFactory;
    @Getter
    private final FakerConfigDTO                  fakerConfig;
    private final FakerEngineConfig               engineConfig;
    @Getter
    private final FakerRepository                 repository;
    private final AbstractCondition               condition;
    //
    //private final QoSBucket                     qosBucket;
    private final ThreadFactory                   threadFactory;
    private final EventQueue                      eventQueue;
    private final List<ShutdownHook>              workers;
    @Getter
    private final FakerMonitor                    monitor;
    private final AtomicReference<FakerRunStatus> status;

    public FakerEngine(FakerConfigDTO fakerConfig, SessionFactory sessionFactory, FakerRepository repository, AbstractCondition condition){
        this.fakerConfig = fakerConfig;
        this.sessionFactory = sessionFactory;
        this.engineConfig = repository.getEngineConfig();
        this.repository = repository;
        this.condition = Objects.requireNonNull(condition);
        this.eventQueue = new EventQueue(this.engineConfig.getQueueCapacity());

        //this.qosBucket = (this.fakerConfig.getWriteQps() > 0) ? new QoSBucket(fakerConfig.getWriteQps()) : null;
        this.workers = new CopyOnWriteArrayList<>();
        this.monitor = new FakerMonitor(this.eventQueue, this.engineConfig.isPrintSql());
        this.status = new AtomicReference<>(FakerRunStatus.INIT);

        if (this.engineConfig.getThreadFactory() != null) {
            this.threadFactory = this.engineConfig.getThreadFactory();
        } else {
            this.threadFactory = Thread::new;
        }
    }

    /** 各 worker 否退出？*/
    public boolean isExitSignal() { return !this.status.get().equals(FakerRunStatus.RUNNING); }

    public boolean isPause() { return this.status.get().equals(FakerRunStatus.PAUSE); }

    public FakerRunStatus getStatus() { return this.status.get(); }

    void checkQoS() {
        //if (this.qosBucket != null) {
        //    this.qosBucket.check();
        //}
    }

    public boolean start() {
        if (this.status.compareAndSet(FakerRunStatus.INIT, FakerRunStatus.RUNNING)) {
            int pThreadCnt = this.engineConfig.getPThreadCnt();
            int wThreadCnt = this.engineConfig.getWThreadCnt();
            logger.info("faker starting up, (pThreadCnt is " + pThreadCnt + ", wThreadCnt is " + wThreadCnt + ")");
            this.waitThreadStart(this.monitor::startCost);
            return true;
        }

        return false;
    }

    public boolean pause() {
        // make just one thread call (resume or pause) function and check right status
        if (!this.status.compareAndSet(FakerRunStatus.RUNNING, FakerRunStatus.WAITING_PAUSE)) {
            return this.status.get().equals(FakerRunStatus.PAUSE) || this.status.get().equals(FakerRunStatus.WAITING_PAUSE);
        }

        logger.info("faker to pause.");
        this.waitThreadExit(true, this.monitor::pauseCost);
        this.status.set(FakerRunStatus.PAUSE);
        logger.info("faker pause complete.");
        return true;
    }

    public boolean resume() {
        // make just one thread call (resume or pause) function and check right status
        if (!this.status.compareAndSet(FakerRunStatus.PAUSE, FakerRunStatus.WAITING_RESUME)) {
            return this.status.get().equals(FakerRunStatus.WAITING_RESUME) || this.status.get().equals(FakerRunStatus.RUNNING);
        }

        int pThreadCnt = this.engineConfig.getPThreadCnt();
        int wThreadCnt = this.engineConfig.getWThreadCnt();
        logger.info("faker to resume, (pThreadCnt is " + pThreadCnt + ", wThreadCnt is " + wThreadCnt + ")");
        this.waitThreadStart(this.monitor::resumeCost);
        this.status.set(FakerRunStatus.RUNNING);
        return true;
    }

    public boolean stop(boolean isWait) {
        if (!this.status.compareAndSet(FakerRunStatus.RUNNING, FakerRunStatus.COMPLETE) && !this.status.compareAndSet(FakerRunStatus.PAUSE, FakerRunStatus.COMPLETE)) {
            return false;
        }

        this.waitThreadExit(isWait, this.monitor::stopCost);
        //        this.monitor.reset();
        return true;
    }

    private void waitThreadStart(Runnable costCallBack) {
        int pThreadCnt = this.engineConfig.getPThreadCnt();
        int wThreadCnt = this.engineConfig.getWThreadCnt();
        String repositoryID = this.repository.getGeneratorID();
        Map<String, String> logCtx = MDC.getCopyOfContextMap();
        if (logCtx == null) {
            logCtx = Collections.emptyMap();
        }

        for (int i = 0; i < pThreadCnt; i++) {
            String workName = String.format("faker-generator[%s-%s]", repositoryID, i);
            this.workers.add(new ProducerWorker(workName, this, this.monitor, logCtx, this.eventQueue, this.sessionFactory, this.condition, this.condition));
        }

        for (int i = 0; i < wThreadCnt; i++) {
            String workName = String.format("faker-writer[%s-%s]", repositoryID, i);
            this.workers.add(new WriteWorker(workName, this, this.monitor, logCtx, this.eventQueue, this.sessionFactory, this.condition));
        }

        String loggerName = String.format("faker-logger[%s]", repositoryID);
        this.workers.add(new LogWorker(loggerName, this, this.monitor, logCtx, this.fakerConfig, this.condition));

        costCallBack.run();

        this.eventQueue.clear();
        for (ShutdownHook worker : this.workers) {
            Thread thread = this.threadFactory.newThread(worker);
            thread.setUncaughtExceptionHandler((t, e) -> {
                logger.error(e.getMessage(), e);
                this.stop(false);
            });
            thread.start();
        }
    }

    private void waitThreadExit(boolean isWait, Runnable costCallBack) {
        for (ShutdownHook hook : this.workers) {
            hook.shutdown();
        }
        costCallBack.run();

        if (!isWait) {
            return;
        }

        while (true) {
            List<ShutdownHook> terminated = new ArrayList<>();
            for (ShutdownHook hook : this.workers) {
                if (hook.isTerminated()) {
                    terminated.add(hook);
                } else {
                    break;
                }
            }

            this.workers.removeAll(terminated);
            if (this.workers.isEmpty()) {
                this.eventQueue.clear();
                break;
            } else {
                ThreadUtils.sleep(100);
            }
        }
        logger.info("all worker threads exit.");
    }
}
