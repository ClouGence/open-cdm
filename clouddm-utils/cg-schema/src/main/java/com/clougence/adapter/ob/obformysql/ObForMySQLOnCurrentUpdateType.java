package com.clougence.adapter.ob.obformysql;

/**
 * MySQL default on xxxx
 *
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum ObForMySQLOnCurrentUpdateType {

    DefaultGenerated("default_generated"),
    CurrentTimestamp("current_timestamp"),
    CurrentDate("current_date"),
    CurrentTime("current_time");

    private final String typeName;

    ObForMySQLOnCurrentUpdateType(String typeName){
        this.typeName = typeName;
    }

    public static ObForMySQLOnCurrentUpdateType valueOfCode(String code) {
        for (ObForMySQLOnCurrentUpdateType currentType : ObForMySQLOnCurrentUpdateType.values()) {
            if (currentType.typeName.equalsIgnoreCase(code)) {
                return currentType;
            }
        }
        return null;
    }

    public String getTypeName() { return this.typeName; }
}
