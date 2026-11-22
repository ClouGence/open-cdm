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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Configuration
public class SessionConfig {

    @Resource
    @Getter(AccessLevel.NONE)
    private Environment environment;

    @Value("${clougence.rdp.session.activity-window-sec:86400}")
    private int activityWindowSec;

    @Value("${clougence.rdp.session.renewal-threshold-sec:21600}")
    private int renewalThresholdSec;

    @Value("${clougence.rdp.session.absolute-lifetime-sec:0}")
    private int absoluteLifetimeSec;

    @PostConstruct
    public void validate() {
        if (environment.containsProperty("clougence.rdp.login.expire.sec")) {
            log.warn("clougence.rdp.login.expire.sec has been removed and is ignored. Set clougence.rdp.session.activity-window-sec instead; current activity window is {} seconds.", activityWindowSec);
        }
        if (activityWindowSec <= 0) {
            throw new IllegalStateException("clougence.rdp.session.activity-window-sec must be positive.");
        }
        if (renewalThresholdSec <= 0 || renewalThresholdSec >= activityWindowSec) {
            throw new IllegalStateException("clougence.rdp.session.renewal-threshold-sec must be positive and less than activity-window-sec.");
        }
        if (absoluteLifetimeSec != 0 && absoluteLifetimeSec < activityWindowSec) {
            throw new IllegalStateException("clougence.rdp.session.absolute-lifetime-sec must be zero or not less than activity-window-sec.");
        }
    }

    public long activityWindowMs() {
        return activityWindowSec * 1000L;
    }

    public long renewalThresholdMs() {
        return renewalThresholdSec * 1000L;
    }

    public long absoluteLifetimeMs() {
        return absoluteLifetimeSec * 1000L;
    }

    public long absoluteDeadlineMs(long createdTimeMs) {
        if (absoluteLifetimeSec == 0) {
            return Long.MAX_VALUE;
        }
        return createdTimeMs + absoluteLifetimeMs();
    }
}
