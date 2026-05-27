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
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalType;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStatus;
import com.clougence.clouddm.platform.dal.handler.RdpApprovalTypeHandler;
import com.clougence.utils.CollectionUtils;

import lombok.Data;

/**
 * @author Ekko
 * @date 2024/5/6 10:58
*/
@Data
@TableName(value = "dm_approval")
public class DmApprovalDO {

    @TableId(type = IdType.AUTO)
    private Long            id;

    private String          bizId;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtModified;

    private String          ownerUid;

    // resource Id,can be null
    private Long            bindDsId;

    private String          targetInfo;

    @TableField(typeHandler = RdpApprovalTypeHandler.class)
    private ApprovalType approType;

    private ApprovalBiz  approBiz;

    private String          approIdentity;

    private String          approTemplateName;

    private String          approTemplateIdentity;

    private String          approComment;

    private String          description;

    private String          ticketTitle;

    private ApprovalStatus ticketStatus;

    private Date            finishTime;

    private String          statusMessage;

    private Boolean         deleted;

    private Integer         errorCount;

    private String          primaryUid;

    private String          envName;

    private String          approvalUrl;

    private String          sessionId;

    private String          explainSqlData;

    private Integer         riskSqlCount;

    private String          rawSql;

    private Integer         totalCount;

    @TableField("`expected_affected_rows`")
    private Long            expectedAffectedRows;

    private Date            expectedExecTime;

    private String          rollBackSql;

    private String          ticketInfo;

    @TableField(value = "levels", typeHandler = JacksonTypeHandler.class)
    private List<String>    levels;

    private String          checkedInfo;

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
