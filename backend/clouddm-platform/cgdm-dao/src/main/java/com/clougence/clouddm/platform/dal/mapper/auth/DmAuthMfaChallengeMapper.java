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
package com.clougence.clouddm.platform.dal.mapper.auth;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthMfaChallengeDO;

public interface DmAuthMfaChallengeMapper extends BaseMapper<DmAuthMfaChallengeDO> {

    DmAuthMfaChallengeDO queryByTokenHash(@Param("challengeTokenHash") String challengeTokenHash);

    int acquireAttempt(@Param("id") Long id, @Param("challengeTokenHash") String challengeTokenHash, @Param("actionType") String actionType,
                       @Param("nowMs") Long nowMs, @Param("maxRetryCount") Integer maxRetryCount);

    int deleteForConsume(@Param("id") Long id, @Param("challengeTokenHash") String challengeTokenHash, @Param("actionType") String actionType,
                         @Param("nowMs") Long nowMs, @Param("maxRetryCount") Integer maxRetryCount);

    int deleteExpired(@Param("nowMs") Long nowMs);
}
