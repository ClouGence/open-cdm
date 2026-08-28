/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.clouddm.console.web.model.vo.cicd;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeFlowType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeFlowRelationItemVO {
    private long           flowId;
    private String         flowName;
    private ChangeFlowType flowType;
    private DataSourceType dsType;
    private String         flowManagerUid;
    private String         flowManagerName;
    private boolean        selectable;
    private String         unavailableReason;
}
