package com.clougence.clouddm.sdk.service.secrules;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Getter
@Setter
public class CheckerRule {

    private String              ruleName;
    private String              ruleDesc;

    private Map<String, String> parameters;
    private String              scriptType;
    private String              script;
    private List<CheckerRange>  rangeList;

    // for QUERY
    private RuleLevel           level;
    private TargetType          target;

    // for SENSITIVE
    private SensitiveMode       senMode;
}
