package com.clougence.clouddm.console.web.service.security.mode;

import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.RuleScriptType;
import com.clougence.clouddm.console.web.dal.model.DmSecRuleDO;
import com.clougence.clouddm.console.web.dal.model.DmSecSensitiveDO;

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
