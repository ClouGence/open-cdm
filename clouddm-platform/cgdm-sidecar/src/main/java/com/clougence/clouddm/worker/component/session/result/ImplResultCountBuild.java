package com.clougence.clouddm.worker.component.session.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultCountBuild;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultCount;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImplResultCountBuild extends AbstractResultBuild<ResultCount> implements ResultCountBuild {

    public ImplResultCountBuild(String resultId, String sessionID, QueryRequest query, ResultListenerContainer listeners){
        super(resultId, sessionID, query, listeners);
    }

    @Override
    protected ResultCount createResult() {
        ResultCount r = new ResultCount();
        r.setResultType(ResultType.ResultCount);
        return r;
    }

    @Override
    protected void initResult(ResultCount result) {

    }

    @Override
    public void receiveUpdateCount(long updateCount) {
        this.get().setUpdateCount(updateCount);
    }

    @Override
    public void receiveGeneratedKey(Map<String, String> generatedData) {
        List<Map<String, String>> generatedKeys = this.get().getGeneratedKeys();
        if (generatedKeys == null) {
            this.get().setGeneratedKeys(new ArrayList<>());
        }

        this.get().getGeneratedKeys().add(generatedData);
    }
}
