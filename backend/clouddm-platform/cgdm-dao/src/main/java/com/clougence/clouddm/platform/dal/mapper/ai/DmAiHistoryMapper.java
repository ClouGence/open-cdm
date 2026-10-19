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
package com.clougence.clouddm.platform.dal.mapper.ai;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.ai.DmAiHistoryDO;

public interface DmAiHistoryMapper extends BaseMapper<DmAiHistoryDO> {
    List<DmAiHistoryDO> findPageBySessionId(@Param("sessionId") Long sessionId, @Param("beforeId") Long beforeId, @Param("limit") int limit, @Param("lock") boolean lock);

    List<DmAiHistoryDO> findBySessionId(@Param("sessionId") Long sessionId);

    List<DmAiHistoryDO> findIdsBySessionIds(@Param("sessionIds") List<Long> sessionIds);

    List<DmAiHistoryDO> findByIds(@Param("ids") List<Long> ids, @Param("lock") boolean lock);

    List<DmAiHistoryDO> findByUuids(@Param("uuids") List<String> uuids);

    long findCurrentTimeMillis();

    DmAiHistoryDO findByUuid(@Param("uuid") String uuid);

    DmAiHistoryDO findByUuidForUpdate(@Param("uuid") String uuid);

    DmAiHistoryDO findBySessionIdAndRequestId(@Param("sessionId") Long sessionId, @Param("requestId") String requestId, @Param("lock") boolean lock);

    int updateUsageById(@Param("id") Long id, @Param("usageJson") String usageJson, @Param("gmtModified") Date gmtModified);

    int updateReferencesById(@Param("id") Long id, @Param("references") String references);

    int deleteBySessionIdFromId(@Param("sessionId") Long sessionId, @Param("historyId") Long historyId);

    int deleteBySessionId(@Param("sessionId") Long sessionId);

}
