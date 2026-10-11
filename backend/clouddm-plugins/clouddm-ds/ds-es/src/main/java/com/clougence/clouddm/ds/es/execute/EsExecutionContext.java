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

import java.util.concurrent.TimeUnit;

import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

final class EsExecutionContext {
    private final long       deadline;
    private volatile boolean cancelled;

    EsExecutionContext(long timeoutMs){
        deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
    }

    void cancel() {
        cancelled = true;
    }

    void checkActive() {
        if (cancelled || Thread.currentThread().isInterrupted()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_REQUEST_CANCELLED);
        }
        if (System.nanoTime() >= deadline) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_REQUEST_TIMEOUT);
        }
    }

    long remainingMillis() {
        checkActive();
        return Math.max(1, TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime()));
    }
}
