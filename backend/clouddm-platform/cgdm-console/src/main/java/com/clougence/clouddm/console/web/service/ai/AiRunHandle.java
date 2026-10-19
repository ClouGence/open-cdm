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
package com.clougence.clouddm.console.web.service.ai;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import com.clougence.clouddm.console.web.model.ai.AiError;
import com.clougence.clouddm.ds.ai.ChatStreamControl;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import lombok.Getter;

public class AiRunHandle {
    @Getter
    private final ChatStreamControl        streamControl;
    private final AtomicReference<AiError> failure = new AtomicReference<>();

    public AiRunHandle(Duration duration){
        if (duration.isNegative() || duration.isZero()) {
            throw AiError.TURN_EXPIRED.exception();
        }
        streamControl = new ChatStreamControl(duration);
    }

    public void checkActive() {
        expireIfDue();
        if (failure.get() != null) {
            throw failure.get().exception();
        }
        try {
            streamControl.checkActive();
        } catch (ThirdPartyApiException e) {
            fail(AiError.TURN_EXPIRED);
            throw failure.get().exception();
        }
    }

    public boolean expireIfDue() {
        if (!streamControl.isExpired()) {
            return false;
        }
        fail(AiError.TURN_EXPIRED);
        return true;
    }

    public long remainingMillis() {
        checkActive();
        try {
            return streamControl.remainingMillis();
        } catch (ThirdPartyApiException e) {
            fail(AiError.TURN_EXPIRED);
            throw failure.get().exception();
        }
    }

    public void cancel() {
        streamControl.cancel();
    }

    public void fail(AiError error) {
        failure.compareAndSet(null, error);
        cancel();
    }
}
