package com.clougence.clouddm.console.web.dal.model.exec;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.AutoExecTaskStatus;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_auto_exec_task")
public class DmAutoExecTaskDO {

    @TableId(type = IdType.AUTO)
    private Long               id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime      gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime      gmtModified;

    private Long               autoExecJobId;
    private String             bizId;
    private Integer            execOrder;
    private String             execSql;
    private AutoExecTaskStatus status;
    private Long               affectRow;
    private SecQueryType       sqlType;
    private Integer            execCount;

    private Date               gmtLastStart;
    private Date               gmtLastEnd;

}
