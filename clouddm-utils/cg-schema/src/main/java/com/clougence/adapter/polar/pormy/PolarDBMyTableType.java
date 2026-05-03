package com.clougence.adapter.polar.pormy;

import com.clougence.schema.metadata.TableType;

/**
 * MySQL 表类型
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PolarDBMyTableType implements TableType {

    Table("BASE TABLE"),
    View("VIEW"),
    SystemView("SYSTEM VIEW");

    private final String typeName;

    PolarDBMyTableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PolarDBMyTableType valueOfCode(String code) {
        for (PolarDBMyTableType tableType : PolarDBMyTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
