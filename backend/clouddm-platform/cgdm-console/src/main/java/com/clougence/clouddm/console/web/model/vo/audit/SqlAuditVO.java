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
package com.clougence.clouddm.console.web.model.vo.audit;

import java.util.Date;

import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlAuditDO;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlAuditVO {

    private Long           id;

    //    private String    uid;
    private String         userName;
    private String         primaryUid;

    private Date           operateTime;
    private Long           cost;
    private String         clientIp;
    private String         logIp;
    private String         execSql;
    private Requester      requester;
    private String         resource;
    private SecQueryKind   sqlKind;
    private long           affectLine;
    private SqlStatus      status;

    private boolean        rewrite;
    private String         originalSql;

    private Long           dsId;
    private String         dsDesc;
    private DataSourceType dataSourceType;
    private String         message;

    public static SqlAuditVO convertFromDO(DmExecSqlAuditDO auditDO) {
        SqlAuditVO vo = new SqlAuditVO();
        vo.setId(auditDO.getId());
        if (auditDO.getEndTime() != null) {
            long cost = auditDO.getEndTime().getTime() - auditDO.getOperateTime().getTime();
            if (cost == 0) {
                cost = 1;
            }
            vo.setCost(cost);
        }

        vo.setDataSourceType(auditDO.getDataSourceType());
        vo.setUserName(auditDO.getUserName() + "(" + auditDO.getUid() + ")");
        vo.setPrimaryUid(auditDO.getPrimaryUid());
        vo.setOperateTime(auditDO.getOperateTime());
        vo.setExecSql(auditDO.getExecSql());
        vo.setRewrite(StringUtils.isNotBlank(auditDO.getOriginalSql()));
        vo.setOriginalSql(auditDO.getOriginalSql());

        vo.setClientIp(auditDO.getClientIp());
        vo.setLogIp(auditDO.getLogIp());

        vo.setRequester(auditDO.getRequester());
        vo.setResource(auditDO.getResource());
        vo.setSqlKind(auditDO.getSqlKind());
        vo.setAffectLine(auditDO.getAffectLine());
        vo.setStatus(auditDO.getStatus());

        vo.setDsId(auditDO.getDsId());
        vo.setDsDesc(auditDO.getDsDesc());
        vo.setMessage(auditDO.getMessage());

        return vo;
    }
}
