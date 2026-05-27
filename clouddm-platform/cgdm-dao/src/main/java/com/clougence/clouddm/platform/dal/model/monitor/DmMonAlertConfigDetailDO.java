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
package com.clougence.clouddm.platform.dal.model.monitor;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.platform.dal.model.monitor.EventType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2020/4/13
 **/
@Getter
@Setter
@TableName(value = "dm_mon_alert_config_detail")
public class DmMonAlertConfigDetailDO {

    @TableId(type = IdType.AUTO)
    private Long        id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date        gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date        gmtModified;

    private EventType eventType;

    private String      uid;

    private boolean     phone;

    private boolean     email;

    private boolean     dingding;

    private boolean     sms;

    private boolean     duplicated;

    private String      ruleName;

    private String      expression;

    private boolean     sendAdmin;

    private boolean     sendSystem;

    private Long        workerId;
}
