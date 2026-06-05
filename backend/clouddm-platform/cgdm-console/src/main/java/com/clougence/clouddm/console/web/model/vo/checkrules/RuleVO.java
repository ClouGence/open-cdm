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
package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.secrule.RuleKind;
import com.clougence.clouddm.platform.dal.model.secrule.RuleScriptType;
import com.clougence.clouddm.platform.dal.model.secrule.RuleSensitiveMode;
import com.clougence.clouddm.platform.dal.model.secrule.RuleTarget;
import com.clougence.clouddm.sdk.service.secrules.SecParam;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
@Setter
public class RuleVO {

    private long                 ruleId;
    private String               ruleName;
    private String               ruleDesc;
    private RuleScriptType       ruleType;
    private RuleKind             ruleKind;
    private List<SecParam>       ruleParameter;
    private String               ruleContent;
    private boolean              inner;
    private boolean              deprecated;

    // for QUERY
    private List<DataSourceType> dsRange;
    private RuleTarget           targetType;
    private String               targetTypeI18n;

    // for SENSITIVE
    private RuleSensitiveMode    senMode;
    private String               senModeI18n;
}
