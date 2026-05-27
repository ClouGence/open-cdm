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
import com.clougence.clouddm.platform.dal.model.monitor.AlertEventStatus;
import com.clougence.clouddm.platform.dal.model.monitor.AlertMediaType;

import lombok.Data;

/**
 * @author wanshao create time is 2020/4/13
 **/
@Data
@TableName(value = "dm_mon_alert_event_log")
public class DmMonAlertEventLogDO {

    @TableId(type = IdType.AUTO)
    private Long             id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;

    private AlertEventStatus status;

    private AlertMediaType   alertMediaType;

    private String           content;

    /**
     * console ip
     */
    private String           ip;

    private String           errMsg;

    private String           uid;
}
