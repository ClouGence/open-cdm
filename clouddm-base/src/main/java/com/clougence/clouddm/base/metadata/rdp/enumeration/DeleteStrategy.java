package com.clougence.clouddm.base.metadata.rdp.enumeration;

public enum DeleteStrategy {

    AUTO,

    FORCE_DV,

    FORCE_REWRITE;

    public static DeleteStrategy valueOfCode(String code) {
        for (DeleteStrategy tableType : DeleteStrategy.values()) {
            if (tableType.name().equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return FORCE_REWRITE;
    }
}
