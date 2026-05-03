package com.clougence.clouddm.console.web.model.fo.checkrules;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.SecMatchMode;
import com.clougence.clouddm.console.web.dal.enumeration.SecRangeType;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class SpecSaveRangeFO {

    private Long         rangeId;

    private long         specId;
    private long         ruleId;
    private RuleKind     ruleKind;
    private boolean      force;

    private SecMatchMode matchMode;
    private SecRangeType rangeType;
    private TargetType   tableLevelType;
    private Long         envId;
    private Long         dsId;
    private String       catalog;
    private String       schema;
    private String       table;
    private List<String> nodes;

    private boolean      chooseAll;

}
