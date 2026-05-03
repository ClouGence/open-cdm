package com.clougence.adapter.mysql;

import com.clougence.utils.StringUtils;

/**
 * MySQL default on xxxx
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum MySQLOnCurrentUpdateType {

    CurrentTimestamp("current_timestamp"),
    CurrentDate("current_date"),
    CurrentTime("current_time");

    private final String typeName;

    MySQLOnCurrentUpdateType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static MySQLOnCurrentUpdateType valueOfCode(String code) {
        for (MySQLOnCurrentUpdateType currentType : MySQLOnCurrentUpdateType.values()) {
            if (StringUtils.equalsIgnoreCase(currentType.typeName, code)) {
                return currentType;
            }
        }
        return null;
    }
}
