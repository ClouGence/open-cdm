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
package com.clougence.clouddm.platform.dal.model.datasource;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * @author wanshao create time is 2019/12/11 10:11 下午 finished
 **/
@Data
@FieldNameConstants
@TableName(value = "dm_ds")
public class DmDsDO {

    @TableId(type = IdType.AUTO)
    private Long             id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtModified;
    private String           uid;
    private String           owner;
    private DataSourceType   dataSourceType;
    private String           host;
    private String           instanceDesc;
    private String           version;
    private String           driver;
    private String           instanceId;
    private String           accessKey;
    private String           secretKey;
    private LifeCycleState   lifeCycleState;
    private DataSourceStatus status;
    private String           statusMessage;
    private Long             bindClusterId;
    private SecurityType     securityType;
    private Long             dsEnvId;
    @TableField(exist = false)
    private DmSysEnvDO       dsEnvDO;
}
