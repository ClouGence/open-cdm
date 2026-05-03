package com.clougence.adapter.dameng;

import com.clougence.schema.metadata.TableType;

/**
 * 达梦 表类型
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
public enum DmTableType implements TableType {

    Table("TABLE"),
    HUGE("HUGE"),
    TEMPORARY("TEMPORARY"),
    View("VIEW");

    private final String typeName;

    DmTableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static DmTableType valueOfCode(String code) {
        for (DmTableType tableType : DmTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
