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
package com.clougence.clouddm.platform.dal.model.secrule;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.secrule.SecMatchMode;
import com.clougence.clouddm.platform.dal.model.secrule.SecRangeType;
import com.clougence.clouddm.platform.dal.handler.SecRangeTypeHandler;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/7/10 15:19
*/
@Getter
@Setter
@TableName(value = "dm_sec_range")
public class DmSecRangeDO {

    @TableId(type = IdType.AUTO)
    private Long           id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtModified;

    private String         ownerUid;

    private long           refSpec;

    private long           refId;

    private SecMatchMode   matchMode;

    @TableField(typeHandler = SecRangeTypeHandler.class)
    private SecRangeType   rangeType;

    private String         levelPrefix;

    @TableField(value = "level_nodes", typeHandler = JacksonTypeHandler.class)
    private List<String>   levelNodes;

    private boolean        chooseAll;

    @TableField("ref_ds_type")
    private DataSourceType referDsType;

    @TableField("table_level")
    private TargetType     tableLevelType; // Table or View or Materialized
}
