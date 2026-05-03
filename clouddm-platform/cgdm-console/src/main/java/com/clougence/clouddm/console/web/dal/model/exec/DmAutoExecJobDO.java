package com.clougence.clouddm.console.web.dal.model.exec;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.console.web.dal.enumeration.AutoExecJobStatus;
import com.clougence.clouddm.console.web.dal.enumeration.DmAutoExecType;
import com.clougence.clouddm.console.web.dal.enumeration.SQLJobBizType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_auto_exec_job")
public class DmAutoExecJobDO {

    @TableId(type = IdType.AUTO)
    private Long              id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtModified;
    private Long              dataSourceId;
    private String            uid;
    private String            bizId;
    private SQLJobBizType     dependOnBizType;
    private String            dependOnBizId;
    private AutoExecJobStatus status;
    private Date              lastReportTime;
    private String            workerSeqNumber;
    private DmAutoExecType    execType;
    private Date              endTime;
    private Date              scheduleTime;
    private String            queryId;
    private String            primaryUid;
    private Boolean           normal;

    @TableField(value = "levels", typeHandler = JacksonTypeHandler.class)
    private List<String>      levels;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private AutoExecJobConfig config;
}
