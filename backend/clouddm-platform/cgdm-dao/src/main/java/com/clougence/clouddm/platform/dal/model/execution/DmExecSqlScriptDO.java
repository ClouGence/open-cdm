/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.platform.dal.model.execution;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("dm_exec_sql_script")
public class DmExecSqlScriptDO {

    @TableId(type = IdType.AUTO)
    private Long            id;
    private Date            gmtCreate;
    private Date            gmtModified;
    private String          ownerUid;
    private String          dsType;
    private String          name;
    private String          fileUri;
    private SqlScriptStatus status;
    private Long            version;
}
