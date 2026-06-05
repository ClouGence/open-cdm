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
