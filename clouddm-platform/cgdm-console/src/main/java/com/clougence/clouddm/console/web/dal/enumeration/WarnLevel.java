package com.clougence.clouddm.console.web.dal.enumeration;

import com.clougence.clouddm.sdk.service.secrules.RuleLevel;

import lombok.Getter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
public enum WarnLevel {

    PASS(4, RuleLevel.PASS),       // L4
    SUGGEST(3, RuleLevel.SUGGEST), // L3
    TICKET(2, RuleLevel.TICKET),   // L2
    FAILURE(1, RuleLevel.FAILURE)  // L1
    ;

    private final int       level;
    private final RuleLevel ruleLevel;

    WarnLevel(int level, RuleLevel ruleLevel){
        this.level = level;
        this.ruleLevel = ruleLevel;
    }

    public static WarnLevel valueOfCode(RuleLevel level) {
        if (level == null) {
            return null;
        }

        switch (level) {
            case PASS:
                return WarnLevel.PASS;
            case SUGGEST:
                return WarnLevel.SUGGEST;
            case TICKET:
                return WarnLevel.TICKET;
            case FAILURE:
                return WarnLevel.FAILURE;
            default:
                return null;
        }
    }
}
