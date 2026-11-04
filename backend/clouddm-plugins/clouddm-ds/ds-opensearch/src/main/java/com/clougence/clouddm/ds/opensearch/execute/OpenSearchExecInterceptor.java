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
 */
package com.clougence.clouddm.ds.opensearch.execute;

import java.io.IOException;
import java.util.concurrent.CancellationException;

import org.apache.hc.client5.http.async.AsyncExecCallback;
import org.apache.hc.client5.http.async.AsyncExecChain;
import org.apache.hc.client5.http.async.AsyncExecChainHandler;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.nio.AsyncEntityProducer;

public final class OpenSearchExecInterceptor implements AsyncExecChainHandler {
    @Override
    public void execute(HttpRequest request, AsyncEntityProducer producer, AsyncExecChain.Scope scope, AsyncExecChain chain, AsyncExecCallback callback) throws HttpException,
                                                                                                                                                         IOException {
        OpenSearchExchange exchange = (OpenSearchExchange) scope.clientContext.getAttribute(OpenSearchExchange.CONTEXT_ATTRIBUTE);
        if (exchange.isCancelled()) {
            callback.failed(new CancellationException("OpenSearch request cancelled"));
            return;
        }
        AsyncExecChain.Scope tracked = new AsyncExecChain.Scope(scope.exchangeId,
            scope.route,
            scope.originalRequest,
            exchange,
            scope.clientContext,
            scope.execRuntime,
            scope.scheduler,
            scope.execCount);
        chain.proceed(request, producer, tracked, callback);
    }
}
