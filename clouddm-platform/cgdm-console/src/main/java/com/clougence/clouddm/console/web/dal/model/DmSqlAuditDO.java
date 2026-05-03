package com.clougence.clouddm.console.web.dal.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Requester;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_sql_audit")
public class DmSqlAuditDO {

    @TableId(type = IdType.AUTO)
    private Long           id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtModified;

    private String         uid;
    private String         userName;
    private String         primaryUid;

    private Date           operateTime;
    private Date           endTime;
    private String         clientIp;
    private String         logIp;
    private String         workSeqNumber;
    private String         execSql;
    private String         originalSql;
    private Requester      requester;
    private String         sessionId;
    private String         resource;
    private SecQueryKind sqlKind;
    private long           affectLine;
    private SqlStatus      status;

    private Long           dsId;
    private DataSourceType dataSourceType;
    private String         dsDesc;
    private String         message;

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
        return String
            .format("%s requestId: %s ,[%s]  uid: %s,dsId: %3s ,operateType: %s ,resource: %s ,sql: \"%s\" ", format, this.sessionId, this.status, this.uid, this.dsId, this.sqlKind, this.resource, sql);
    }
}
