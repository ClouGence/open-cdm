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
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.gitops.ScmType;
import com.clougence.clouddm.platform.dal.model.system.ImType;
import com.clougence.clouddm.sdk.scm.ScmEventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_change_flow")
public class DmChangeFlowDO {
    @TableId(type = IdType.AUTO)
    private Long                     id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                     gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                     gmtModified;
    @TableField("owner_uid")
    private String                   ownerUid;
    @TableField("flow_uid")
    private String                   flowUid;
    @TableField("flow_name")
    private String                   flowName;
    @TableField("flow_desc")
    private String                   flowDesc;
    @TableField("flow_manager_uid")
    private String                   flowManagerUid;
    @TableField("flow_status")
    private ChangeFlowStatus         flowStatus;
    @TableField(value = "flow_options", typeHandler = JacksonTypeHandler.class)
    private RsChangeFlowOptionObj    flowOptions;
    @TableField(value = "flow_scm_options", typeHandler = JacksonTypeHandler.class)
    private RsChangeFlowScmOptionObj flowScmOptions;
    @TableField("ref_scm_id")
    private long                     refScmId;
    @TableField("ref_scm_type")
    private ScmType                  refScmType;
    @TableField("scm_repo_space")
    private String                   scmRepoSpace;
    @TableField("scm_repo_identifier")
    private String                   scmRepoIdentifier;
    @TableField("scm_repo_name")
    private String                   scmRepoName;
    @TableField("scm_repo_url")
    private String                   scmRepoUrl;
    @TableField("scm_repo_branch")
    private String                   scmRepoBranch;
    @TableField("scm_repo_event")
    private ScmEventType             scmRepoEvent;
    @TableField("scm_repo_script")
    private String                   scmRepoScript;
    @TableField("scm_repo_hook_pwd")
    private String                   scmBindWebhookPwd;
    @TableField("scm_repo_hook_signing_token")
    private String                   scmBindWebhookSigningToken;
    @TableField("enable_hook")
    private boolean                  enableWebhook;
    @TableField("enable_trigger")
    private boolean                  enableTrigger;
    @TableField("trigger_token")
    private String                   triggerToken;
    @TableField("ds_id")
    private long                     dsId;
    @TableField("ds_type")
    private DataSourceType           dsType;
    @TableField("ds_instance")
    private String                   dsInstance;
    @TableField("ds_desc")
    private String                   dsDesc;
    @TableField("ds_path")
    private String                   dsPath;
    @TableField("ref_msg_id")
    private Long                     refMsgId;
    @TableField("ref_msg_type")
    private ImType                   refMsgType;
    @TableField("msg_language")
    private String                   msgLanguage;
    @TableField("enable_msg")
    private boolean                  enableMsg;
    @TableField("event_flow_status")
    private boolean                  eventFlowStatus;
    @TableField("event_flow_config")
    private boolean                  eventFlowConfig;
    @TableField("event_change_life")
    private boolean                  eventChangeLife;
    @TableField("event_change_notice")
    private boolean                  eventChangeNotice;
    @TableField("callback_url")
    private String                   callbackUrl;
    @TableField("callback_method")
    private String                   callbackMethod;
    @TableField("enable_callback")
    private boolean                  enableCallback;
    @TableField("flow_hashcode")
    private long                     flowHashcode;
    @TableField("enable")
    private boolean                  enable;
    @TableField("deleted")
    private boolean                  deleted;
    @TableField(exist = false)
    private String                   scmValidatedCommitId;
    @TableField(exist = false)
    private List<String>             scmPreflightWarnings;

    public ChangeFlowStatus getChangeFlowStatus() { return flowStatus; }

    public void setChangeFlowStatus(ChangeFlowStatus changeFlowStatus) { this.flowStatus = changeFlowStatus; }

    public RsChangeFlowOptionObj getOptions() { return flowOptions; }

    public void setOptions(RsChangeFlowOptionObj options) { this.flowOptions = options; }

    public void setOptions(RsChangeFlowScmOptionObj options) { this.flowScmOptions = options; }

    public long getRefFlowId() { return id == null ? 0 : id; }

    public void setRefFlowId(long refFlowId) { this.id = refFlowId; }

    public String getLanguage() { return msgLanguage; }

    public void setLanguage(String language) { this.msgLanguage = language; }

    public boolean isEventChangeFlowStatus() { return eventFlowStatus; }

    public void setEventChangeFlowStatus(boolean eventChangeFlowStatus) { this.eventFlowStatus = eventChangeFlowStatus; }

}
