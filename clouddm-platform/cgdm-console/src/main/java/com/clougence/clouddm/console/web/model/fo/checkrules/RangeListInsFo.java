package com.clougence.clouddm.console.web.model.fo.checkrules;

import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.SecMatchMode;
import com.clougence.clouddm.console.web.dal.enumeration.SecRangeType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class RangeListInsFo {

    private String       envId;

    private Long         refId;

    private SecRangeType rangeType;

    private SecMatchMode matchType;

    private RuleKind     ruleKind;
}
