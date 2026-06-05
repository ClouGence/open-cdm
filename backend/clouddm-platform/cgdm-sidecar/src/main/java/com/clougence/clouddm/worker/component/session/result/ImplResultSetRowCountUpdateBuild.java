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
 */package com.clougence.clouddm.worker.component.session.result;

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
