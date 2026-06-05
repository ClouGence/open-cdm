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
