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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentTooLongException;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.CapacityChannel;
import org.apache.hc.core5.http.protocol.HttpContext;

final class OpenSearchResponseConsumer implements AsyncResponseConsumer<OpenSearchResponse> {
    private final int                          limit;
    private final OpenSearchExchange           exchange;
    private ByteArrayOutputStream              body;
    private FutureCallback<OpenSearchResponse> callback;
    private int                                status;

    OpenSearchResponseConsumer(int limit, OpenSearchExchange exchange){
        this.limit = limit;
        this.exchange = exchange;
        body = new ByteArrayOutputStream(Math.min(8192, limit));
    }

    @Override
    public synchronized void consumeResponse(HttpResponse response, EntityDetails entity, HttpContext context, FutureCallback<OpenSearchResponse> callback) throws IOException {
        this.callback = callback;
        status = response.getCode();
        if (entity == null) {
            callback.completed(new OpenSearchResponse(status, new byte[0]));
            return;
        }
        if (entity.getContentLength() > limit) {
            exceeded();
        }
    }

    @Override
    public void informationResponse(HttpResponse response, HttpContext context) {
    }

    @Override
    public void updateCapacity(CapacityChannel channel) throws IOException {
        channel.update(8192);
    }

    @Override
    public synchronized void consume(ByteBuffer data) throws IOException {
        if (body == null) {
            return;
        }
        if (data.remaining() > limit - body.size()) {
            exceeded();
        }
        byte[] chunk = new byte[data.remaining()];
        data.get(chunk);
        body.write(chunk);
    }

    private void exceeded() throws ContentTooLongException {
        ContentTooLongException cause = new ContentTooLongException("OpenSearch response exceeds configured limit");
        // Publish failure explicitly: an exception from a TLS consumer alone may leave its outer future pending.
        exchange.failed(cause);
        throw cause;
    }

    @Override
    public synchronized void streamEnd(List<? extends Header> trailers) {
        if (body != null && callback != null) {
            callback.completed(new OpenSearchResponse(status, body.toByteArray()));
        }
    }

    @Override
    public synchronized void failed(Exception cause) {
        exchange.failed(cause);
        if (callback != null) {
            callback.failed(cause);
        }
        body = null;
    }

    @Override
    public synchronized void releaseResources() {
        body = null;
        callback = null;
    }
}
