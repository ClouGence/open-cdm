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
package com.clougence.clouddm.platform.dal.model.approval;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.platform.dal.handler.enums.RdpApprovalTypeHandler;
import com.clougence.utils.CollectionUtils;

import lombok.Data;

/**
 * @author Ekko
 * @date 2024/5/6 10:58
*/
@Data
@TableName(value = "dm_approval", autoResultMap = true)
public class DmApprovalDO {

    // basic information
    @TableId(type = IdType.AUTO)
    private Long                  id;
    private String                bizId;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                  gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                  gmtModified;
    private String                ownerUid;
    private String                primaryUid;
    private Long                  bindDsId;
    private String                targetInfo;
    private ApprovalBiz           approBiz;
    private String                description;
    private String                ticketTitle;
    private String                envName;
    private String                rawSql;
    private SqlContentType        contentType;
    @TableField(value = "levels", typeHandler = JacksonTypeHandler.class)
    private List<String>          levels;
    private Boolean               deleted;

    // ticket flow
    @TableField(typeHandler = RdpApprovalTypeHandler.class)
    private ApprovalType          approType;
    private String                approIdentity;
    private String                approTemplateName;
    private String                approTemplateIdentity;
    private String                approComment;
    private ApprovalStatus        ticketStatus;
    private String                statusMessage;
    private Date                  finishTime;
    private String                approvalUrl;
    @TableField(value = "features", typeHandler = JacksonTypeHandler.class)
    private List<ApprovalFeature> features;

    // ticket execution
    @TableField("`expected_affected_rows`")
    private Long                  expectedAffectedRows;
    private String                rollBackSql;
    private String                ticketInfo;

    public boolean hasFeature(ApprovalFeature feature) {
        return CollectionUtils.isNotEmpty(this.features) && this.features.contains(feature);
    }

    public String getLevelPath() {
        if (CollectionUtils.isEmpty(this.levels)) {
            return "/";
        }
        StringBuilder sb = new StringBuilder("/");
        for (String level : this.levels) {
            sb.append(level);
            sb.append("/");
        }
        return sb.toString();
    }
}
