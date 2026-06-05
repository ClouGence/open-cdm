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
package com.clougence.clouddm.console.web.component.detectrule;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.secrules.CheckerRule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecCheckerRules {

    private final boolean           valid;
    private final long              envId;
    private final long              dsId;
    private final String            dsName;
    private final DataSourceType    dsType;

    private final String            dsUseSpecName;

    private final List<CheckerRule> queryRuleList;
    private final List<CheckerRule> senRuleList;

    public SecCheckerRules(){
        this.valid = false;
        this.envId = 0L;
        this.dsId = 0L;
        this.dsName = null;
        this.dsType = null;
        this.dsUseSpecName = "";
        this.queryRuleList = Collections.emptyList();
        this.senRuleList = Collections.emptyList();

    }

    public SecCheckerRules(long envId, long dsId, String dsName, DataSourceType dsType, String dsUseSpecName, List<CheckerRule> queryRuleList, List<CheckerRule> senRuleList){
        this.valid = true;
        this.envId = envId;
        this.dsId = dsId;
        this.dsName = dsName;
        this.dsType = dsType;
        this.dsUseSpecName = dsUseSpecName;
        this.queryRuleList = (queryRuleList == null) ? Collections.emptyList() : queryRuleList;
        this.senRuleList = (senRuleList == null) ? Collections.emptyList() : senRuleList;
    }

    public boolean isEmpty() { return this.queryRuleList.isEmpty() && this.senRuleList.isEmpty(); }
}
