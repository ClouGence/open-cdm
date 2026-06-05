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
package com.clougence.clouddm.console.web.component.detectrule.local;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.secrule.RuleKind;
import com.clougence.clouddm.platform.dal.model.secrule.RuleScriptType;
import com.clougence.clouddm.platform.dal.model.secrule.RuleSensitiveMode;
import com.clougence.clouddm.platform.dal.model.secrule.RuleTarget;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SecRuleScriptInfo {

    private String               ruleId;
    private String               ruleName;
    private String               ruleDesc;
    private RuleKind             ruleKind;
    private RuleScriptType       scriptType;
    private String               contentDef;
    private String               content;
    private String               contentMD5;
    private boolean              deprecated;

    private Long                 oldId;

    private List<DataSourceType> dsRange;
    private RuleTarget           ruleTarget;
    private RuleSensitiveMode    senMode;
}
