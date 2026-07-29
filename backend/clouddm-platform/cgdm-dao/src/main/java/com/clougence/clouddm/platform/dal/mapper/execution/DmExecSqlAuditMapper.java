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
package com.clougence.clouddm.platform.dal.mapper.execution;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlAuditDO;
import com.clougence.clouddm.sdk.service.secrules.Requester;

public interface DmExecSqlAuditMapper extends BaseMapper<DmExecSqlAuditDO> {

    int markRunningByQueryId(DmExecSqlAuditDO audit);

    int completeByQueryId(@Param("queryId") String queryId, @Param("sessionId") String sessionId, @Param("status") String status, @Param("affectLine") long affectLine,
                          @Param("message") String message, @Param("time") Date time);

    List<DmExecSqlAuditDO> pageByCondition(@Param("puid") String puid, @Param("uid") String uid, @Param("dsId") Long dsId, @Param("requester") Requester requester,
                                           @Param("status") SqlStatus status, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd,
                                           @Param("offset") int offset, @Param("pageSize") int pageSize);

    long countByCondition(@Param("puid") String puid, @Param("uid") String uid, @Param("dsId") Long dsId, @Param("requester") Requester requester,
                          @Param("status") SqlStatus status, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

    DmExecSqlAuditDO queryByQueryId(@Param("queryId") String queryId);

    List<DmExecSqlAuditDO> queryWaitConfirmBySessionId(@Param("sessionId") String sessionId);

    List<DmExecSqlAuditDO> queryByCondition(String uid, Long dsId, Requester requester, SqlStatus status, Date dateStart, Date dateEnd, long startId, int pageSize);

    void confirmSession(String sessionId);

    void rollbackSession(String sessionId);

    void updateErrorSql(@Param("wsn") String wsn);

    int deleteAuditBeforeDate(@Param("puid") String puid, @Param("date") Date date);
}
