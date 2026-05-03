package com.clougence.clouddm.sdk.execute.session;

import com.clougence.utils.future.CgFutureObj;

public class QueryFuture extends CgFutureObj<Void> {

    private final String sessionId;
    private final long   beginTime;

    public QueryFuture(String sessionId){
        this.sessionId = sessionId;
        this.beginTime = System.currentTimeMillis();
    }

    public String getSessionId() { return this.sessionId; }

    public long getBeginTime() { return this.beginTime; }
}
