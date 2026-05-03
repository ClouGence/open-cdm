package com.clougence.clouddm.console.web.model.fo.checkrules;

import java.util.Map;

import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.RuleSensitiveMode;
import com.clougence.clouddm.console.web.dal.enumeration.WarnLevel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class SpecRulesFO {

    private Long                refId;
    private Long                ruleId;
    private RuleKind            ruleKind;
    private boolean             enable;
    private Map<String, String> ruleParam;

    // for QUERY
    private WarnLevel           warnLevel;

    // for SENSITIVE
    private RuleSensitiveMode   senMode;
}
