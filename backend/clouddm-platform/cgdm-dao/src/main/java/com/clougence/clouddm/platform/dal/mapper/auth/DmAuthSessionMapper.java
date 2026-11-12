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

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthSessionDO;

public interface DmAuthSessionMapper extends BaseMapper<DmAuthSessionDO> {

    DmAuthSessionDO queryByTokenHash(@Param("tokenHash") String tokenHash);

    List<DmAuthSessionDO> queryByIds(@Param("ids") Collection<Long> ids);

    int renewCas(@Param("id") Long id, @Param("oldExpireTimeMs") Long oldExpireTimeMs, @Param("newExpireTimeMs") Long newExpireTimeMs, @Param("renewedTimeMs") Long renewedTimeMs);

    int deleteByUid(@Param("uid") String uid);

    int deleteExpiredByExpire(@Param("nowMs") Long nowMs, @Param("limit") int limit);

    int deleteExpiredByAbsolute(@Param("cutoffGmtCreateMs") Long cutoffGmtCreateMs, @Param("limit") int limit);
}
