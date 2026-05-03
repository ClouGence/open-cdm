package com.clougence.clouddm.console.web.model.fo.checkrules;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class SpecDeleteRuleFO {

    private long        specId;

    private boolean     force;

    private SpecRulesFO rule;
}
