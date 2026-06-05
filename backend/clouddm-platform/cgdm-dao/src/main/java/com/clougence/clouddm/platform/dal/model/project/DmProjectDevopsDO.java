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
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.project.ScmType;
import com.clougence.clouddm.sdk.scm.ScmEventType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_project_devops")
public class DmProjectDevopsDO {

    @TableId(type = IdType.AUTO)
    private Long                  id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                  gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                  gmtModified;

    @TableField("owner_uid")
    private String                ownerUid;

    @TableField("ref_project_id")
    private long                  refProjectId;

    @TableField("ref_scm_id")
    private long                  refScmId;

    @TableField("ref_scm_type")
    private ScmType               refScmType;

    @TableField("scm_repo_space")
    private String                scmRepoSpace;

    @TableField("scm_repo_name")
    private String                scmRepoName;

    @TableField("scm_repo_url")
    private String                scmRepoUrl;

    @TableField("scm_repo_branch")
    private String                scmRepoBranch;

    @TableField("scm_repo_event")
    private ScmEventType          scmRepoEvent;

    @TableField("scm_repo_script")
    private String                scmRepoScript;

    @TableField("ds_id")
    private long                  dsId;

    @TableField("ds_instance")
    private String                dsInstance;

    @TableField("ds_type")
    private DataSourceType        dsType;

    @TableField("ds_desc")
    private String                dsDesc;

    @TableField("ds_path")
    private String                dsPath;

    @TableField(value = "devops_options", typeHandler = JacksonTypeHandler.class)
    private RsProjectDevopsOptionObj options;

    @TableField("devops_hashcode")
    private long                  devopsHashcode;

    @TableField("scm_repo_hook_pwd")
    private String                scmBindWebhookPwd;

    @TableField("enable_hook")
    private boolean               enableWebhook;

    @TableField("callback_url")
    private String                callbackUrl;

    @TableField("callback_method")
    private String                callbackMethod;

    @TableField("enable_callback")
    private boolean               enableCallback;

    @TableField("enable_trigger")
    private boolean               enableTrigger;

    @TableField("trigger_token")
    private String                triggerToken;

    @TableField("enable")
    private boolean               enable;

    @TableField("deleted")
    private boolean               deleted;
}
