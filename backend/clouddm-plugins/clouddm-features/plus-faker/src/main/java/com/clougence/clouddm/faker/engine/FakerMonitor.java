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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;

import com.clougence.clouddm.faker.config.OpsType;
import com.clougence.clouddm.faker.generator.BoundQuery;
import com.clougence.clouddm.faker.generator.SqlArg;
import com.clougence.clouddm.sdk.execute.tools.ToolUtils;
import com.clougence.clouddm.sdk.model.faker.FakerRunStatus;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.WellKnowFormat;

import lombok.Getter;

/**
 * 状态监听
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2022-07-25
 */
public class FakerMonitor {

    private static final Logger                         logger            = ToolUtils.getLoggerAppender();
    private static final AtomicLong                     ZERO              = new AtomicLong(0);

    // statistics for Table.
    @Getter
    private final Map<String, Map<OpsType, AtomicLong>> succeedCounter    = new ConcurrentHashMap<>();
    private final Map<String, Map<OpsType, AtomicLong>> failedCounter     = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, Map<OpsType, AtomicLong>> affectRowsCounter = new ConcurrentHashMap<>();

    // statistics for Threads.
    private final List<Thread>                          producerThreads   = new CopyOnWriteArrayList<>();
    private final List<Thread>                          writerThreads     = new CopyOnWriteArrayList<>();
    private final Map<String, AtomicLong>               writerTotal       = new ConcurrentHashMap<>();
    private final AtomicLong                            workingTimeMs     = new AtomicLong(0);
    private final MovingAverage                         writerAverage     = new MovingAverage(100);

    private long                                        startCostTimeMs;
    private final AtomicReference<FakerRunStatus>       monitorCostStatus = new AtomicReference<>(FakerRunStatus.INIT);

    // statistics for fakerJob.
    private final boolean                               printSql;
    private final EventQueue                            eventQueue;
    private final List<ErrorMessage>                    lastException     = new CopyOnWriteArrayList<>();
    private final int                                   KEEP_ERROR_SIZE   = 200;
    private final int                                   KEEP_TIME_MS      = 3000;

    FakerMonitor(EventQueue eventQueue, boolean printSql){
        this.eventQueue = eventQueue;
        this.printSql = printSql;
    }

    // ------------------------------------------------------------------------
    //                                                    Statistics for Table.
    // ------------------------------------------------------------------------

    public long getSucceedInsert() {
        AtomicLong result = new AtomicLong(0);
        this.succeedCounter.forEach((tab, opsCounter) -> {
            result.getAndAdd(opsCounter.getOrDefault(OpsType.Insert, ZERO).get());
        });
        return result.get();
    }

    public long getSucceedUpdate() {
        AtomicLong result = new AtomicLong(0);
        this.succeedCounter.forEach((tab, opsCounter) -> {
            result.getAndAdd(opsCounter.getOrDefault(OpsType.Update, ZERO).get());
        });
        return result.get();
    }

    public long getSucceedDelete() {
        AtomicLong result = new AtomicLong(0);
        this.succeedCounter.forEach((tab, opsCounter) -> {
            result.getAndAdd(opsCounter.getOrDefault(OpsType.Delete, ZERO).get());
        });
        return result.get();
    }

    public long getFailedInsert() {
        AtomicLong result = new AtomicLong(0);
        this.failedCounter.forEach((tab, opsCounter) -> {
            result.getAndAdd(opsCounter.getOrDefault(OpsType.Insert, ZERO).get());
        });
        return result.get();
    }

    public long getFailedUpdate() {
        AtomicLong result = new AtomicLong(0);
        this.failedCounter.forEach((tab, opsCounter) -> {
            result.getAndAdd(opsCounter.getOrDefault(OpsType.Update, ZERO).get());
        });
        return result.get();
    }

    public long getFailedDelete() {
        AtomicLong result = new AtomicLong(0);
        this.failedCounter.forEach((tab, opsCounter) -> {
            result.getAndAdd(opsCounter.getOrDefault(OpsType.Delete, ZERO).get());
        });
        return result.get();
    }

    public long getSucceed() {
        AtomicLong result = new AtomicLong(0);
        this.succeedCounter.forEach((tab, opsCounter) -> {
            opsCounter.forEach((opsType, counter) -> {
                result.getAndAdd(counter.get());
            });
        });
        return result.get();
    }

    public long getFailed() {
        AtomicLong result = new AtomicLong(0);
        this.failedCounter.forEach((tab, opsCounter) -> {
            opsCounter.forEach((opsType, counter) -> {
                result.getAndAdd(counter.get());
            });
        });
        return result.get();
    }

    void recordMonitor(String writerID, String tranID, BoundQuery event, int affectRows, long writeCost) {
        String table = event.getTableInfo().getTable();
        OpsType opsType = event.getOpsType();
        int records = event.getRecords();

        Map<OpsType, AtomicLong> recordsMap = this.succeedCounter.computeIfAbsent(table, s -> new ConcurrentHashMap<>());
        Map<OpsType, AtomicLong> affectMap = this.affectRowsCounter.computeIfAbsent(table, s -> new ConcurrentHashMap<>());

        recordsMap.computeIfAbsent(opsType, s -> new AtomicLong()).addAndGet(affectRows);
        affectMap.computeIfAbsent(opsType, s -> new AtomicLong()).addAndGet(affectRows);
        this.writerTotal.computeIfAbsent(writerID, s -> new AtomicLong()).addAndGet(affectRows);

        this.writerAverage.next(writeCost);
    }

    void recordFailed(String writerID, String tranID, BoundQuery event, Exception e) {
        String table = event.getTableInfo().getTable();
        OpsType opsType = event.getOpsType();
        int rowCount = event.getRecords();

        Map<OpsType, AtomicLong> rowCntMap = this.failedCounter.computeIfAbsent(table, s -> new ConcurrentHashMap<>());
        //Map<OpsType, AtomicLong> affectMap = this.affectRowsCounter.computeIfAbsent(table, s -> new ConcurrentHashMap<>());

        rowCntMap.computeIfAbsent(opsType, s -> new AtomicLong()).addAndGet(rowCount);
        //affectMap.computeIfAbsent(opsType, s -> new AtomicLong()).addAndGet(0);

        this.printError(event, e);
    }

    private void printError(BoundQuery event, Exception e) {
        String table = event.getTableInfo().getTable();
        OpsType opsType = event.getOpsType();

        ErrorMessage msg = new ErrorMessage(String.format("table %s %s ignore writer failed, ", table, opsType) + e.getMessage());
        synchronized (this.lastException) {
            if (this.lastException.contains(msg)) {
                ErrorMessage message = this.lastException.get(this.lastException.indexOf(msg));
                if ((message.getTimestamp() + KEEP_TIME_MS) > System.currentTimeMillis()) {
                    return;
                } else {
                    message.updateTimestamp();
                }
            } else {
                this.lastException.add(msg);
            }
        }

        logger.error(msg.getMessageKey());

        if (this.printSql) {
            String sqlString = event.getSqlString();
            SqlArg[] sqlArgs = event.getArgs();

            logger.info("write error " + e.getMessage() + ", sql " + sqlString);
        }
    }
    // ------------------------------------------------------------------------
    //                                                  Statistics for Threads.
    // ------------------------------------------------------------------------

    public long getWorkingTimeMs() { return this.workingTimeMs.get(); }

    public double getWriteAvgTime() {
        try {
            return BigDecimal.valueOf(this.writerAverage.getAvg()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return this.writerAverage.getAvg();
        }
    }

    public synchronized void startCost() {
        if (this.monitorCostStatus.compareAndSet(FakerRunStatus.INIT, FakerRunStatus.RUNNING)) {
            this.workingTimeMs.set(0);
            this.startCostTimeMs = System.currentTimeMillis();
        }
    }

    public synchronized void pauseCost() {
        if (this.monitorCostStatus.compareAndSet(FakerRunStatus.RUNNING, FakerRunStatus.PAUSE)) {
            long addon = System.currentTimeMillis() - this.startCostTimeMs;
            this.workingTimeMs.getAndAdd(addon);
        }
    }

    public synchronized void resumeCost() {
        if (this.monitorCostStatus.compareAndSet(FakerRunStatus.PAUSE, FakerRunStatus.RUNNING)) {
            this.startCostTimeMs = System.currentTimeMillis();
        }
    }

    public synchronized void stopCost() {
        if (this.monitorCostStatus.compareAndSet(FakerRunStatus.RUNNING, FakerRunStatus.INIT)) {
            long addon = System.currentTimeMillis() - this.startCostTimeMs;
            this.workingTimeMs.getAndAdd(addon);
        } else if (this.monitorCostStatus.compareAndSet(FakerRunStatus.PAUSE, FakerRunStatus.INIT)) {
            // there is nothing todo
        }
    }

    private synchronized void refreshCost() {
        if (this.monitorCostStatus.get() == FakerRunStatus.RUNNING) {
            long addon = System.currentTimeMillis() - this.startCostTimeMs;
            this.startCostTimeMs = System.currentTimeMillis();
            this.workingTimeMs.getAndAdd(addon);
        }
    }

    void producerStart(String producerID, Thread workThread) {
        this.producerThreads.add(workThread);
    }

    void writerStart(String writerID, Thread workThread) {
        this.writerThreads.add(workThread);
    }

    // ------------------------------------------------------------------------
    //                                                  Statistics for Other.
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        this.refreshCost();

        long succeedTotal = this.getSucceed();
        long failedTotal = this.getFailed();
        long passedTimeSec = Math.max(1, this.getWorkingTimeMs() / 1000);
        long writerTotal = succeedTotal + failedTotal;
        long perWriterAvg = this.writerTotal.isEmpty() ? 0 : (writerTotal / this.writerTotal.size());

        int queueCapacity = this.eventQueue.getCapacity();
        int queueSize = this.eventQueue.getQueueSize();
        int queueDutyRatio = (int) (((double) queueSize / (double) queueCapacity) * 100);

        int producerRunningCnt = (int) this.producerThreads.stream().map(Thread::getState).filter(state -> state == Thread.State.RUNNABLE).count();
        int writerRunningCnt = (int) this.writerThreads.stream().map(Thread::getState).filter(state -> state == Thread.State.RUNNABLE).count();
        int producerDutyRatio = (int) (((double) producerRunningCnt / (double) this.producerThreads.size()) * 100);
        int writerDutyRatio = (int) (((double) writerRunningCnt / (double) this.writerThreads.size()) * 100);

        return String.format("Succeed[I/U/D] %s/%s/%s, Failed[I/U/D] %s/%s/%s, RPS(s)[per/sum] %s/%s, total/affect %s/%s, load[Q/P/W] %d%%/%d%%/%d%%", //
                getSucceedInsert(), getSucceedUpdate(), getSucceedDelete(),     // Succeed[I/U/D]
                getFailedInsert(), getFailedUpdate(), getFailedDelete(),        // Failed[I/U/D]
                (perWriterAvg / passedTimeSec), (writerTotal / passedTimeSec),  // RPS[perWriter/total]
                writerTotal, affectRowsCounter,                                 // total/affect
                queueDutyRatio, producerDutyRatio, writerDutyRatio              // dutyRatio[Q/P/W] -> queue/producer/writer
        );
    }

    public void reset() {
        this.succeedCounter.clear();
        this.failedCounter.clear();
        this.affectRowsCounter.clear();

        this.producerThreads.clear();
        this.writerThreads.clear();
        this.writerTotal.clear();

        this.workingTimeMs.set(0);
        this.startCostTimeMs = 0;
        this.monitorCostStatus.set(FakerRunStatus.INIT);
    }

    public String getInformation(OpsType[] includeTypes) {
        this.refreshCost();
        StringBuilder builder = new StringBuilder();

        ZonedDateTime utcTime = new Date(this.getWorkingTimeMs()).toInstant().atZone(ZoneId.of("UTC"));
        builder.append("run time " + WellKnowFormat.WKF_TIME24.toPattern().format(utcTime));

        // succeed logger
        Map<OpsType, AtomicLong> allSucceedCounter = new HashMap<>();
        this.succeedCounter.forEach((tab, opsCounter) -> {
            if (opsCounter.containsKey(OpsType.Insert)) {
                allSucceedCounter.computeIfAbsent(OpsType.Insert, opsType -> new AtomicLong()).getAndAdd(opsCounter.get(OpsType.Insert).get());
            }
            if (opsCounter.containsKey(OpsType.Update)) {
                allSucceedCounter.computeIfAbsent(OpsType.Update, opsType -> new AtomicLong()).getAndAdd(opsCounter.get(OpsType.Update).get());
            }
            if (opsCounter.containsKey(OpsType.Delete)) {
                allSucceedCounter.computeIfAbsent(OpsType.Delete, opsType -> new AtomicLong()).getAndAdd(opsCounter.get(OpsType.Delete).get());
            }
        });
        builder.append(" Succeed[" + StringUtils.join(Arrays.stream(includeTypes).map(Enum::name).toArray(), "/") + "] ");
        for (int i = 0; i < includeTypes.length; i++) {
            if (i == 0) {
                builder.append(String.format("%s", allSucceedCounter.getOrDefault(includeTypes[i], ZERO).get()));
            } else {
                builder.append(String.format("/%s", allSucceedCounter.getOrDefault(includeTypes[i], ZERO).get()));
            }
        }

        // failed logger
        Map<OpsType, AtomicLong> allFailedCounter = new HashMap<>();
        this.failedCounter.forEach((tab, opsCounter) -> {
            if (opsCounter.containsKey(OpsType.Insert)) {
                allFailedCounter.computeIfAbsent(OpsType.Insert, opsType -> new AtomicLong()).getAndAdd(opsCounter.get(OpsType.Insert).get());
            }
            if (opsCounter.containsKey(OpsType.Update)) {
                allFailedCounter.computeIfAbsent(OpsType.Update, opsType -> new AtomicLong()).getAndAdd(opsCounter.get(OpsType.Update).get());
            }
            if (opsCounter.containsKey(OpsType.Delete)) {
                allFailedCounter.computeIfAbsent(OpsType.Delete, opsType -> new AtomicLong()).getAndAdd(opsCounter.get(OpsType.Delete).get());
            }
        });
        builder.append(", Failed[" + StringUtils.join(Arrays.stream(includeTypes).map(Enum::name).toArray(), "/") + "] ");
        for (int i = 0; i < includeTypes.length; i++) {
            if (i == 0) {
                builder.append(String.format("%s", allFailedCounter.getOrDefault(includeTypes[i], ZERO).get()));
            } else {
                builder.append(String.format("/%s", allFailedCounter.getOrDefault(includeTypes[i], ZERO).get()));
            }
        }
        builder.append(" records");
        builder.append(", wAvgTime " + this.getWriteAvgTime() + "ms");

        return builder.toString();
    }

    public void clearLastException() {
        int size = this.lastException.size();
        if (size > KEEP_ERROR_SIZE) {
            synchronized (this.lastException) {
                this.lastException.sort(Comparator.comparingLong(ErrorMessage::getTimestamp));
                List<ErrorMessage> keep = new ArrayList<>(this.lastException.subList(size - (KEEP_ERROR_SIZE / 2), size));
                this.lastException.clear();
                this.lastException.addAll(keep);
            }
        }
    }
}
