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
package com.clougence.clouddm.platform.dal.model.cicd;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_change_version")
public class DmChangeVersionDO {
    @TableId(type = IdType.AUTO)
    private Long              id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtModified;
    @TableField("owner_uid")
    private String            ownerUid;
    @TableField("ref_flow_id")
    private long              refFlowId;
    @TableField("ref_change_id")
    private long              refChangeId;
    @TableField("version")
    private Date              version;
    @TableField("commit_id")
    private String            commitId;
    @TableField("content")
    private String            content;
    @TableField("type")
    private ChangeVersionType type;
}
