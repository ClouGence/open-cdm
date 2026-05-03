package com.clougence.clouddm.worker.component.session.result;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.PhaseBuild;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhase;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhaseType;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImplPhaseBuild extends AbstractResultBuild<ResultPhase> implements PhaseBuild {

    public ImplPhaseBuild(String resultId, String sessionID, QueryRequest query, ResultListenerContainer listeners){
        super(resultId, sessionID, query, listeners);
    }

    @Override
    protected ResultPhase createResult() {
        ResultPhase r = new ResultPhase();
        r.setResultType(ResultType.Phase);
        return r;
    }

    @Override
    protected void initResult(ResultPhase result) {

    }

    @Override
    public void onPhase(ResultPhaseType type) {
        get().setPhaseType(type);
    }
}
