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
import com.clougence.clouddm.platform.dal.model.ai.DmAiSessionDO;

public interface DmAiSessionMapper extends BaseMapper<DmAiSessionDO> {
    List<DmAiSessionDO> findPageByUid(@Param("puid") String puid, @Param("uid") String uid, @Param("beforeModifiedAt") Date beforeModifiedAt, @Param("beforeId") String beforeId,
                                      @Param("keyword") String keyword, @Param("limit") int limit);

    List<DmAiSessionDO> findByIds(@Param("ids") List<Long> ids, @Param("lock") boolean lock);

    DmAiSessionDO findBySessionId(@Param("id") String id);

    DmAiSessionDO findBySessionIdForUpdate(@Param("id") String id);

    int updateCompactionBySessionId(@Param("sessionId") String sessionId, @Param("compactSummary") String compactSummary,
                                    @Param("compactAnchorHistoryId") Long compactAnchorHistoryId);

    int updateModelBySessionId(@Param("sessionId") String sessionId, @Param("engineId") String engineId, @Param("engineVersion") int engineVersion,
                               @Param("engineName") String engineName, @Param("gmtModified") Date gmtModified);

    int updateModifiedBySessionId(@Param("id") String id, @Param("gmtModified") Date gmtModified);

    int updateTitleBySessionId(@Param("id") String id, @Param("title") String title);

    int deleteBySessionId(@Param("id") String id);

}
