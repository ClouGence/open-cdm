/*
 * Copyright 2015-2022 the original author or authors. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.clougence.clouddm.faker.engine;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.MDC;

import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.generator.BoundQuery;
import com.clougence.clouddm.faker.generator.SqlArg;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.tools.ToolUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 写入器
 *
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
class WriteWorker implements ShutdownHook, Runnable {

    private static final Logger       logger    = ToolUtils.getLoggerAppender();
    private final String              threadName;
    private final FakerEngine         engine;
    private final FakerEngineConfig   engineConfig;
    private final ExitCondition       exitCondition;
    private final FakerMonitor        monitor;
    private final Map<String, String> logCtx;
    private final EventQueue          eventQueue;
    private final SessionFactory      sessionFactory;
    private volatile Thread           workThread;
    private volatile Session          workSession;
    private static final int          MAX_BATCH = 200;
    private PreparedStatement         ps;

    public WriteWorker(String threadName, FakerEngine engine, FakerMonitor monitor, Map<String, String> logCtx, //
                       EventQueue eventQueue, SessionFactory sessionFactory, ExitCondition exitCondition){
        this.threadName = threadName;
        this.engine = engine;
        this.engineConfig = engine.getRepository().getEngineConfig();
        this.exitCondition = exitCondition;
        this.monitor = monitor;
        this.logCtx = Objects.requireNonNull(logCtx);
        this.eventQueue = eventQueue;
        this.sessionFactory = sessionFactory;
    }

    private static String newBatchID() {
        return UUID.randomUUID().toString().replace("-", "");
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
                logger.warn("writeWorker has an unexpected error on exit, " + e.getMessage(), e);
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
        logger.info("writeWorker '" + this.threadName + "' thread init.");

        try (Session session = this.sessionFactory.createSession()) {
            this.workSession = session;
            this.monitor.writerStart(this.threadName, this.workThread);
            logger.info("writeWorker '" + this.threadName + "' thread startup.");

            while (this.testContinue()) {
                try {
                    this.doWorker(session);
                } catch (InterruptedException e) {
                    return;
                } catch (Throwable e) {
                    logger.error("writeWorker '" + this.threadName + "' encountered an error, " + e.getMessage(), e);
                }
            }

            logger.info("writeWorker '" + this.threadName + "' thread exits.");
        } catch (Throwable e) {
            logger.error("writeWorker '" + this.threadName + "' thread exits, encountered an error, " + e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            this.engine.stop(false);
        }
    }

    private void doWorker(Session session) throws Exception {
        List<BoundQuery> queries = this.eventQueue.tryPoll();
        if (queries == null) {
            Thread.sleep(100); // prevent empty loop
            return;
        }

        if (this.workThread.isInterrupted()) {
            throw new InterruptedException();
        }

        session.executeQuery(con -> {
            try {
                String batchID = null;
                if (this.engineConfig.isTransaction()) {
                    Thread.sleep(this.engineConfig.randomPausePerTransactionMs());
                    batchID = newBatchID();
                    con.setAutoCommit(false);
                }

                doBatch(batchID, con, queries);

                if (this.engineConfig.isTransaction()) {
                    con.commit();
                }
            } finally {
                if (this.engineConfig.isTransaction()) {
                    con.setAutoCommit(true);
                }
            }
            return null;
        });
    }

    private void doBatch(final String tranID, Connection conn, List<BoundQuery> batch) throws SQLException, InterruptedException {
        if (this.workThread.isInterrupted()) {
            throw new InterruptedException();
        }

        String useTranID = tranID;
        if (useTranID == null) {
            useTranID = newBatchID();
        }

        this.engine.checkQoS(); // 写入限流

        try {
            doEvent(conn, batch, useTranID);
        } catch (SQLException e) {
            if (!this.engineConfig.ignoreError(e)) {
                this.engine.stop(false);
                throw e;
            }
        }

    }

    private void doEvent(Connection conn, List<BoundQuery> batch, String useTranID) throws SQLException {
        if (CollectionUtils.isEmpty(batch)) {
            return;
        }
        BoundQuery eventA = null;
        int batchSize = batch.size();
        int size = Math.min(batchSize, MAX_BATCH);
        for (int i = 0; i < size; i++) {
            BoundQuery event = batch.get(i);
            String sqlString = event.getSqlString();
            SqlArg[] sqlArgs = event.getArgs();

            if (this.exitCondition.testIgnore(this.monitor, event)) {
                return;
            }

            if (i == 0) {
                this.ps = conn.prepareStatement(sqlString);
                eventA = event;
            }

            for (int j = 1; j <= sqlArgs.length; j++) {
                if (sqlArgs[j - 1] == null) {
                    this.ps.setObject(j, null);
                } else {
                    sqlArgs[j - 1].setParameter(this.ps, j);
                }
            }

            this.ps.addBatch();
        }

        int events = 0;
        long writeStart = 0;
        long writeCost = 0;
        try {
            writeStart = System.currentTimeMillis();
            events = this.ps.executeBatch().length;
            writeCost = System.currentTimeMillis() - writeStart;
        } catch (BatchUpdateException e) {
            writeCost = System.currentTimeMillis() - writeStart;
            this.monitor.recordFailed(this.threadName, useTranID, Objects.requireNonNull(eventA), e);
            logger.error(e.getMessage());
            for (int count : e.getUpdateCounts()) {
                if (count >= 0) {
                    events++;
                }
            }
        } finally {
            this.ps.close();
            this.monitor.recordMonitor(this.threadName, useTranID, Objects.requireNonNull(eventA), events, writeCost);
        }
    }
}
