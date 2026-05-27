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
package com.clougence.clouddm.console.web.service.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.console.web.model.vo.audit.SqlAuditVO;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlAuditDO;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Requester;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SqlAuditServiceImpl implements SqlAuditService {
    @Resource
    private ExecutionDal executionDal;

    private final int    DEFAULT_PAGE_SIZE = 20;
    private final int    MAX_PAGE_SIZE     = 60;

    @Override
    public List<SqlAuditVO> queryUserAllAudit(String puid, String uid, SecQueryKind sqlKind, String resourcePath, Long dsId, Requester requester, SqlStatus status, Date start,
                                              Date end, long startId, int pageSize) {
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        List<DmExecSqlAuditDO> auditDOs = executionDal.sqlAuditMapper().queryByCondition(puid, uid, sqlKind, resourcePath, dsId, requester, status, start, end, startId, pageSize);

        if (auditDOs == null || auditDOs.isEmpty()) {
            return new ArrayList<>();
        }

        return auditDOs.stream().map(SqlAuditVO::convertFromDO).collect(Collectors.toList());
    }
}
