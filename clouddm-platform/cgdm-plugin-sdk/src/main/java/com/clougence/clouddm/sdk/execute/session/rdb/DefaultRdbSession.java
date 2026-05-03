package com.clougence.clouddm.sdk.execute.session.rdb;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhaseType;
import com.clougence.clouddm.sdk.execute.session.*;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.*;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.drivers.DsObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.FileUtils;
import com.clougence.utils.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2022/2/17 17:02:59
 */
@Slf4j
public class DefaultRdbSession extends AbstractDsSession implements Session, KillCurrentQueryAble {

    private final DsObject<Connection> dsObject;
    private final AtomicBoolean        rdbCancelSignal;
    protected boolean                  rdbAutoCommit;
    protected boolean                  rdbReadOnly;
    protected RdbIsolation             rdbIsolation;
    private ColReader                  colReader;
    // status
    private String                     rdbConnectionId;
    private boolean                    rdbHasUnCommitted;
    private long                       rdbLastTestTime;

    public DefaultRdbSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject, SessionHook sessionHook){
        super(newSessionId, dsConfig, sessionHook);
        this.dsObject = dsObject;
        this.dsObject.addCloseListener(this::triggerCloseListener);
        this.rdbCancelSignal = new AtomicBoolean(false);
    }

    @Override
    public void initSession(DsResourceManager rm, SessionContextDTO initContextDTO) throws Exception {
        super.initSession(rm, initContextDTO);

        Connection conn = this.dsObject.getTarget();

        this.colReader = rdbHook().createColReader();
        this.rdbConnectionId = getCurrentQueryId();
        this.rdbCancelSignal.set(false);
        this.rdbAutoCommit = this.rdbHook().isAutoCommit(conn);
        this.rdbReadOnly = this.rdbHook().isReadOnly(conn);
        this.rdbIsolation = this.rdbHook().getIsolation(conn);
        this.rdbHasUnCommitted = false;
        this.rdbLastTestTime = System.currentTimeMillis();
    }

    public DsMetaService getMetaService() { return super.getMetaService(); }

    public boolean isClose() { return this.dsObject.isClose(); }

    @Override
    protected Connection currentResource() {
        return this.dsObject.getTarget();
    }

    protected SessionHook rdbHook() {
        return this.sessionHook;
    }

    @Override
    public void commit() {
        try {
            rdbHook().commit(this.dsObject.getTarget());
            this.rdbHasUnCommitted = false;
        } catch (Exception e) {
            log.error("commit: " + e.getMessage(), e);
        }
    }

    @Override
    public void rollback() {
        try {
            rdbHook().rollback(this.dsObject.getTarget());
            this.rdbHasUnCommitted = false;
        } catch (Exception e) {
            log.error("rollback: " + e.getMessage(), e);
        }
    }

    @Override
    public void setCurrentCatalog(String catalog) {
        try {
            rdbHook().setCurrentCatalog(this.dsObject.getTarget(), catalog);
        } catch (Exception e) {
            log.error("setCurrentSchema: " + e.getMessage(), e);
            throw ExceptionUtils.toRuntime(e);
        }
    }

    @Override
    public void setCurrentSchema(String schemaName) {
        try {
            rdbHook().setCurrentSchema(this.dsObject.getTarget(), schemaName);
        } catch (Exception e) {
            log.error("setCurrentSchema: " + e.getMessage(), e);
            throw ExceptionUtils.toRuntime(e);
        }
    }

    @Override
    public boolean isAutoCommit() { return this.rdbAutoCommit; }

    @Override
    public void setAutoCommit(boolean autoCommit) {
        try {
            rdbHook().setAutoCommit(this.dsObject.getTarget(), autoCommit);
            this.rdbAutoCommit = rdbHook().isAutoCommit(this.dsObject.getTarget());
            if (this.rdbAutoCommit) {
                this.rdbHasUnCommitted = false;
            }
        } catch (Exception e) {
            log.error("setAutoCommit: " + e.getMessage(), e);
            throw ExceptionUtils.toRuntime(e);
        }
    }

    @Override
    public RdbIsolation getIsolation() { return this.rdbIsolation; }

    @Override
    public void setIsolation(RdbIsolation isolation) {
        try {
            rdbHook().setIsolation(this.dsObject.getTarget(), isolation);
            this.rdbIsolation = rdbHook().getIsolation(this.dsObject.getTarget());
        } catch (Exception e) {
            log.error("setIsolation: " + e.getMessage(), e);
            throw ExceptionUtils.toRuntime(e);
        }
    }

    @Override
    public boolean isReadOnly() { return this.rdbReadOnly; }

    @Override
    public void setReadOnly(boolean readOnly) {
        try {
            rdbHook().setReadOnly(this.dsObject.getTarget(), readOnly);
            this.rdbReadOnly = rdbHook().isReadOnly(this.dsObject.getTarget());
        } catch (Exception e) {
            log.error("setReadOnly: " + e.getMessage(), e);
            throw ExceptionUtils.toRuntime(e);
        }
    }

    @Override
    public boolean hasUnCommitted() {
        return !this.rdbAutoCommit && this.rdbHasUnCommitted;
    }

    @Override
    public String getCurrentQueryId() {
        if (StringUtils.isNotBlank(this.rdbConnectionId)) {
            return this.rdbConnectionId;
        }

        try {
            SessionHook rdbSessionHook = rdbHook();
            this.rdbConnectionId = rdbSessionHook.getQueryID(this.dsObject.getTarget());
            return this.rdbConnectionId;
        } catch (Exception e) {
            log.error("getCurrentQueryId: " + e.getMessage(), e);
            throw ExceptionUtils.toRuntime(e);
        }
    }

    @Override
    public void cancel() {
        if (!isExecuting()) {
            return;
        }

        if (this.rdbCancelSignal.compareAndSet(false, true)) {
            // kill.
            try {
                killCurrentQuery();
            } catch (Exception e) {
                log.error("cancel: " + e.getMessage(), e);
                throw ExceptionUtils.toRuntime(e);
            } finally {
                this.rdbCancelSignal.set(false);
            }
        } else {
            throw new IllegalStateException("the last cancel operation is in progress.");
        }
    }

    @Override
    public void killCurrentQuery() throws Exception {
        try (DsObject<Connection> newConnection = this.dsObject.unsafeSimilar()) {
            rdbHook().killProcess(newConnection.getTarget(), getCurrentQueryId());
        }
    }

    @Override
    public void close() {
        try {
            if (isExecuting()) {
                this.cancel();
            }
        } catch (Exception ignore) {

        } finally {
            doClose();
        }
    }

    protected void doClose() {
        if (!this.dsObject.isClose()) {
            this.dsObject.close();
        }
    }

    @Override
    protected void beforeQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {

    }

    @Override
    protected void execQuery(long beginTime, QueryRequest query, ResultBuilder builder, AtomicBoolean receiveSignal) throws Exception {
        Connection conn = this.currentResource();
        long exeStartTime = System.currentTimeMillis();
        Statement ps = null;
        boolean retVal;
        Exception ex = null;
        try {
            ps = this.createStatement(conn, query);

            int queryTimeoutMs = query.getResultConf().getQueryTimeoutSec() * 1000;
            if (queryTimeoutMs > 0) {
                ps.setQueryTimeout(queryTimeoutMs);
            }

            this.applyArgs(query, ps);
            retVal = this.executeStatement(ps, query, builder);
        } catch (Exception e) {
            ex = e;
            retVal = false;
        } finally {
            receiveSignal.set(true);
        }

        try {
            if (ex == null) {
                long fetchTimeMs = System.currentTimeMillis();
                try {
                    PhaseBuild rb = builder.newPhase(query);
                    rb.onPhase(ResultPhaseType.BeginReceive);
                    rb.collectCost(System.currentTimeMillis() - exeStartTime);
                    rb.finishRecord(true);

                    handleRs(query, retVal, ps, builder, this);
                } finally {
                    PhaseBuild rb = builder.newPhase(query);
                    rb.onPhase(ResultPhaseType.FinishReceive);
                    rb.collectCost(System.currentTimeMillis() - fetchTimeMs);
                    rb.finishRecord(true);
                }
            } else {
                ResultMessageBuild rb = builder.newMessage(query);
                rb.collectCost(System.currentTimeMillis() - exeStartTime);
                rb.receiveMessage(MessageLevel.Error, ex.getMessage());
                rb.finishRecord(true);
                throw ex;
            }

            if (this.rdbCancelSignal.get()) {
                ResultMessageBuild rb = builder.newMessage(query);
                rb.collectCost(System.currentTimeMillis() - exeStartTime);
                rb.receiveMessage(MessageLevel.Error, "The query has been cancelled.");
                rb.finishRecord(true);
            }
        } finally {
            IOUtils.closeQuietly(ps);

            if (!conn.isClosed() && query.getResultConf().isRefreshStatus()) {
                this.refreshStatus(conn);
            }

            this.rdbCancelSignal.set(false); // reset cancel signal
        }
    }

    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
        if (query.isUseExplain()) {
            query.setUsingValueProcess(false);
            return rdbHook().explainStatement(conn, query);
        } else {
            return rdbHook().executeStatement(conn, query);
        }
    }

    protected void refreshStatus(Connection conn) throws SQLException {
        if (!conn.isClosed()) {
            this.rdbReadOnly = this.rdbHook().isReadOnly(conn);
            this.rdbAutoCommit = this.rdbHook().isAutoCommit(conn);

            RdbIsolation isolation = this.rdbHook().getIsolation(conn);
            if (isolation != null) {
                this.rdbIsolation = isolation;
            }
        }
    }

    protected boolean executeStatement(Statement ps, QueryRequest query, ResultBuilder builder) throws SQLException {
        return ((PreparedStatement) ps).execute();
    }

    @Override
    protected void afterQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        this.rdbHasUnCommitted = !this.rdbAutoCommit;
    }

    @Override
    protected void throwQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder, Exception e) {

    }

    protected void applyArgs(QueryRequest query, Statement statement) throws SQLException {
        List<QueryArg> queryArgs = query.getQueryArgs();
        if (queryArgs == null) {
            return;
        }
        if (!query.isUseCallable() && statement instanceof PreparedStatement) {
            PreparedStatement ps = (PreparedStatement) statement;
            for (int i = 1; i <= queryArgs.size(); i++) {
                QueryArg argDTO = queryArgs.get(i - 1);
                ps.setObject(i, argDTO.getValue(), argDTO.getJdbcType());
            }
        }
    }

    protected void handleRs(QueryRequest query, boolean retVal, //
                            Statement ps, ResultBuilder builder, KillCurrentQueryAble killAble) throws SQLException, IOException {
        long fetchTimeMs = System.currentTimeMillis();
        QueryResultConf resultConf = query.getResultConf();

        if (retVal) {
            try (ResultSet rs = ps.getResultSet()) {
                String resultId = builder.newResultId(query);
                receiveResultRows(query, resultId, rs, builder);
            } catch (Throwable e) {
                ResultMessageBuild rb = builder.newMessage(query);
                rb.receiveMessage(MessageLevel.Error, e.getMessage());
                rb.collectCost(System.currentTimeMillis() - fetchTimeMs);
                rb.finishRecord(false, e.getMessage(), e);
                throw e;
            }
        } else {
            ResultCountBuild rb = builder.newResultCount(query);
            try {
                rb.receiveUpdateCount(getUpdateCount(ps));

                if (resultConf.isReturnAutoIncrKey()) {
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        receiveGeneratedKeys(keys, rb, query);
                    }
                }

                rb.collectCost(System.currentTimeMillis() - fetchTimeMs);
                rb.finishRecord(true);
            } catch (Exception e) {
                rb.collectCost(System.currentTimeMillis() - fetchTimeMs);
                rb.finishRecord(false, e.getMessage(), e);
                throw e;
            }

            // for callable
            ResultOutputBuild rbOut = null;
            try {
                if (query.isUseCallable()) {
                    rbOut = builder.newOutput(query);
                    receiveOutParams((CallableStatement) ps, rbOut, query);

                    rbOut.collectCost(System.currentTimeMillis() - fetchTimeMs);
                    rbOut.finishRecord(true);
                }
            } catch (Exception e) {
                if (rbOut == null) {
                    ResultMessageBuild rbMsg = builder.newMessage(query);
                    rbMsg.collectCost(0);
                    rbMsg.receiveMessage(MessageLevel.Error, e.getMessage());
                    rbMsg.finishRecord(true);
                } else {
                    rbOut.collectCost(System.currentTimeMillis() - fetchTimeMs);
                    rbOut.finishRecord(false, e.getMessage(), e);
                }
                throw e;
            }
        }

        // if skip more result then return
        if (!resultConf.isFetchMoreResult()) {
            return;
        }

        // mores
        while (!this.rdbCancelSignal.get()) {
            boolean moreResults = ps.getMoreResults();
            if (moreResults) {
                try (ResultSet rs = ps.getResultSet()) {
                    String resultId = builder.newResultId(query);
                    receiveResultRows(query, resultId, rs, builder);
                } catch (Throwable e) {
                    ResultMessageBuild rb = builder.newMessage(query);
                    rb.receiveMessage(MessageLevel.Error, e.getMessage());
                    rb.collectCost(System.currentTimeMillis() - fetchTimeMs);
                    rb.finishRecord(false, e.getMessage(), e);
                    throw e;
                }
                continue;
            }

            long updateCount = getUpdateCount(ps);
            if (updateCount == -1) {
                break; // equal to: ((ps.getMoreResults() == false) && (ps.getUpdateCount() == -1))
            } else {
                ResultCountBuild rb = builder.newResultCount(query);
                rb.receiveUpdateCount(updateCount);

                rb.collectCost(System.currentTimeMillis() - fetchTimeMs);
                rb.finishRecord(true);
                continue;
            }
        }
    }

    protected void receiveGeneratedKeys(ResultSet keys, ResultCountBuild rb, QueryRequest query) throws SQLException {
        // process data
        int fetchCount = 0;
        long maxFetchCount = query.getResultConf().getFetchRecordCountLimit();

        while (keys.next()) {
            HashMap<String, String> map = new LinkedHashMap<>();
            ResultSetMetaData metaData = keys.getMetaData();
            String columnName = metaData.getColumnName(1);
            map.put(columnName, keys.getString(1));
            rb.receiveGeneratedKey(map);
            fetchCount++;

            // fetch exit
            if (maxFetchCount > 0 && fetchCount >= maxFetchCount) {
                break;
            }
        }
    }

    protected void receiveOutParams(CallableStatement ps, ResultOutputBuild rb, QueryRequest queryRequest) throws SQLException {
        if (CollectionUtils.isNotEmpty(queryRequest.getQueryArgs())) {
            for (QueryArg paramDef : queryRequest.getQueryArgs()) {
                if (paramDef.isOutParam()) {
                    String value = ps.getString(paramDef.getIndex());
                    rb.receiveOutParam(paramDef, value);
                }
            }
        }
    }

    protected void receiveResultRows(QueryRequest query, String resultId, ResultSet rs, ResultBuilder b) throws SQLException, IOException {
        int fetchCount = 0;
        long beginFetchTimeMs = System.currentTimeMillis();
        long lastFetchTimeMs = System.currentTimeMillis();
        long maxFetchCountLimit = query.getResultConf().getFetchRecordCountLimit();
        long maxFetchSizeLimit = query.getResultConf().getFetchResultSetBytesLimit();
        long maxPageSize = query.getResultConf().getFetchPageSize();
        ReceiveMode receiveMode = query.getResultConf().getReceiveMode();

        // process metaData
        ResultSetMetaBuild meta = b.newResultMeta(query, resultId);
        ResultSetMetaData metaData = rs.getMetaData();
        ResultSetRowsBuild rb = meta.receiveMeta(extractMetaData(query, metaData));
        meta.finishRecord(true);

        // process rows
        boolean silentReceive = false;
        long lastPageFetchCount = 0;
        while (rs.next() && !this.rdbCancelSignal.get()) {
            fetchCount++;
            lastPageFetchCount++;
            rb.receiveRow(silentReceive, rs);

            // receiveMode
            switch (receiveMode) {
                case STREAM: {
                    rb.collectMetric(fetchCount);
                    rb.collectCost(System.currentTimeMillis() - beginFetchTimeMs);
                    rb.flushData();
                    rb.finishAndContinue();
                    lastPageFetchCount = 0;
                    break;
                }
                case PAGINATED:
                case PAGE_FULL: {
                    if (!silentReceive && maxPageSize > 0 && lastPageFetchCount >= maxPageSize) {
                        rb.collectCost(System.currentTimeMillis() - beginFetchTimeMs);
                        rb.collectMetric(fetchCount);
                        rb.flushData();
                        rb.finishAndContinue();
                        lastPageFetchCount = 0;
                        silentReceive = receiveMode == ReceiveMode.PAGINATED;
                    }

                    if (silentReceive) {
                        if ((lastFetchTimeMs + 1500) < System.currentTimeMillis()) {
                            lastFetchTimeMs = System.currentTimeMillis();
                            ResultSetRowCountUpdateBuild cb = rb.newRowCountUpdate();
                            cb.collectCost(System.currentTimeMillis() - beginFetchTimeMs);
                            cb.collectMetric(fetchCount);
                            cb.finishRecord(true);
                            rb.flushData();
                        }
                    }
                    break;
                }
                default: {
                    ResultMessageBuild mb = b.newMessage(query);
                    mb.receiveMessage(MessageLevel.Error, "the receive mode " + receiveMode + " not support.");
                    mb.finishRecord(true);
                    throw new SQLException("the receive mode " + receiveMode + " not support.");
                }
            }

            // fetch exit
            if (maxFetchCountLimit > 0 && fetchCount >= maxFetchCountLimit) {
                ResultMessageBuild mb = b.newMessage(query);
                mb.receiveMessage(MessageLevel.Warn, "maximum number of receive rows limit " + maxFetchCountLimit + ", other rows will be skipped.");
                mb.finishRecord(true);
                break;
            }
            if (rb.fetcherOverflow()) {
                String limitStr = FileUtils.readableFileSize(maxFetchSizeLimit);
                ResultMessageBuild mb = b.newMessage(query);
                mb.receiveMessage(MessageLevel.Warn, "maximum number of receive size limit " + limitStr + ", other rows will be skipped.");
                mb.finishRecord(true);
                break;
            }
        }

        if (silentReceive) {
            ResultSetRowCountUpdateBuild cb = rb.newRowCountUpdate();
            cb.collectMetric(fetchCount);
            cb.collectCost(System.currentTimeMillis() - beginFetchTimeMs);
            cb.finishRecord(true);
            rb.flushData();
            rb.finishAndSilent(true);
            ResultMessageBuild mb = b.newMessage(query);
            mb.receiveMessage(MessageLevel.Info, fetchCount + " row retrieved finish.");
            mb.finishRecord(true);
        } else {
            rb.collectMetric(fetchCount);
            rb.collectCost(System.currentTimeMillis() - beginFetchTimeMs);
            rb.flushData();
            rb.finishRecord(true);
        }

        if (this.rdbCancelSignal.get()) {
            log.warn("queryId " + query.getQueryId() + ", resultId " + resultId + ", cancel the query and block receive data.");
        }
    }

    private Map<String, ResultColMeta> extractMetaData(QueryRequest query, ResultSetMetaData metadata) throws SQLException {
        Map<String, ResultColMeta> columnMetaDataMapping = new LinkedHashMap<>();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            ColMetaData cm = this.rdbHook().getColumnMetaData(query, metadata, i);
            ValueFetcher cf = this.colReader.readColumn(cm.getColumn(), cm);
            columnMetaDataMapping.put(cm.getColumn(), new ResultColMeta(cm, cf));
        }
        return columnMetaDataMapping;
    }

    public long getUpdateCount(Statement ps) throws SQLException {
        return ps.getLargeUpdateCount();
    }
}
