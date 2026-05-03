package com.clougence.clouddm.console.web.model.fo.checkrules;

import com.clougence.clouddm.console.web.dal.enumeration.RuleScriptType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class RuleExtractParametersFO {

    private RuleScriptType type;

    private String         content;
}
