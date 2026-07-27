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
package com.clougence.clouddm.platform.dal.model.execution;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.api.console.sqlaudit.SqlAuditRequestDTO;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.handler.json.SplitQueryTypeListTypeHandler;
import com.clougence.clouddm.platform.dal.handler.json.SqlAuditRequestListTypeHandler;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_exec_sql_audit", autoResultMap = true)
public class DmExecSqlAuditDO {

    @TableId(type = IdType.AUTO)
    private Long                     id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                     gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                     gmtModified;
    private String                   uid;
    private String                   userName;
    private Date                     operateTime;
    private Date                     endTime;
    private String                   clientIp;
    private String                   logIp;
    private String                   workSeqNumber;
    private String                   execSql;
    private String                   originalSql;
    private Requester                requester;
    private String                   queryId;
    private String                   sessionId;
    @TableField(value = "query_types", typeHandler = SplitQueryTypeListTypeHandler.class)
    private List<SplitQueryType>     queryTypes;
    @TableField(value = "requests", typeHandler = SqlAuditRequestListTypeHandler.class)
    private List<SqlAuditRequestDTO> requests;
    private long                     affectLine;
    private SqlStatus                status;
    private Long                     dsId;
    private DataSourceType           dataSourceType;
    private String                   dsDesc;
    private String                   message;

    @Override
    public String toString() {
        if (this.status == SqlStatus.RUNNING) {
            return this.toBeginString();
        } else {
            return this.toEndString();
        }
    }

    private String toEndString() {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(this.operateTime);
        String sql = this.execSql.replaceAll("\\s+", " ");
        return String.format("%s requestId: %s ,[%s] affectLine: %d ,sql: \"%s\" ,message: %s", format, this.sessionId, this.status, this.affectLine, sql, this.message);

    }

    private String toBeginString() {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(this.operateTime);

        String sql = this.execSql.replaceAll("\\s+", " ");
        return String.format("%s requestId: %s ,[%s] uid: %s, dsId: %3s, sql: \"%s\" ", format, this.sessionId, this.status, this.uid, this.dsId, sql);
    }
}
