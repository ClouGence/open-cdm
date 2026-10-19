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
package com.clougence.clouddm.platform.dal.model.ai;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clougence.clouddm.platform.dal.handler.encrypt.StrSecretTypeHandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_ai_event", autoResultMap = true)
public class DmAiHistoryEventDO {
    @TableId(type = IdType.AUTO)
    private Long   id;
    private Long   sessionId;
    private Long   historyId;
    private String eventType;
    @TableField(typeHandler = StrSecretTypeHandler.class)
    private String eventData;
    private Date   gmtCreate;
    private Date   gmtModified;
}
