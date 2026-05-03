package com.clougence.clouddm.sdk.execute.session;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultMessageBuild;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.timer.Timer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDsSession implements Session {

    protected final SessionHook              sessionHook;
    // self info
    private final String                     newSessionId;
    private final DataSourceConfig           dsConfig;
    private final AtomicBoolean              executing = new AtomicBoolean(false);
    private final List<SessionCloseListener> closeListenerList;
    // context and env
    protected Timer                          globalTimer;
    private DsMetaService                    metaService;
    // execute
    private long                             lastRequestTime;

    public AbstractDsSession(String newSessionId, DataSourceConfig dsConfig, SessionHook sessionHook){
        this.newSessionId = Objects.requireNonNull(newSessionId, "newSessionId is null.");
        this.dsConfig = Objects.requireNonNull(dsConfig, "dsConfig is null.");
        this.sessionHook = Objects.requireNonNull(sessionHook, "sessionHook is null.");
        this.lastRequestTime = System.currentTimeMillis();
        this.executing.set(false);
        this.closeListenerList = new ArrayList<>();
    }

    public void initSession(DsResourceManager rm, SessionContextDTO initContextDTO) throws Exception {
        this.globalTimer = rm.getTimer(this.getDsConfig());
        this.metaService = this.sessionHook.createMetaService(this);
        this.sessionHook.configSession(this.currentResource(), initContextDTO);
    }

    @Override
    public void addCloseListener(SessionCloseListener closeListener) {
        this.closeListenerList.add(closeListener);
    }

    @Override
    public String getSessionId() { return this.newSessionId; }

    @Override
    public long getLastQueryTime() { return this.lastRequestTime; }

    @Override
    public DataSourceConfig getDsConfig() { return this.dsConfig; }

    @Override
    public DsMetaService getMetaService() { return this.metaService; }

    @Override
    public boolean isExecuting() { return this.executing.get(); }

    protected final void markExecutingFinish() {
        this.executing.set(false);
    }

    @Override
    public <V> V executeQuery(SessionCallback<V> callback) throws Exception {
        if (!this.executing.compareAndSet(false, true)) {
            throw new RuntimeException("previous COMMAND is not finish yet.");
        }

        try {
            this.lastRequestTime = System.currentTimeMillis();
            return callback.doCallback(this.currentResource());
        } finally {
            this.executing.set(false);
        }
    }

    private void waitReceiveMessage(AtomicBoolean signal, long startTime, QueryRequest query, ResultBuilder builder) {
        if (!this.executing.get() || signal.get()) {
            return;
        }

        long passedTimeMs = System.currentTimeMillis() - startTime;
        String messageString;
        if (passedTimeMs < 1000) {
            messageString = passedTimeMs + " ms";
        } else if (passedTimeMs < 60 * 1000) {
            double seconds = (double) passedTimeMs / 1000.0;
            messageString = String.format("%.1f seconds", seconds);
        } else if (passedTimeMs < 60 * 60 * 1000) {
            long totalSeconds = passedTimeMs / 1000;
            long minutes = totalSeconds / 60;
            long remainingSeconds = totalSeconds % 60;
            messageString = String.format("%d minutes %d seconds", minutes, remainingSeconds);
        } else {
            long totalSeconds = passedTimeMs / 1000;
            long hours = totalSeconds / 3600;
            long remainingMinutes = (totalSeconds % 3600) / 60;
            messageString = String.format("%d hour %d minutes", hours, remainingMinutes);
        }

        ResultMessageBuild rb = builder.newMessage(query);
        rb.receiveMessage(MessageLevel.Info, "Waiting for receive result. (" + messageString + " have passed.) ");
        rb.finishRecord(true);

        if (!signal.get()) {
            this.globalTimer.newTimeout(timeout -> {
                waitReceiveMessage(signal, startTime, query, builder);
            }, 3000, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public final void executeQuery(QueryRequest query, final ResultBuilder builder) {
        if (!this.executing.compareAndSet(false, true)) {
            throw new RuntimeException("previous COMMAND is not finish yet.");
        }

        final long beginTime = System.currentTimeMillis();
        this.lastRequestTime = System.currentTimeMillis();
        // wait receive message.
        final AtomicBoolean receiveSignal = new AtomicBoolean(false);
        this.globalTimer.newTimeout(timeout -> {
            waitReceiveMessage(receiveSignal, beginTime, query, builder);
        }, 300, TimeUnit.MILLISECONDS);

        // do query.
        try {
            beforeQueryRequest(beginTime, query, builder);
            execQuery(beginTime, query, builder, receiveSignal);
            afterQueryRequest(beginTime, query, builder);
            this.triggerCompleted();
        } catch (Exception e) {
            receiveSignal.set(true);
            throwQueryRequest(beginTime, query, builder, e);
            triggerFailed(e);
        }
    }

    private void triggerCompleted() {
        if (!this.executing.compareAndSet(true, false)) {
            log.warn("triggerCompleted, session '" + getSessionId() + "' No SQL in execution.");
            return;
        }

        log.info("triggerCompleted, session '" + getSessionId());
    }

    private void triggerFailed(Throwable ex) {
        if (!this.executing.compareAndSet(true, false)) {
            log.warn("triggerFailed, session '" + getSessionId() + "' No SQL in execution.");
            return;
        }

        log.warn("triggerFailed, session '" + getSessionId() + ", message " + ExceptionUtils.getRootCauseMessage(ex), ex);
    }

    protected void triggerCloseListener() {
        for (SessionCloseListener listener : this.closeListenerList) {
            listener.onClose(this.getSessionId());
        }
    }

    protected abstract Connection currentResource();

    protected abstract void beforeQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws Exception;

    protected abstract void execQuery(long beginTime, QueryRequest query, ResultBuilder builder, AtomicBoolean receiveSignal) throws Exception;

    protected abstract void afterQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws Exception;

    protected abstract void throwQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder, Exception e);
}
