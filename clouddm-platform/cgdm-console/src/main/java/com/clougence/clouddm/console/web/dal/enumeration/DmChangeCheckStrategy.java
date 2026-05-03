package com.clougence.clouddm.console.web.dal.enumeration;

import lombok.Getter;

/**
 * @author mode 2021/1/7 19:16
 */
@Getter
public enum DmChangeCheckStrategy {

    Skip(null),
    Suggest(WarnLevel.SUGGEST),   // L3
    Ticket(WarnLevel.TICKET),     // L2
    Failure(WarnLevel.FAILURE),   // L1
    Always(null),;

    private final WarnLevel level;

    DmChangeCheckStrategy(WarnLevel level){
        this.level = level;
    }
}
