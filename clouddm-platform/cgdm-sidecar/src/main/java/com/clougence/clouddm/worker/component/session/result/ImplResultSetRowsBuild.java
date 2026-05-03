package com.clougence.clouddm.worker.component.session.result;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowCountUpdateBuild;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowsBuild;
import com.clougence.clouddm.sdk.execute.session.ResultColMeta;
import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSet;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;
import com.clougence.clouddm.worker.component.session.SessionSupport;
import com.clougence.clouddm.worker.component.session.storage.ResultStorage;
import com.clougence.clouddm.worker.component.session.storage.RowStorage;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.result.ResultSetOverflowException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImplResultSetRowsBuild extends AbstractResultBuild<ResultSet> implements ResultSetRowsBuild {

    private final AtomicLong            rowId;
    private final QueryRequest          query;
    private final ValueProcessService   processSpi;
    private ResultStorage               localCache;
    // current rs
    private String[]                    columnList;
    private ValueFetcher[]              columnFetcher;
    private final ValueFetcherContext[] columnFetcherCtx;
    private Map<String, ColMetaData>    cacheRowMeta;
    private long                        fetcherDataSize;
    private long                        expansionSize;
    private boolean                     fetcherOverflow;

    public ImplResultSetRowsBuild(String resultId, String sessionID, QueryRequest query, ResultListenerContainer listeners, //
                                  SessionSupport ss, ResultStorage localCache, List<ValueFetcherContext> metaCtx){
        super(resultId, sessionID, query, listeners);
        this.rowId = new AtomicLong();
        this.query = query;
        this.localCache = localCache;
        this.processSpi = ss.getResultProcessSpi();

        int colCount = metaCtx.size();
        this.columnList = new String[colCount];
        this.columnFetcher = new ValueFetcher[colCount];
        this.columnFetcherCtx = new ValueFetcherContext[colCount];
        this.cacheRowMeta = new LinkedHashMap<>();
        for (int i = 0; i < colCount; i++) {
            ValueFetcherContext ctx = metaCtx.get(i);
            ResultColMeta meta = ctx.getMeta();
            String colName = meta.getMeta().getColumn();

            this.columnList[i] = colName;
            this.columnFetcher[i] = meta.getFetcher();
            this.columnFetcherCtx[i] = ctx;
            this.cacheRowMeta.put(colName, meta.getMeta());
        }
    }

    private void resetCurrent() {
        this.localCache = null;
        this.cacheRowMeta = null;
        this.columnList = null;
        this.columnFetcher = null;
        this.fetcherDataSize = 0;
        this.expansionSize = 0;
        this.fetcherOverflow = false;
    }

    @Override
    protected ResultSet createResult() {
        ResultSet r = new ResultSet();
        r.setResultType(ResultType.ResultSet);
        r.setRowSet(new LinkedList<>());
        return r;
    }

    @Override
    protected void initResult(ResultSet result) {

    }

    @Override
    public void receiveRow(boolean silent, java.sql.ResultSet rs) throws SQLException, IOException {
        List<ResultSetValue> data = silent ? Collections.emptyList() : new ArrayList<>();
        try (RowStorage row = this.localCache.nextRsRow()) {
            for (int i = 0; i < this.columnList.length; i++) {
                String column = this.columnList[i];
                ValueFetcher fetcher = this.columnFetcher[i];
                ValueFetcherContext fetcherCtx = this.columnFetcherCtx[i];

                ResultSetValue value;
                try {
                    byte tag = 0;
                    value = row.addValue(tag, rs, column, fetcher, fetcherCtx);

                    if (fetcherCtx.isErrStatus()) {
                        if (fetcherCtx.getErrObject() instanceof ResultSetOverflowException) {
                            throw (ResultSetOverflowException) fetcherCtx.getErrObject();
                        } else {
                            throw new SQLException("Value fetcher error: " + fetcherCtx.getErrObject());
                        }
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    fetcherCtx.free();
                }

                this.fetcherDataSize += value.getTotalSize();
                this.expansionSize += value.getTotalSize();

                if (!silent) {
                    data.add(value);
                }
            }

            // save
            row.commit();
            long incremented = this.rowId.getAndIncrement();

            if (!silent) {
                ResultSetRow rowDTO = new ResultSetRow();
                rowDTO.setRowId(String.valueOf(incremented));
                rowDTO.setData(data);
                this.get().getRowSet().add(rowDTO);
            }
        } catch (ResultSetOverflowException e) {
            this.expansionSize += e.getOverflowSize();
            this.fetcherOverflow = true;
        }
    }

    @Override
    public void receiveRow(boolean silent, Map<String, Object> rs) throws SQLException, IOException {
        List<ResultSetValue> data = silent ? Collections.emptyList() : new ArrayList<>();
        try (RowStorage row = this.localCache.nextRsRow()) {
            for (int i = 0; i < this.columnList.length; i++) {
                String column = this.columnList[i];
                ValueFetcher fetcher = this.columnFetcher[i];
                ValueFetcherContext fetcherCtx = this.columnFetcherCtx[i];

                ResultSetValue value;
                try {
                    byte tag = 0;
                    value = row.addValue(tag, rs, column, fetcher.getType(), fetcherCtx);

                    if (fetcherCtx.isErrStatus()) {
                        if (fetcherCtx.getErrObject() instanceof ResultSetOverflowException) {
                            throw (ResultSetOverflowException) fetcherCtx.getErrObject();
                        } else {
                            throw new SQLException("Value fetcher error: " + fetcherCtx.getErrObject());
                        }
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    fetcherCtx.free();
                }

                this.fetcherDataSize += value.getTotalSize();
                this.expansionSize += value.getTotalSize();

                if (!silent) {
                    data.add(value);
                }
            }

            // save
            row.commit();
            long incremented = this.rowId.incrementAndGet();

            if (!silent) {
                ResultSetRow rowDTO = new ResultSetRow();
                rowDTO.setRowId(String.valueOf(incremented));
                rowDTO.setData(data);
                this.get().getRowSet().add(rowDTO);
            }
        } catch (ResultSetOverflowException e) {
            this.expansionSize += e.getOverflowSize();
            this.fetcherOverflow = true;
        }
    }

    @Override
    public long dataSize() {
        return this.fetcherDataSize;
    }

    @Override
    public long expansionSize() {
        return this.expansionSize;
    }

    @Override
    public boolean fetcherOverflow() {
        return this.fetcherOverflow;
    }

    @Override
    public void finishAndContinue() {
        this.doMask();

        this.get().setSuccess(true);
        try {
            this.listeners.disabledAll();
            this.listeners.enableListener(ResultListenerKey.RESULT_LISTENER);
            this.listeners.doListeners(this.query, this.result);
        } finally {
            this.listeners.enableAll();
        }
        this.result = this.get().cloneEmpty();
    }

    @Override
    public void finishAndSilent(boolean success) {
        try {
            this.listeners.disabledListener(ResultListenerKey.RESULT_LISTENER);
            this.finishRecord(success);
        } finally {
            this.listeners.enableListener(ResultListenerKey.RESULT_LISTENER);
        }
    }

    @Override
    public ResultSetRowCountUpdateBuild newRowCountUpdate() {
        String resultId = this.get().getResultId();
        String sessionId = this.get().getSessionId();
        ImplResultSetRowCountUpdateBuild cb = new ImplResultSetRowCountUpdateBuild(resultId, sessionId, this.query, this.listeners);
        cb.initBuild();
        return cb;
    }

    @Override
    public void flushData() {
        if (this.localCache != null) {
            this.localCache.flush();
        }
    }

    @Override
    public void collectMetric(int fetchCount) {
        this.get().setFetchCount(fetchCount);
    }

    @Override
    public void finishRecord(boolean success) {
        if (this.localCache != null) {
            if (success) {
                this.localCache.finish();
            } else {
                this.localCache.free();
            }
        }

        this.doMask();
        this.resetCurrent();

        super.finishRecord(success);
    }

    @Override
    public void finishRecord(boolean success, String message, Throwable e) {
        if (this.localCache != null) {
            if (success) {
                this.localCache.finish();
            } else {
                this.localCache.free();
            }
        }

        if (success) {
            this.doMask();
        }
        this.resetCurrent();

        super.finishRecord(success, message, e);
    }

    private void doMask() {
        if (!query.isUsingValueProcess() || CollectionUtils.isEmpty(this.get().getRowSet())) {
            return;
        }
        if (this.processSpi == null) {
            throw new UnsupportedOperationException("plugin 'plus-sec-rules' is not exist.");
        }

        Map<String, Object> flash = new ConcurrentHashMap<>();
        this.processSpi.begin(query, this.cacheRowMeta, flash);
        this.get().getRowSet().parallelStream().forEach(row -> {
            List<String> tmpRowData = row.getData().stream().map(ResultSetValue::getValue).collect(Collectors.toList());
            List<String> maskRowData = this.processSpi.processRow(query, this.cacheRowMeta, tmpRowData, flash);
            for (int i = 0; i < row.getData().size(); i++) {
                ResultSetValue column = row.getData().get(i);
                String beforeData = column.getValue();
                String afterData = maskRowData.get(i);

                column.setValue(afterData);
                if (!StringUtils.equals(beforeData, afterData)) {
                    column.setMask(true);
                }
            }
        });
        this.processSpi.finish(query, flash);
    }
}
