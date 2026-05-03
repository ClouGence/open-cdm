package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.RuleScriptType;
import com.clougence.clouddm.console.web.dal.enumeration.RuleSensitiveMode;
import com.clougence.clouddm.console.web.dal.enumeration.RuleTarget;
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
