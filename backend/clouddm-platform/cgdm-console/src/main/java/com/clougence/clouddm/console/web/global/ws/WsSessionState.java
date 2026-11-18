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
package com.clougence.clouddm.console.web.global.ws;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

import lombok.Getter;

@Getter
public class WsSessionState {

    static final long                   FRESH_WINDOW_NANOS = 30_000_000_000L;

    private final Long                  sessionId;
    private final String                uid;
    private final Set<WebSocketSession> connections        = ConcurrentHashMap.newKeySet();

    private volatile long               checkedAtNanos     = System.nanoTime() - FRESH_WINDOW_NANOS;
    private volatile long               hardExpireTimeMs;
    private volatile boolean            revoked;
    private volatile String             roleFingerprint;

    public WsSessionState(Long sessionId, String uid){
        this.sessionId = sessionId;
        this.uid = uid;
    }

    public boolean isFresh() {
        return !revoked //
               && (System.nanoTime() - checkedAtNanos) < FRESH_WINDOW_NANOS //
               && System.currentTimeMillis() < hardExpireTimeMs;
    }

    // callers synchronize on this state; never overwrite a newer stamp, never un-revoke
    public void refresh(long checkedAtNanos, long hardExpireTimeMs, String roleFingerprint) {
        if (this.revoked || checkedAtNanos <= this.checkedAtNanos) {
            return;
        }
        // hard expiry lands first so a newly observed stamp never pairs with an old cap
        this.hardExpireTimeMs = hardExpireTimeMs;
        this.roleFingerprint = roleFingerprint;
        this.checkedAtNanos = checkedAtNanos;
    }

    public void markRevoked() {
        this.revoked = true;
    }
}
