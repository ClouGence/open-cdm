package com.clougence.clouddm.worker.component.session.result;

import com.clougence.clouddm.api.sidecar.session.execute.ResultPhaseOfBatch;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhaseType;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImplBatchBuild extends AbstractResultBuild<ResultPhaseOfBatch> implements BatchBuild {

    public ImplBatchBuild(String resultId, String sessionID, String batchId, ResultListenerContainer listeners){
        super(resultId, sessionID, new QueryRequest(), listeners);
        this.get().setBatchId(batchId);
    }

    @Override
    protected ResultPhaseOfBatch createResult() {
        ResultPhaseOfBatch r = new ResultPhaseOfBatch();
        r.setResultType(ResultType.Phase);
        return r;
    }

    @Override
    protected void initResult(ResultPhaseOfBatch result) {

    }

    @Override
    public void onBegin() {
        this.get().setPhaseType(ResultPhaseType.Before);
    }

    @Override
    public void onEnd() {
        this.get().setPhaseType(ResultPhaseType.After);
    }

    @Override
    public void onCancel() {
        this.get().setPhaseType(ResultPhaseType.Cancel);
    }

}
