package com.clougence.clouddm.worker.component.session.result;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.worker.component.session.SessionSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultResultBuilder implements ResultBuilder {

    private final AtomicBoolean           finished  = new AtomicBoolean(false);
    private final AtomicInteger           resultIdx = new AtomicInteger();
    private final String                  sessionId;
    private final ResultListenerContainer listeners;
    private final SessionSupport          ss;

    public DefaultResultBuilder(String sessionId, SessionSupport ss, ResultListenerContainer listeners){
        this.sessionId = sessionId;
        this.listeners = listeners;
        this.ss = ss;
    }

    public boolean isPending() { return !this.finished.get(); }

    public ResultListenerContainer getListenerContainer() { return listeners; }

    private void checkStatus() {
        if (this.finished.get()) {
            throw new IllegalStateException("the query has finish.");
        }
    }

    @Override
    public void finished() {
        this.finished.set(true);
    }

    @Override
    public String newResultId(QueryRequest query) {
        return "r_" + this.sessionId + "_" + query.getQueryId() + "_" + this.resultIdx.incrementAndGet();
    }

    @Override
    public PhaseBuild newPhase(QueryRequest query) {
        this.checkStatus();
        ImplPhaseBuild build = new ImplPhaseBuild(null, this.sessionId, query, this.listeners);
        build.initBuild();
        return build;
    }

    @Override
    public PhaseBuild newPhase(QueryRequest query, String resultId) {
        this.checkStatus();
        ImplPhaseBuild build = new ImplPhaseBuild(resultId, this.sessionId, query, this.listeners);
        build.initBuild();
        return build;
    }

    @Override
    public ResultSetMetaBuild newResultMeta(QueryRequest query, String resultId) {
        this.checkStatus();

        ImplResultSetMetaBuild build = new ImplResultSetMetaBuild(resultId, this.sessionId, query, this.listeners, this.ss);
        build.initBuild();
        return build;
    }

    @Override
    public ResultCountBuild newResultCount(QueryRequest query) {
        this.checkStatus();
        ImplResultCountBuild build = new ImplResultCountBuild(null, this.sessionId, query, this.listeners);
        build.initBuild();
        return build;
    }

    @Override
    public ResultOutputBuild newOutput(QueryRequest query) {
        this.checkStatus();
        ImplResultOutputBuild build = new ImplResultOutputBuild(null, this.sessionId, query, this.listeners);
        build.initBuild();
        return build;
    }

    @Override
    public ResultMessageBuild newMessage(QueryRequest query) {
        this.checkStatus();
        ImplResultMessageBuild build = new ImplResultMessageBuild(null, this.sessionId, query, this.listeners);
        build.initBuild();
        return build;
    }

    public BatchBuild newBatch(String batchId) {
        this.checkStatus();
        ImplBatchBuild build = new ImplBatchBuild(null, this.sessionId, batchId, this.listeners);
        build.initBuild();
        return build;
    }
}
