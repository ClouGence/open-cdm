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
package com.clougence.clouddm.console.web.model.vo.cicd;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.gitops.ScmType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeVO {

    private long                   changeId;
    private Long                   ticketId;
    private Long                   batchId;
    private ChangeBatchStatus      batchStatus;
    private Long                   rootChangeId;
    private Long                   parentChangeId;
    private Long                   parentFlowId;
    private String                 parentFlowName;
    private List<ChangeTransferVO> downstream;
    private ChangeFlowType         flowType;
    private long                   flowId;
    private String                 flowName;
    private String                 flowManagerName;
    private ChangeFlowStatus       flowStatus;

    private Long                   scmId;
    private String                 scmDisplay;
    private ScmType                scmType;
    private String                 scmTypeI18n;

    private String                 repoUrl;
    private String                 repoName;
    private String                 repoBranch;
    private String                 repoScriptPath;

    private long                   dsId;
    private DataSourceType         dsType;
    private String                 dsInstance;
    private String                 dsDesc;
    private String                 dsDisplay;
    private String                 dsHost;
    private List<String>           dsLevels;

    private String                 changeName;
    private String                 changeTime;
    private ChangeStep             currentStep;
    private ChangeStatus           currentStatus;
    private String                 remark;
    private boolean                locked;

    public ChangeFlowStatus getChangeFlowStatus() { return flowStatus; }

    public void setChangeFlowStatus(ChangeFlowStatus changeFlowStatus) { this.flowStatus = changeFlowStatus; }
}
