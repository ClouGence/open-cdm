package com.clougence.clouddm.console.web.component.detectrule.local;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.RuleScriptType;
import com.clougence.clouddm.console.web.dal.enumeration.RuleSensitiveMode;
import com.clougence.clouddm.console.web.dal.enumeration.RuleTarget;

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
