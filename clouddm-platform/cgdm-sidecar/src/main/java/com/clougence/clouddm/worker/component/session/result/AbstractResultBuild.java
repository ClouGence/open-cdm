package com.clougence.clouddm.worker.component.session.result;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultBuild;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class AbstractResultBuild<T extends Result> implements ResultBuild, Supplier<T> {

    private final AtomicBoolean             offered;
    protected final ResultListenerContainer listeners;
    protected T                             result;
    private final QueryRequest              query;

    public AbstractResultBuild(String resultId, String sessionID, QueryRequest query, ResultListenerContainer listeners){
        this.offered = new AtomicBoolean(false);
        this.listeners = listeners;
        this.query = query;

        this.result = this.createResult();
        this.result.setResultId(resultId);
        this.result.setSessionId(sessionID);
        this.result.setBatchId(query.getBatchId());
        this.result.setQueryId(query.getQueryId());
        this.result.setQuerySql(query.getQueryBody());
        this.result.setResource(query.getResource());
        this.result.setVariables(query.getVariables());
        this.result.setHasRewrite(query.isHasRewrite());
        this.result.setOriginalScript(query.getOriginalBody());
        this.result.setRewriteTag(query.getRewriteTag());
    }

    protected abstract T createResult();

    public void initBuild() {
        this.initResult(this.result);
    }

    protected abstract void initResult(T result);

    @Override
    public final T get() {
        return this.result;
    }

    @Override
    public final void collectCost(long costTimeMs) {
        this.result.setCostTimeMs(costTimeMs);
    }

    @Override
    public void finishRecord(boolean success) {
        if (this.offered.compareAndSet(false, true)) {
            get().setSuccess(success);
            this.listeners.doListeners(this.query, this.result);
        }
    }

    @Override
    public void finishRecord(boolean success, String message, Throwable e) {
        if (this.offered.compareAndSet(false, true)) {
            get().setSuccess(success);
            get().setMessage(message);

            if (e != null) {
                log.error(ExceptionUtils.getRootCauseMessage(e), e);
            }

            this.listeners.doListeners(this.query, this.result);
        }
    }
}
