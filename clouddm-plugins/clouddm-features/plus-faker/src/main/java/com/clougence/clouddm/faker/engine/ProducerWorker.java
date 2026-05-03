/*
 * Copyright 2015-2022 the original author or authors. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.clougence.clouddm.faker.engine;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.MDC;

import com.clougence.clouddm.faker.generator.BoundQuery;
import com.clougence.clouddm.faker.generator.FakerRepository;
import com.clougence.clouddm.faker.generator.GenCondition;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.tools.ToolUtils;
import com.clougence.utils.ThreadUtils;

/**
 * 数据发生器
 *
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
class ProducerWorker implements ShutdownHook, Runnable {

    private static final Logger       logger = ToolUtils.getLoggerAppender();
    private final String              threadName;
    private final FakerEngine         engine;
    private final FakerRepository     repository;
    private final GenCondition        genCondition;
    private final ExitCondition       exitCondition;
    private final FakerMonitor        monitor;
    private final Map<String, String> logCtx;
    private final EventQueue          eventQueue;
    private final SessionFactory      sessionFactory;
    private volatile Thread           workThread;
    private volatile Session          workSession;

    ProducerWorker(String threadName, FakerEngine engine, FakerMonitor monitor, Map<String, String> logCtx, //
                   EventQueue eventQueue, SessionFactory sessionFactory,//
                   ExitCondition exitCondition, GenCondition genCondition){
        this.threadName = threadName;
        this.engine = engine;
        this.repository = engine.getRepository();
        this.genCondition = genCondition;
        this.exitCondition = exitCondition;
        this.monitor = monitor;
        this.logCtx = Objects.requireNonNull(logCtx);
        this.eventQueue = eventQueue;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void shutdown() {
        // the first time interrupt, give worker process 25ms window to exit, then will be close session
        ThreadUtils.safeInterrupt(this.workThread);
        ThreadUtils.sleep(20);
        if (this.workSession != null && this.workSession.isExecuting()) {
            try {
                // close cannot be used. in mysql:
                //      if doWorker in the query or execute, then close well be wait for lock.
                //      if query or execute is blocked, the close operation will deadlock!!!
                this.workSession.cancel();
            } catch (Exception e) {
                logger.warn("producerWorker has an unexpected error on exit, " + e.getMessage(), e);
            }
        }

        // second interrupt
        ThreadUtils.safeInterrupt(this.workThread);
    }

    @Override
    public boolean isTerminated() { return this.workThread.getState() == Thread.State.TERMINATED; }

    private boolean testContinue() {
        return !this.engine.isExitSignal() && !Thread.interrupted() && this.exitCondition.testContinue(this.monitor);
    }

    @Override
    public void run() {
        this.workThread = Thread.currentThread();
        this.workThread.setName(this.threadName);
        MDC.setContextMap(this.logCtx);
        logger.info("producerWorker '" + this.threadName + "' thread init.");

        try (Session session = this.sessionFactory.createSession()) {
            this.workSession = session;
            this.monitor.producerStart(this.threadName, this.workThread);
            logger.info("producerWorker '" + this.threadName + "' thread startup.");

            while (this.testContinue()) {
                try {
                    this.doWorker(session);
                } catch (InterruptedException e) {
                    return;
                } catch (Throwable e) {
                    logger.error("producerWorker '" + this.threadName + "' encountered an error, " + e.getMessage(), e);
                }
            }

            logger.info("producerWorker '" + this.threadName + "' thread exits.");
        } catch (Throwable e) {
            logger.error("producerWorker '" + this.threadName + "' thread exits, encountered an error, " + e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            this.engine.stop(false);
        }
    }

    private void doWorker(Session session) throws Exception {
        List<BoundQuery> queries = this.repository.generator(this.genCondition, session);
        while (this.testContinue()) {
            if (!this.eventQueue.tryOffer(queries)) {
                Thread.sleep(100); // prevent empty loop
            } else {
                return;
            }

            if (this.workThread.isInterrupted()) {
                throw new InterruptedException();
            }
        }
    }
}
