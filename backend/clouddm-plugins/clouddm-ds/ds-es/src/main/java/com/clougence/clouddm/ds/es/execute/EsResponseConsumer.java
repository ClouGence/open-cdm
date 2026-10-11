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
package com.clougence.clouddm.ds.es.execute;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.http.ContentTooLongException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;

/** Counts actual bytes as well as Content-Length, including chunked responses. */
final class EsResponseConsumer extends AbstractAsyncResponseConsumer<HttpResponse> {
    private final int             limit;
    private final ByteBuffer      buffer  = ByteBuffer.allocate(8192);
    private ByteArrayOutputStream content = new ByteArrayOutputStream();
    private HttpResponse          response;
    private ContentType           contentType;

    EsResponseConsumer(int limit){
        this.limit = limit;
    }

    @Override
    protected void onResponseReceived(HttpResponse response) {
        this.response = response;
    }

    @Override
    protected void onEntityEnclosed(HttpEntity entity, ContentType type) throws IOException {
        contentType = type;
        if (entity.getContentLength() > limit) {
            throw new ContentTooLongException("ES response exceeds " + limit + " bytes");
        }
    }

    @Override
    protected void onContentReceived(ContentDecoder decoder, IOControl control) throws IOException {
        buffer.clear();
        int count;
        while ((count = decoder.read(buffer)) > 0) {
            if ((long) content.size() + count > limit) {
                throw new ContentTooLongException("ES response exceeds " + limit + " bytes");
            }
            content.write(buffer.array(), 0, count);
            buffer.clear();
        }
    }

    @Override
    protected HttpResponse buildResult(HttpContext context) {
        if (response.getEntity() != null) {
            response.setEntity(new ByteArrayEntity(content.toByteArray(), contentType));
        }
        return response;
    }

    @Override
    protected void releaseResources() {
        content = null;
        response = null;
    }
}
