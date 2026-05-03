package com.clougence.clouddm.worker.component.session;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryBatch {

    private String              batchId;
    private Queue<QueryRequest> requests;
    private boolean             canceled;
    private long                beginTime;

    public QueryBatch(String batchId, List<QueryRequest> requests){
        this.batchId = batchId;
        this.requests = new LinkedList<>(requests);
        this.canceled = false;
    }

    public QueryRequest pollRequest() {
        return this.requests.poll();
    }
}
