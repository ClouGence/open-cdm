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

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.audit.SqlAuditVO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlAuditDO;
import com.clougence.clouddm.sdk.service.secrules.Requester;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SqlAuditServiceImpl implements SqlAuditService {
    @Resource
    private ExecutionDal   executionDal;
    @Resource
    private ObjectCacheDao objectCacheDao;

    private final int      DEFAULT_PAGE_SIZE = 20;
    private final int      MAX_PAGE_SIZE     = 60;

    @Override
    public DmPageVO<SqlAuditVO> pageUserAllAudit(String puid, String uid, Long dsId, Requester requester, SqlStatus status, Date start, Date end, int pageNumber, int pageSize) {
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        if (pageNumber < 1) {
            pageNumber = 1;
        }

        int offset = (pageNumber - 1) * pageSize;
        List<DmExecSqlAuditDO> auditDOs = executionDal.sqlAuditMapper().pageByCondition(puid, uid, dsId, requester, status, start, end, offset, pageSize);
        long total = executionDal.sqlAuditMapper().countByCondition(puid, uid, dsId, requester, status, start, end);

        if (auditDOs == null || auditDOs.isEmpty()) {
            return new DmPageVO<>(pageNumber, pageSize, total, new ArrayList<>());
        }

        Map<Long, DsCacheEntry> dsCacheById = new HashMap<>();
        auditDOs.stream().map(DmExecSqlAuditDO::getDsId).filter(Objects::nonNull).distinct().forEach(id -> dsCacheById.put(id, objectCacheDao.queryByDsId(id)));

        List<SqlAuditVO> auditVOS = auditDOs.stream().map(auditDO -> {
            SqlAuditVO vo = DmConvertUtils.convertToSqlAuditVO(auditDO);
            DsCacheEntry dsCache = dsCacheById.get(auditDO.getDsId());
            if (dsCache != null) {
                vo.setDsResourceId(dsCache.getDsInstId());
                vo.setDsRemark(dsCache.getDsInstDesc());
            }
            return vo;
        }).collect(Collectors.toList());
        return new DmPageVO<>(pageNumber, pageSize, total, auditVOS);
    }
}
