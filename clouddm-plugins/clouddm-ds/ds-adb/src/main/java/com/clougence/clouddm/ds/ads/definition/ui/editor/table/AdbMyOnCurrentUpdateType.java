package com.clougence.clouddm.ds.ads.definition.ui.editor.table;

/**
 * AdbMySQL default on xxxx
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum AdbMyOnCurrentUpdateType {

    CurrentTimestamp("current_timestamp"),
    CurrentDate("current_date"),
    CurrentTime("current_time"),
    EMPTY("");

    private final String typeName;

    AdbMyOnCurrentUpdateType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static AdbMyOnCurrentUpdateType valueOfCode(String code) {
        for (AdbMyOnCurrentUpdateType currentType : AdbMyOnCurrentUpdateType.values()) {
            if (currentType.typeName.equalsIgnoreCase(code)) {
                return currentType;
            }
        }
        return null;
    }
}
