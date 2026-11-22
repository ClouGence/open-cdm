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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;

import jakarta.annotation.Resource;

@Service
public class SessionAccountPolicy {

    @Resource
    private SystemDal systemDal;

    public Long passwordValidDays(DmAuthUserDO user) {
        LocalDateTime expiresAt = passwordExpiresAt(user);
        if (expiresAt == null) {
            return null;
        }
        long diffMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), expiresAt);
        return (long) Math.floor(((double) diffMinutes) / 60 / 24);
    }

    public boolean isPasswordExpired(DmAuthUserDO user) {
        LocalDateTime expiresAt = passwordExpiresAt(user);
        return expiresAt != null && !expiresAt.isAfter(LocalDateTime.now());
    }

    private LocalDateTime passwordExpiresAt(DmAuthUserDO user) {
        if (user.getParentId() == null || user.getLastDateUpdatePwd() == null) {
            return null;
        }
        Integer days = systemDal.fetchSystemConf(RootUserConfig.Fields.accountPwdExpireDays, Integer.class);
        if (days == null || days <= 0) {
            return null;
        }
        LocalDateTime updated = LocalDateTime.ofInstant(user.getLastDateUpdatePwd().toInstant(), ZoneId.systemDefault());
        return updated.plusDays(days);
    }
}
