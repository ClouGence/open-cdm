package com.clougence.clouddm.worker.component.session.result;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;

@FunctionalInterface
public interface ResultListener {

    void onResult(QueryRequest query, Result result);

}
