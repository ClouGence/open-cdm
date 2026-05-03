package com.clougence.rdp.controller.model.enumeration;

import com.clougence.utils.StringUtils;

/**
 * keep,avoid another eventtyp
 */
public enum EventType {

    APPROVAL,;

    public static EventType valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (EventType t : EventType.values()) {
            if (StringUtils.equalsIgnoreCase(t.name(), code)) {
                return t;
            }
        }
        return null;
    }
}
