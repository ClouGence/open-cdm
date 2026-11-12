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
package com.clougence.clouddm.console.web.global.session;

import com.clougence.clouddm.console.web.constants.LoginAuthType;

import jakarta.servlet.http.HttpServletRequest;

public interface SessionService {

    /**
     * Creates a fresh session and returns its plaintext token. Contract: the
     * caller MUST hold the user row lock (see DmAuthUserMapper.lockByUid) and
     * have verified an active user inside the surrounding transaction.
     */
    String create(String uid, LoginAuthType loginType);

    SessionPrincipal authenticate(HttpServletRequest request);

    SessionPrincipal authenticateById(Long sessionId);

    java.util.Map<Long, SessionPrincipal> authenticateByIds(java.util.Collection<Long> sessionIds);

    void renewIfNeeded(SessionPrincipal principal);

    void revokeUserSessions(String uid);

    /**
     * Idempotent logout helper: extracts the bounded session cookie, looks the
     * row up by token hash even when already expired and deletes it if present.
     * The raw token is never retained.
     */
    void revokeCurrentSession(HttpServletRequest request);
}
