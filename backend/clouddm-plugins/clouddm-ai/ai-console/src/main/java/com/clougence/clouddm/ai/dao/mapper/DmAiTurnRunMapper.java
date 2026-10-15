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
package com.clougence.clouddm.ai.dao.mapper;

import java.util.Date;
import java.util.List;

import com.clougence.clouddm.ai.dao.model.DmAiTurnRunDO;
import com.clougence.clouddm.ai.model.AiTurnStatus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface DmAiTurnRunMapper extends BaseMapper<DmAiTurnRunDO> {
    List<DmAiTurnRunDO> findByHistoryIds(@Param("historyIds") List<Long> historyIds, @Param("lock") boolean lock);

    List<DmAiTurnRunDO> findBySessionIds(@Param("sessionIds") List<Long> sessionIds);

    List<DmAiTurnRunDO> findRunningBySessionId(@Param("sessionId") Long sessionId, @Param("lock") boolean lock);

    List<DmAiTurnRunDO> findRunningByDeadline(@Param("now") Date now, @Param("limit") int limit);

    DmAiTurnRunDO findByRunIdForUpdate(@Param("runId") String runId);

    DmAiTurnRunDO findByRunId(@Param("runId") String runId);

    int updateStatusByRunId(@Param("runId") String runId, @Param("status") AiTurnStatus status, @Param("errorCode") String errorCode, @Param("errorMessage") String errorMessage,
                            @Param("gmtModified") Date gmtModified);

    int updateSeqByRunId(@Param("runId") String runId, @Param("seq") long seq);

    int deleteBySessionIdFromHistoryId(@Param("sessionId") Long sessionId, @Param("historyId") Long historyId);

    int deleteBySessionId(@Param("sessionId") Long sessionId);

}
