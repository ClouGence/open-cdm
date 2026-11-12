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

import lombok.Getter;

@Getter
public class SessionPrincipal {

    private final Long          sessionId;
    private final String        uid;
    private final LoginAuthType loginType;
    private final long          createdTimeMs;
    private final long          renewedTimeMs;
    private final long          expireTimeMs;

    public SessionPrincipal(Long sessionId, String uid, LoginAuthType loginType, long createdTimeMs, long renewedTimeMs, long expireTimeMs){
        this.sessionId = sessionId;
        this.uid = uid;
        this.loginType = loginType;
        this.createdTimeMs = createdTimeMs;
        this.renewedTimeMs = renewedTimeMs;
        this.expireTimeMs = expireTimeMs;
    }
}
