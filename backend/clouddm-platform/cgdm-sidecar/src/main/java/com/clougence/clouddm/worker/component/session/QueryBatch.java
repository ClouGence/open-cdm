/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package com.clougence.clouddm.worker.component.session;

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
