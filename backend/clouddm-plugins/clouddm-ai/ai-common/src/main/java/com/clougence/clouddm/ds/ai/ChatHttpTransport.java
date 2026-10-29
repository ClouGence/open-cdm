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
package com.clougence.clouddm.ds.ai;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.CharacterCodingException;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.ExceptionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import okhttp3.*;

public class ChatHttpTransport implements AutoCloseable {
    private final Set<Call>     activeCalls = ConcurrentHashMap.newKeySet();
    private final AtomicBoolean closed      = new AtomicBoolean();

    public ChatResponse stream(Request request, ChatStreamControl control, ChatStreamDecoder decoder, ChatStreamListener listener) throws IOException {
        control.checkActive();
        if (closed.get()) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_PROVIDER_UNAVAILABLE);
        }
        // Release transport resources when each stream completes.
        ConnectionPool pool = new ConnectionPool(0, 1, TimeUnit.SECONDS);
        OkHttpClient client = new OkHttpClient.Builder().connectionPool(pool)
            .retryOnConnectionFailure(false)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(600, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .callTimeout(control.remainingMillis(), TimeUnit.MILLISECONDS)
            .build();
        Call call = client.newCall(request);
        Runnable cancelTransport = call::cancel;
        activeCalls.add(call);
        try {
            if (closed.get()) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_PROVIDER_UNAVAILABLE);
            }
            control.attach(cancelTransport);
            try (Response response = call.execute()) {
                if (!response.isSuccessful()) {
                    String body = "";
                    if (response.body() != null) {
                        body = response.body().string();
                    }
                    throw ChatHttpErrorDecoder.decode(response.code(), body);
                }
                if (response.body() == null || response.body().contentType() == null || !"text".equals(response.body().contentType().type())
                    || !"event-stream".equals(response.body().contentType().subtype())) {
                    throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
                }
                ChatResponse reply = decoder.parse(response.body().byteStream(), control, listener);
                control.checkActive();
                if (closed.get()) {
                    throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_PROVIDER_UNAVAILABLE);
                }
                return reply;
            }
        } catch (IOException | RuntimeException e) {
            Exception failure = e;
            try {
                control.checkActive();
                Throwable root = ExceptionUtils.getRootCause(e);
                if (e instanceof JsonProcessingException || e instanceof CharacterCodingException) {
                    failure = ThirdPartyApiException.as().with(e, ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
                } else if (root instanceof InterruptedIOException || root instanceof TimeoutException) {
                    failure = ThirdPartyApiException.as().with(e, ChatProtocolConstants.ERROR_PROVIDER_TIMEOUT);
                } else if (root instanceof UnknownHostException || root instanceof UnresolvedAddressException) {
                    failure = ThirdPartyApiException.as().with(e, ChatProtocolConstants.ERROR_PROVIDER_UNRESOLVED_SERVER);
                }
            } catch (RuntimeException terminal) {
                failure = terminal;
            }
            if (failure instanceof ThirdPartyApiException provider && ChatProtocolConstants.ERROR_MODEL_CONTENT_FILTERED.equals(provider.getMessageKey())) {
                return decoder.contentFiltered();
            }
            boolean cancelled = failure instanceof CancellationException;
            Throwable interruptionError = failure;
            if (cancelled) {
                interruptionError = null;
            }
            listener.getInterrupted().accept(new ChatStreamInterruption(decoder.snapshot(), interruptionError, cancelled));
            if (failure instanceof IOException io) {
                throw io;
            }
            throw (RuntimeException) failure;
        } finally {
            activeCalls.remove(call);
            control.detach(cancelTransport);
            call.cancel();
            pool.evictAll();
            client.dispatcher().executorService().shutdown();
        }
    }

    @Override
    public void close() {
        closed.set(true);
        activeCalls.forEach(Call::cancel);
    }
}
