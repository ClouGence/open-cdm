package com.clougence.clouddm.console.web.service.audit.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmSqlAuditMapper;
import com.clougence.clouddm.console.web.dal.model.DmSqlAuditDO;
import com.clougence.clouddm.console.web.model.vo.audit.SqlAuditVO;
import com.clougence.clouddm.console.web.service.audit.SqlAuditService;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SqlAuditServiceImpl implements SqlAuditService {

    private final int        DEFAULT_PAGE_SIZE = 20;
    private final int        MAX_PAGE_SIZE     = 60;

    @Resource
    private DmSqlAuditMapper sqlAuditMapper;

    @Override
    public List<SqlAuditVO> queryUserAllAudit(String puid, String uid, SecQueryKind sqlKind, String resourcePath, Long dsId, Requester requester, SqlStatus status, Date start,
                                              Date end, long startId, int pageSize) {
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        List<DmSqlAuditDO> auditDOs = sqlAuditMapper.queryByCondition(puid, uid, sqlKind, resourcePath, dsId, requester, status, start, end, startId, pageSize);

        if (auditDOs == null || auditDOs.isEmpty()) {
            return new ArrayList<>();
        }

        return auditDOs.stream().map(SqlAuditVO::convertFromDO).collect(Collectors.toList());
    }
}
