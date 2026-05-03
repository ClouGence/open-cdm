package com.clougence.clouddm.worker.component.session.result;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowCountUpdateBuild;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetCount;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImplResultSetRowCountUpdateBuild extends AbstractResultBuild<ResultSetCount> implements ResultSetRowCountUpdateBuild {

    public ImplResultSetRowCountUpdateBuild(String resultId, String sessionID, QueryRequest query, ResultListenerContainer listeners){
        super(resultId, sessionID, query, listeners);
    }

    @Override
    protected ResultSetCount createResult() {
        ResultSetCount r = new ResultSetCount();
        r.setResultType(ResultType.ResultSetRows);
        return r;
    }

    @Override
    protected void initResult(ResultSetCount result) {

    }

    @Override
    public void collectMetric(int fetchCount) {
        this.get().setFetchCount(fetchCount);
    }
}
