package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.sdk.service.secrules.SecParam;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
@Setter
public class SpecRulesVO {

    private Long                 refId;
    private long                 ruleId;
    private String               ruleName;
    private String               ruleDesc;
    private RuleScriptType       ruleType;
    private RuleKind             ruleKind;
    private String               ruleContent;
    private boolean              ruleChange;
    private List<SecParam>       parameterDef;
    private Map<String, String>  parameterValue;
    private boolean              lock;
    private boolean              enable;
    private boolean              deprecated;

    // for QUERY
    private List<DataSourceType> dsRange;
    private RuleTarget           targetType;
    private String               targetTypeI18n;
    private WarnLevel            warnLevel;

    // for SENSITIVE
    private RuleSensitiveMode    senMode;
    private String               senModeI18n;
}
