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
package com.clougence.clouddm.console.web.service.security.mode;

import com.clougence.clouddm.platform.dal.model.secrule.RuleKind;
import com.clougence.clouddm.platform.dal.model.secrule.RuleScriptType;
import com.clougence.clouddm.platform.dal.model.secrule.DmSecRuleDO;
import com.clougence.clouddm.platform.dal.model.secrule.DmSecSensitiveDO;

import lombok.Getter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
public class DmSecRuleMO {

    private final Long             id;
    private final RuleKind         ruleKind;
    private final String           ownerUid;
    private final String           ruleId;
    private final String           name;
    private final String           description;
    private final RuleScriptType   scriptType;
    private final String           scriptDef;
    private final String           scriptContent;
    private final String           scriptMD5;
    private final boolean          innerShare;
    private final boolean          deprecated;

    private final DmSecRuleDO      ruleDO;
    private final DmSecSensitiveDO senDO;

    public DmSecRuleMO(DmSecRuleDO ruleDO){
        this.id = ruleDO.getId();
        this.ruleKind = RuleKind.QUERY;
        this.ruleDO = ruleDO;
        this.senDO = null;

        this.ownerUid = ruleDO.getOwnerUid();
        this.ruleId = ruleDO.getRuleId();
        this.name = ruleDO.getName();
        this.description = ruleDO.getDescription();
        this.scriptType = ruleDO.getScriptType();
        this.scriptDef = ruleDO.getScriptDef();
        this.scriptContent = ruleDO.getScriptContent();
        this.scriptMD5 = ruleDO.getScriptMD5();
        this.innerShare = ruleDO.isInnerShare();
        this.deprecated = ruleDO.isDeprecated();
    }

    public DmSecRuleMO(DmSecSensitiveDO senDO){
        this.id = senDO.getId();
        this.ruleKind = RuleKind.SENSITIVE;
        this.ruleDO = null;
        this.senDO = senDO;

        this.ownerUid = senDO.getOwnerUid();
        this.ruleId = senDO.getSenId();
        this.name = senDO.getName();
        this.description = senDO.getDescription();
        this.scriptType = senDO.getScriptType();
        this.scriptDef = senDO.getScriptDef();
        this.scriptContent = senDO.getScriptContent();
        this.scriptMD5 = senDO.getScriptMD5();
        this.innerShare = senDO.isInnerShare();
        this.deprecated = senDO.isDeprecated();
    }
}
