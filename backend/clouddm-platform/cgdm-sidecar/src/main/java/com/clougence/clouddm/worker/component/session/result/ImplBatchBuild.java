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

import com.clougence.clouddm.api.sidecar.session.execute.ResultPhaseOfBatch;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhaseType;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ImplBatchBuild extends AbstractResultBuild<ResultPhaseOfBatch> implements BatchBuild {

    public ImplBatchBuild(String resultId, String sessionID, String batchId, ResultListenerContainer listeners){
        super(resultId, sessionID, new QueryRequest(), listeners);
        this.get().setBatchId(batchId);
    }

    @Override
    protected ResultPhaseOfBatch createResult() {
        ResultPhaseOfBatch r = new ResultPhaseOfBatch();
        r.setResultType(ResultType.Phase);
        return r;
    }

    @Override
    protected void initResult(ResultPhaseOfBatch result) {

    }

    @Override
    public void onBegin() {
        this.get().setPhaseType(ResultPhaseType.Before);
    }

    @Override
    public void onEnd() {
        this.get().setPhaseType(ResultPhaseType.After);
    }

    @Override
    public void onCancel() {
        this.get().setPhaseType(ResultPhaseType.Cancel);
    }

}
