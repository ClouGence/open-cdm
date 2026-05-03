package com.clougence.adapter.polar.porpg;

import com.clougence.schema.metadata.TableType;

/**
 * Postgres 的表类型
 * @version : 2021-05-10
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PolarDBPgTableType implements TableType {

    Table("BASE TABLE"),
    View("VIEW"),
    ForeignTable("FOREIGN"),
    LocalTemporary("LOCAL TEMPORARY"),
    Materialized("MATERIALIZED VIEW"),;

    private final String typeName;

    PolarDBPgTableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PolarDBPgTableType valueOfCode(String code) {
        for (PolarDBPgTableType tableType : PolarDBPgTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
