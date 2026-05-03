package com.clougence.clouddm.console.web.model.fo.checkrules;

import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class RuleDeleteFO {

    private long     ruleId;

    private RuleKind ruleKind;

    private boolean  force;
}
