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
package com.clougence.clouddm.console.web.model.vo.project;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.*;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.*;
import com.clougence.clouddm.platform.dal.model.datasource.*;
import com.clougence.clouddm.platform.dal.model.execution.*;
import com.clougence.clouddm.platform.dal.model.monitor.*;
import com.clougence.clouddm.platform.dal.model.project.*;
import com.clougence.clouddm.platform.dal.model.secrule.*;
import com.clougence.clouddm.platform.dal.model.system.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectChangeVO {

    private long                    changeId;
    private long                    devopsId;
    private long                    projectId;
    private String                  projectName;
    private ProjectStatus           projectStatus;
    private ChangeCheckStrategy   flowCheck;
    private ChangeApproveStrategy flowApprove;
    private ChangeExecStrategy    flowExecute;

    private long                    scmId;
    private String                  scmDisplay;
    private ScmType                 scmType;
    private String                  scmTypeI18n;

    private String                  repoUrl;
    private String                  repoName;
    private String                  repoBranch;
    private String                  repoScriptPath;

    private long                    dsId;
    private DataSourceType          dsType;
    private String                  dsInstance;
    private String                  dsDesc;
    private String                  dsDisplay;
    private String                  dsHost;
    private List<String>            dsLevels;

    private String                  changeName;
    private String                  changeTime;
    private ProjectChangeStep       currentStep;
    private ProjectChangeStatus     currentStatus;
    private String                  remark;
    private boolean                 locked;

}
