package com.clougence.adapter.mysql;

/**
 * @author chunlin create time is 2025/3/28
 */
public enum MySQLViewSecurityType {

    DEFINER,
    INVOKER;

    public static MySQLViewSecurityType valueOfCode(String code) {
        for (MySQLViewSecurityType value : values()) {
            if (value.name().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}
