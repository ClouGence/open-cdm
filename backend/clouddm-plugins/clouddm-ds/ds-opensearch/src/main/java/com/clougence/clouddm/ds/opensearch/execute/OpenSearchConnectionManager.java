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
import java.util.concurrent.Future;

import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
import org.apache.hc.client5.http.nio.AsyncConnectionEndpoint;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.ConnectionInitiator;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

public final class OpenSearchConnectionManager implements AsyncClientConnectionManager {
    private final AsyncClientConnectionManager delegate;

    public OpenSearchConnectionManager(AsyncClientConnectionManager delegate){
        this.delegate = delegate;
    }

    @Override
    public Future<AsyncConnectionEndpoint> lease(String id, HttpRoute route, Object state, Timeout timeout, FutureCallback<AsyncConnectionEndpoint> callback) {
        OpenSearchExchange exchange = (OpenSearchExchange) state;
        // The context token tracks this exchange. Credentials are fixed per client, so pooled connections stay stateless.
        return delegate.lease(id, route, null, timeout, new OpenSearchLeaseCallback(delegate, exchange, callback));
    }

    @Override
    public void release(AsyncConnectionEndpoint endpoint, Object state, TimeValue duration) {
        delegate.release(endpoint, null, duration);
    }

    @Override
    public Future<AsyncConnectionEndpoint> connect(AsyncConnectionEndpoint endpoint, ConnectionInitiator initiator, Timeout timeout, Object attachment, HttpContext context,
                                                   FutureCallback<AsyncConnectionEndpoint> callback) {
        return delegate.connect(endpoint, initiator, timeout, attachment, context, callback);
    }

    @Override
    public void upgrade(AsyncConnectionEndpoint endpoint, Object attachment, HttpContext context) {
        delegate.upgrade(endpoint, attachment, context);
    }

    @Override
    public void upgrade(AsyncConnectionEndpoint endpoint, Object attachment, HttpContext context, FutureCallback<AsyncConnectionEndpoint> callback) {
        delegate.upgrade(endpoint, attachment, context, callback);
    }

    @Override
    public void close(CloseMode mode) {
        delegate.close(mode);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
