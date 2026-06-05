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
package com.clougence.clouddm.sdk.service.secrules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.detectrule.lang.reflect.RuleIgnore;
import com.clougence.utils.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RuleDomain implements SecResolveName, Domain {

    @RuleIgnore
    private SplitScript         splitScript;

    private SecQueryType        sqlType;
    private SecQueryKind        auditKind;
    private Map<String, String> options;

    private long                envId;
    private String              envName;
    private long                dsId;
    private String              dsName;
    private DataSourceType      dsType;
    private String              userName;
    private String              userRole;
    private String              primaryUid;
    @RuleIgnore
    private List<RuleDomain>    children;

    public TargetType getSqlTarget() {
        if (sqlType == null) {
            return null;
        } else {
            return this.sqlType.getTarget();
        }
    }

    public void addChild(RuleDomain child) {
        if (CollectionUtils.isEmpty(children)) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }
}
