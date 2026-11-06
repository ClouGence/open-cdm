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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.apache.hc.core5.concurrent.Cancellable;
import org.apache.hc.core5.concurrent.CancellableDependency;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ConnectionRequestTimeoutException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class OpenSearchExchange implements CancellableDependency, FutureCallback<OpenSearchResponse> {
    static final String                                 CONTEXT_ATTRIBUTE = OpenSearchExchange.class.getName();
    private final CompletableFuture<OpenSearchResponse> result            = new CompletableFuture<>();
    private final List<Cancellable>                     dependencies      = new ArrayList<>();
    private final ScheduledExecutorService              deadlines;
    private final int                                   requestTimeoutMs;
    private ScheduledFuture<?>                          deadline;
    private boolean                                     acquired;
    private boolean                                     finished;
    private Exception                                   failure;

    OpenSearchExchange(ScheduledExecutorService deadlines, int requestTimeoutMs){
        this.deadlines = deadlines;
        this.requestTimeoutMs = requestTimeoutMs;
    }

    synchronized void startQueue(int timeoutMs) {
        deadline = deadlines.schedule(() -> expire(false), timeoutMs, TimeUnit.MILLISECONDS);
    }

    synchronized boolean acquired() {
        if (finished) {
            return false;
        }
        acquired = true;
        deadline.cancel(false);
        deadline = deadlines.schedule(() -> expire(true), requestTimeoutMs, TimeUnit.MILLISECONDS);
        return true;
    }

    private void expire(boolean execution) {
        Exception cause;
        synchronized (this) {
            if (finished || acquired != execution) {
                return;
            }
            if (execution) {
                cause = new TimeoutException("OpenSearch request timed out");
            } else {
                cause = new ConnectionRequestTimeoutException("OpenSearch pool lease timed out");
            }
            // Claim the terminal state before an acquisition callback can switch phases.
            finished = true;
            failure = cause;
        }
        finish(null, cause);
    }

    @Override
    public void setDependency(Cancellable dependency) {
        boolean abort;
        synchronized (this) {
            abort = finished && failure != null;
            if (!finished) {
                // HC5 can bind the completed lease after the active HTTP dependency on a reused connection.
                dependencies.add(dependency);
            }
        }
        if (abort) {
            dependency.cancel();
        }
    }

    @Override
    public boolean cancel() {
        return complete(null, new CancellationException("OpenSearch request cancelled"));
    }

    @Override
    public synchronized boolean isCancelled() { return finished && failure != null; }

    @Override
    public void completed(OpenSearchResponse response) {
        complete(response, null);
    }

    @Override
    public void failed(Exception cause) {
        complete(null, cause);
    }

    @Override
    public void cancelled() {
        cancel();
    }

    private boolean complete(OpenSearchResponse response, Exception cause) {
        synchronized (this) {
            if (finished) {
                return false;
            }
            finished = true;
            failure = cause;
        }
        finish(response, cause);
        return true;
    }

    private void finish(OpenSearchResponse response, Exception cause) {
        List<Cancellable> handles;
        synchronized (this) {
            if (deadline != null) {
                deadline.cancel(false);
            }
            handles = List.copyOf(dependencies);
            dependencies.clear();
        }
        try {
            if (cause != null) {
                for (Cancellable handle : handles) {
                    try {
                        handle.cancel();
                    } catch (RuntimeException e) {
                        log.error("Cancel OpenSearch HTTP dependency failed", e);
                    }
                }
            }
        } finally {
            if (cause == null) {
                result.complete(response);
            } else {
                result.completeExceptionally(cause);
            }
        }
    }

    OpenSearchResponse await() throws InterruptedException, ExecutionException {
        return result.get();
    }
}
