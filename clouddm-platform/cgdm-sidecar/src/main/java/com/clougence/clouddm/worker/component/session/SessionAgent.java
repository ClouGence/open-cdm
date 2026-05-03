package com.clougence.clouddm.worker.component.session;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.clouddm.api.sidecar.session.execute.AsyncWaitResult;
import com.clougence.clouddm.api.sidecar.session.execute.ResultPhaseOfBatch;
import com.clougence.clouddm.api.sidecar.session.execute.StatusDTO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.*;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.PhaseBuild;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultMessage;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhase;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhaseType;
import com.clougence.clouddm.worker.component.notify.SidecarSqlNotifyService;
import com.clougence.clouddm.worker.component.session.result.BatchBuild;
import com.clougence.clouddm.worker.component.session.result.DefaultResultBuilder;
import com.clougence.clouddm.worker.component.session.result.ResultListenerContainer;
import com.clougence.clouddm.worker.component.session.result.ResultListenerKey;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionAgent implements Session {

    // service and config
    private final SessionSupport          ss;
    private final SidecarSqlNotifyService notifyService;
    private final Executor                queryExecutor;
    private final int                     maxIdleTimeSec;
    // status
    private final StatusDTO               statusMap;
    private volatile boolean              recycling;
    private final AtomicBoolean           running;
    // self
    private final Session                 session;
    private final Queue<Result>           resultCache;
    private final DefaultResultBuilder    resultBuilder;
    private final Queue<QueryBatch>       activeQueue = new ConcurrentLinkedQueue<>();
    private volatile QueryBatch           current     = null;

    public SessionAgent(Session session, SessionSupport ss, DsResourceManager rm, int maxIdleTimeSec){
        this.session = Objects.requireNonNull(session, "session is null.");
        this.resultCache = new ConcurrentLinkedQueue<>();
        this.ss = ss;
        this.notifyService = ss.getNotifyService();
        this.queryExecutor = rm.getExecutor(session.getDsConfig());
        this.maxIdleTimeSec = maxIdleTimeSec;
        this.statusMap = new StatusDTO();
        this.statusMap.setCreateTime(System.currentTimeMillis());
        this.statusMap.setMaxIdleTimeSec(maxIdleTimeSec);
        this.recycling = false;
        this.running = new AtomicBoolean(false);

        ResultListenerContainer container = new ResultListenerContainer();
        container.addListener(ResultListenerKey.RESULT_LISTENER, this::watchResult);
        container.addListener(ResultListenerKey.AUDIT_LISTENER, this::watchAudit);
        this.resultBuilder = new DefaultResultBuilder(this.session.getSessionId(), this.ss, container);

        this.updateStatus();
    }

    private void watchResult(QueryRequest query, Result res) {
        this.resultCache.add(res);
        this.statusMap.setEventGoods(this.statusMap.getEventGoods() + 1);
    }

    private void watchAudit(QueryRequest query, Result res) {
        switch (res.getResultType()) {
            case Phase: {
                if (!(res instanceof ResultPhaseOfBatch)) {
                    if (((ResultPhase) res).getPhaseType() == ResultPhaseType.After) {
                        this.notifyService.finishForConsoleQuery(query, res);
                    }
                }
                break;
            }
            // fail
            case Message: {
                ResultMessage resultMessage = (ResultMessage) res;
                if (!resultMessage.isNotify()) {
                    return;
                }
                if (resultMessage.getLevel() == MessageLevel.Error) {
                    this.notifyService.finishForConsoleQuery(query, res);
                } else if (resultMessage.getLevel() == MessageLevel.Info) {
                    this.notifyService.finishForConsoleQuery(query, res);
                }
                break;
            }
            case ResultCount: {
                this.notifyService.finishForConsoleQuery(query, res);
                break;
            }
        }
    }

    // ---------------------------------------

    private void updateStatus() {
        try {
            this.statusMap.setCurCatalog(this.session.getMetaService().getCurrentCatalog());
            this.statusMap.setCurSchema(this.session.getMetaService().getCurrentSchema());
            this.statusMap.setAutoCommit(this.session.isAutoCommit());
            this.statusMap.setReadOnly(this.session.isReadOnly());
            this.statusMap.setIsolation(this.session.getIsolation());
            this.statusMap.setHasUnCommitted(this.session.hasUnCommitted());
        } catch (Exception e) {
            log.error("session update status failed " + e.getMessage(), e);
        }
    }

    public StatusDTO getSessionStatus() { return this.statusMap; }

    public boolean tryIdle() {
        synchronized (this.activeQueue) {
            long lastTime = Math.max(this.statusMap.getCreateTime(), this.statusMap.getLastRequestTime());
            long timeout = lastTime + (this.maxIdleTimeSec * 1000L);
            boolean check = timeout <= System.currentTimeMillis() && !this.statusMap.isExecuting();

            if (check) {
                this.recycling = true;
            }
            return check;
        }
    }

    // ----------- begin for Query -----------

    private void batchPhase(String batchId, ResultPhaseType phase, long cost) {
        BatchBuild build = this.resultBuilder.newBatch(batchId);
        switch (phase) {
            case Before:
                build.onBegin();
                break;
            case After:
                build.onEnd();
                break;
            case Cancel:
                build.onCancel();
                break;
            default:
                break;
        }
        build.collectCost(cost);
        build.finishRecord(true);
    }

    private void queryPhase(QueryRequest request, ResultPhaseType phase, long cost) {
        PhaseBuild build = this.resultBuilder.newPhase(request);
        build.onPhase(phase);
        build.collectCost(cost);

        if (phase == ResultPhaseType.Before) {
            List<QueryArg> args = request.getQueryArgs();
            if (CollectionUtils.isEmpty(args)) {
                build.finishRecord(true, request.getQueryBody(), null);
            } else {
                build.finishRecord(true, request.getQueryBody() + " using args [" + StringUtils.join(args.toArray(), ", ") + "]", null);
            }
        } else if (phase == ResultPhaseType.After) {
            build.finishRecord(true);
        } else if (phase == ResultPhaseType.Cancel) {
            build.finishRecord(true);
        }
    }

    public synchronized AsyncWaitResult submitQueries(String batchId, List<QueryRequest> queryRequest) {
        Objects.requireNonNull(batchId, "batchId is null.");
        AsyncWaitResult result = new AsyncWaitResult();
        result.setSessionId(this.session.getSessionId());

        if (this.recycling) {
            result.setSuccess(false);
            result.setMessage("The session has ended and been deleted.");
            return result;
        }

        if (queryRequest.isEmpty()) {
            this.batchPhase(batchId, ResultPhaseType.Before, 0);
            this.batchPhase(batchId, ResultPhaseType.After, 0);
            result.setSuccess(true);
            return result;
        } else {
            synchronized (this.activeQueue) {
                this.statusMap.setExecuting(true);
                for (QueryRequest request : queryRequest) {
                    request.setBatchId(batchId);
                }
                this.activeQueue.add(new QueryBatch(batchId, new LinkedList<>(queryRequest)));
                this.statusMap.setWaitBatchSize(this.activeQueue.size());
                this.statusMap.setWaitQuerySize(queryRequest.size());
            }

            try {
                if (this.running.compareAndSet(false, true)) {
                    this.asyncExecQuery();
                }
                result.setSuccess(true);
                return result;
            } catch (Exception e) {
                log.error("submitQueries failed " + e.getMessage(), e);
                result.setSuccess(false);
                result.setMessage(e.getMessage());
                return result;
            }
        }
    }

    @Override
    public void cancel() {
        synchronized (this.activeQueue) {
            if (this.current != null) {
                this.current.setCanceled(true);
            }
            for (QueryBatch queryBatch : this.activeQueue) {
                queryBatch.setCanceled(true);
            }
        }

        if (this.session.isExecuting()) {
            ThreadUtils.safeSleep(200); // wait for auto exit
            if (this.session.isExecuting()) {
                this.session.cancel();
            }
        }
    }

    private QueryBatch getOrNextBatch() {
        synchronized (this.activeQueue) {
            if (this.current == null) {
                QueryBatch nextBatch = this.activeQueue.poll();
                if (nextBatch == null) {
                    this.statusMap.setExecuting(false);
                    this.statusMap.setWaitBatchSize(0);
                    return null;
                }

                this.statusMap.setWaitBatchSize(this.activeQueue.size());
                this.statusMap.setWaitQuerySize(nextBatch.getRequests().size());
                this.current = nextBatch;
                nextBatch.setBeginTime(System.currentTimeMillis());
                this.batchPhase(this.current.getBatchId(), ResultPhaseType.Before, 0);
            } else if (this.current.getRequests().isEmpty()) {
                this.current = null;
                this.statusMap.setWaitQuerySize(0);
                return this.getOrNextBatch();
            }
        }
        return this.current;
    }

    private void asyncExecQuery() {
        this.queryExecutor.execute(this::doQuery);
    }

    private void doQuery() {
        QueryBatch batch = getOrNextBatch();
        if (batch == null) {
            this.running.set(false);
            return;
        }

        if (batch.isCanceled()) {
            QueryRequest needOne = batch.pollRequest();
            long cose = System.currentTimeMillis() - batch.getBeginTime();
            this.failedAndContinue(batch, needOne, cose, null, true);
            return;
        }

        long time = System.currentTimeMillis();
        QueryRequest request = batch.pollRequest();
        this.queryPhase(request, ResultPhaseType.Before, 0);
        this.ss.getNotifyService().beginForConsoleQuery(request, this.session.getSessionId());

        try {
            this.statusMap.setCurBatchId(request.getBatchId());
            this.statusMap.setCurQueryId(request.getQueryId());
            this.statusMap.setLastRequestTime(System.currentTimeMillis());

            try {
                this.session.executeQuery(request, this.resultBuilder);
            } finally {
                this.resultBuilder.getListenerContainer().enableAll(); // reset listeners
                this.updateStatus();
            }
            this.finishAndContinue(batch, request, time);
        } catch (Throwable e) {
            this.failedAndContinue(batch, request, time, e, false);
        }
    }

    private void finishAndContinue(QueryBatch batch, QueryRequest request, long time) {
        this.statusMap.setCurBatchId(null);
        this.statusMap.setCurQueryId(null);
        this.statusMap.setWaitQuerySize(this.statusMap.getWaitQuerySize() - 1);

        long cost = System.currentTimeMillis() - time;
        this.queryPhase(request, ResultPhaseType.After, cost);

        if (batch.getRequests().isEmpty()) {
            cost = System.currentTimeMillis() - batch.getBeginTime();
            this.batchPhase(batch.getBatchId(), ResultPhaseType.After, cost);
        }

        // use ResultListenerKey.AUDIT_LISTENER to this.ss.getNotifyService().finishForConsoleQuery(request, this.session.getSessionId());
        this.asyncExecQuery();
    }

    private void failedAndContinue(QueryBatch batch, QueryRequest request, long time, Throwable e, boolean isCancel) {
        this.statusMap.setCurBatchId(null);
        this.statusMap.setCurQueryId(null);
        this.statusMap.setWaitQuerySize(this.statusMap.getWaitQuerySize() - 1);

        long cost = System.currentTimeMillis() - time;
        if (isCancel) {
            this.queryPhase(request, ResultPhaseType.Cancel, cost);
        } else {
            this.queryPhase(request, ResultPhaseType.After, cost);
        }

        if (batch.getRequests().isEmpty()) {
            cost = System.currentTimeMillis() - batch.getBeginTime();
            this.batchPhase(batch.getBatchId(), ResultPhaseType.After, cost);
        }

        // use ResultListenerKey.AUDIT_LISTENER to this.ss.getNotifyService().finishForConsoleQuery(request, this.session.getSessionId());
        this.asyncExecQuery();
    }

    //
    //
    //

    @Deprecated
    public long getUpdateCount(PreparedStatement ps) throws SQLException {
        if (this.session instanceof DefaultRdbSession) {
            DefaultRdbSession rdbSession = (DefaultRdbSession) session;
            return rdbSession.getUpdateCount(ps);
        } else {
            return ps.getLargeUpdateCount();
        }
    }

    @Override
    public void close() throws Exception {
        this.cancel();
        this.resultBuilder.finished();
        this.session.close();

    }

    // ----------- end for Query -----------
    //
    // ----------- begin for Session -----------

    public DsMetaService getMetaService() { return this.session.getMetaService(); }

    @Override
    public DataSourceConfig getDsConfig() { return this.session.getDsConfig(); }

    private void checkExecuting() {
        if (this.statusMap.isExecuting()) {
            throw new IllegalStateException("session '" + this.getSessionId() + "' is executing.");
        }
    }

    @Override
    public <V> V executeQuery(SessionCallback<V> callback) throws Exception {
        try {
            this.checkExecuting();
            return this.session.executeQuery(callback);
        } finally {
            this.updateStatus();
        }
    }

    public void commit() {
        try {
            this.checkExecuting();
            this.session.commit();
        } finally {
            this.updateStatus();
        }
    }

    public void rollback() {
        try {
            this.checkExecuting();
            this.session.rollback();
        } finally {
            this.updateStatus();
        }
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            this.checkExecuting();
            this.session.setAutoCommit(autoCommit);
        } finally {
            this.updateStatus();
        }
    }

    public void setIsolation(RdbIsolation isolation) {
        try {
            this.checkExecuting();
            this.session.setIsolation(isolation);
        } finally {
            this.updateStatus();
        }
    }

    public void setReadOnly(boolean readOnly) {
        try {
            this.checkExecuting();
            this.session.setReadOnly(readOnly);
        } finally {
            this.updateStatus();
        }
    }

    public void setCurrentCatalog(String catalog) {
        try {
            this.checkExecuting();
            this.session.setCurrentCatalog(catalog);
        } finally {
            this.updateStatus();
        }
    }

    public void setCurrentSchema(String schema) {
        try {
            this.checkExecuting();
            this.session.setCurrentSchema(schema);
        } finally {
            this.updateStatus();
        }
    }

    public String getSessionId() { return this.session.getSessionId(); }

    public String getCurrentQueryId() { return this.statusMap.getCurQueryId(); }

    public long getLastQueryTime() { return this.statusMap.getLastRequestTime(); }

    public boolean isExecuting() { return this.statusMap.isExecuting(); }

    public boolean isAutoCommit() { return this.statusMap.isAutoCommit(); }

    public boolean isReadOnly() { return this.statusMap.isReadOnly(); }

    public RdbIsolation getIsolation() { return this.statusMap.getIsolation(); }

    public boolean hasUnCommitted() {
        return this.statusMap.isHasUnCommitted();
    }

    // ----------- end for Session -----------
    //
    // ----------- begin for Result -----------

    public List<Result> popList() {
        if (this.resultCache.isEmpty()) {
            return Collections.emptyList();
        }

        List<Result> result = new ArrayList<>();
        while (!this.resultCache.isEmpty()) {
            result.add(this.resultCache.poll());
        }
        this.statusMap.setEventGoods(this.resultCache.size());
        return result;
    }

    public boolean hasMore() {
        return !this.resultCache.isEmpty();
    }

    // ----------- end for Result -----------

    @Override
    public void executeQuery(QueryRequest query, ResultBuilder rb) {
        throw new UnsupportedOperationException("SessionAgent not support");
    }

    @Override
    public void addCloseListener(SessionCloseListener closeListener) {
        throw new UnsupportedOperationException("SessionAgent not support");
    }
}
