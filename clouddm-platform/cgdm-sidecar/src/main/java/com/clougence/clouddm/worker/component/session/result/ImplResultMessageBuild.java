package com.clougence.clouddm.worker.component.session.result;

import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultMessageBuild;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultMessage;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImplResultMessageBuild extends AbstractResultBuild<ResultMessage> implements ResultMessageBuild {

    public ImplResultMessageBuild(String resultId, String sessionID, QueryRequest query, ResultListenerContainer listeners){
        super(resultId, sessionID, query, listeners);
    }

    @Override
    protected ResultMessage createResult() {
        ResultMessage r = new ResultMessage();
        r.setResultType(ResultType.Message);
        return r;
    }

    @Override
    protected void initResult(ResultMessage result) {

    }

    @Override
    public void receiveMessage(MessageLevel level, String message) {
        get().setLevel(level);
        get().setMessage(message);
    }

    @Override
    public void receiveMessage(MessageLevel level, String message, boolean notify) {
        get().setLevel(level);
        get().setMessage(message);
        get().setNotify(notify);
    }
}
