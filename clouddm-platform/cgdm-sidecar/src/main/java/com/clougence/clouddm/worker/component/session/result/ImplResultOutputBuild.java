package com.clougence.clouddm.worker.component.session.result;

import java.util.HashMap;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultOutputBuild;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultOut;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImplResultOutputBuild extends AbstractResultBuild<ResultOut> implements ResultOutputBuild {

    public ImplResultOutputBuild(String resultId, String sessionID, QueryRequest query, ResultListenerContainer listeners){
        super(resultId, sessionID, query, listeners);
    }

    @Override
    protected ResultOut createResult() {
        ResultOut r = new ResultOut();
        r.setResultType(ResultType.ResultOut);
        return r;
    }

    @Override
    protected void initResult(ResultOut result) {

    }

    @Override
    public void receiveOutParam(QueryArg paramDef, String value) {
        Map<String, String> outs = this.get().getOutParams();
        if (outs == null) {
            this.get().setOutParams(new HashMap<>());
        }

        this.get().getOutParams().put(String.valueOf(paramDef.getIndex()), value);
    }
}
