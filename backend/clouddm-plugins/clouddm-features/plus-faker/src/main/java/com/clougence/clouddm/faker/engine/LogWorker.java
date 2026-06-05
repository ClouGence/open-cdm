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

import java.io.File;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.MDC;

import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.clouddm.sdk.execute.tools.ToolUtils;
import com.clougence.clouddm.sdk.model.faker.FakerConfigDTO;
import com.clougence.clouddm.sdk.model.faker.FakerRunModel;
import com.clougence.utils.ThreadUtils;

/**
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
class LogWorker implements ShutdownHook, Runnable {

    private static final Logger       logger = ToolUtils.getLoggerAppender();
    private final String              threadName;
    private final FakerEngine         engine;
    private final FakerConfigDTO      fakerConfig;
    private final ExitCondition       exitCondition;
    private final FakerMonitor        monitor;
    private final Map<String, String> logCtx;
    private volatile Thread           logThread;

    public LogWorker(String threadName, FakerEngine engine, FakerMonitor monitor, Map<String, String> logCtx,//
                     FakerConfigDTO fakerConfig, ExitCondition exitCondition){
        this.threadName = threadName;
        this.engine = engine;
        this.exitCondition = exitCondition;
        this.monitor = monitor;
        this.logCtx = Objects.requireNonNull(logCtx);
        this.fakerConfig = fakerConfig;
    }

    @Override
    public void shutdown() {
        if (this.logThread != null && this.logThread.getState() != Thread.State.TERMINATED) {
            this.logThread.interrupt();
        }
    }

    @Override
    public boolean isTerminated() { return this.logThread.getState() == Thread.State.TERMINATED; }

    private boolean testContinue() {
        return !this.engine.isExitSignal() && !Thread.interrupted() && this.exitCondition.testContinue(this.monitor);
    }

    @Override
    public void run() {
        this.logThread = Thread.currentThread();
        this.logThread.setName(this.threadName);
        MDC.setContextMap(this.logCtx);

        try {
            logger.info("logWorker '" + this.threadName + "' thread startup.");

            this.doWorker();

            logger.info("logWorker '" + this.threadName + "' thread exits.");
        } catch (Throwable e) {
            logger.error("logWorker '" + this.threadName + "' thread exits, encountered an error, " + e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            this.engine.stop(false);
        }
    }

    private void doWorker() {
        FakerRunModel runModel = this.fakerConfig.getRunModel();
        OpsType[] includeTypes = (runModel == FakerRunModel.FULL) ? new OpsType[] { OpsType.Insert } : OpsType.values();
        File logFile = ToolUtils.getLogFile();

        while (this.testContinue()) {
            if (!logFile.exists()) {
                try {
                    logFile.getParentFile().mkdirs();
                    logFile.createNewFile();
                } catch (Exception ignored) {
                }
            }

            this.monitor.clearLastException();
            logger.info(this.monitor.getInformation(includeTypes));
            ThreadUtils.sleep(500);
        }

        // maker sure print last log
        ThreadUtils.sleep(200);
        logger.info(this.monitor.getInformation(includeTypes));
    }
}
