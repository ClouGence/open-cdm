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
package com.clougence.clouddm.platform.dal.model.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/2/29 11:48
 */
@Getter
@Setter
@TableName(value = "dm_auth_res", autoResultMap = true)
public class DmAuthResDO {

    @TableId(type = IdType.AUTO)
    private Long         id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtModified;

    private String       ownerUid;

    private Long         resId;

    private String       resInstId;

    private String       resDesc;

    private String       resPath;

    private String       levelOne;

    private String       levelTwo;

    private String       levelThree;

    private String       levelFour;

    private AuthKind     kindType;

    private Date         startTime;

    private Date         endTime;

    @TableField(value = "res_auth_label", typeHandler = JacksonTypeHandler.class)
    private List<String> authLabels = new ArrayList<>();

    public boolean isEffective() {
        Date now = new Date();
        if (this.getStartTime() == null && this.getEndTime() == null) {
            return true;
        } else if (this.getStartTime() != null && this.getEndTime() != null) {
            return now.after(this.getStartTime()) && now.before(this.getEndTime());
        } else if (this.getStartTime() != null) {
            return now.after(this.getStartTime());
        } else {
            return now.before(this.getEndTime());
        }
    }

    public boolean isNotExpired() {
        if (this.endTime == null) {
            return true;
        }
        return new Date().before(this.endTime);
    }
}
