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

import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

/** One deadline and cancellation scope, reusable across sequential model requests. */
public class ChatStreamControl {
    private final long                      deadlineNanos;
    private final AtomicBoolean             cancelled = new AtomicBoolean();
    private final AtomicReference<Runnable> transport = new AtomicReference<>();

    public ChatStreamControl(Duration duration){
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("Stream duration must be positive");
        }
        deadlineNanos = System.nanoTime() + duration.toNanos();
    }

    public boolean isExpired() { return System.nanoTime() - deadlineNanos >= 0; }

    public void checkActive() {
        if (isExpired()) {
            cancel();
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_PROVIDER_TIMEOUT);
        }
        if (cancelled.get()) {
            throw new CancellationException("Chat stream cancelled");
        }
    }

    public long remainingMillis() {
        checkActive();
        return Math.max(1, Duration.ofNanos(deadlineNanos - System.nanoTime()).toMillis());
    }

    public void attach(Runnable cancelTransport) {
        if (!transport.compareAndSet(null, cancelTransport)) {
            cancelTransport.run();
            throw new IllegalStateException("A stream transport is already attached");
        }
        // A stop before binding must still cancel the newly created transport.
        if (cancelled.get() && transport.compareAndSet(cancelTransport, null)) {
            cancelTransport.run();
        }
        checkActive();
    }

    public void detach(Runnable cancelTransport) {
        transport.compareAndSet(cancelTransport, null);
    }

    public void cancel() {
        cancelled.set(true);
        Runnable active = transport.getAndSet(null);
        if (active != null) {
            active.run();
        }
    }
}
