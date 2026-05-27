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
package com.clougence.clouddm.platform.dal.model.project;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.platform.dal.model.project.ChangeItemType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020/11/9 13:23
 */
@Getter
@Setter
@TableName(value = "dm_project_change_item")
public class DmProjectChangeItemDO {

    @TableId(type = IdType.AUTO)
    private Long             id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtModified;

    @TableField("owner_uid")
    private String           ownerUid;

    @TableField("ref_project_id")
    private long             refProjectId;

    @TableField("ref_change_id")
    private long             refChangeId;

    @TableField("ref_change_item_type")
    private ChangeItemType changeItemType;

    @TableField("content_name")
    private String           contentName;

    @TableField("content_index")
    private int              contentIndex;

    @TableField("content")
    private String           content;
}
