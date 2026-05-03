package com.clougence.adapter.mysql;

import com.clougence.schema.metadata.CgTableType;
import com.clougence.schema.metadata.TableType;

/**
 * MySQL 表类型
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum MySQLTableType implements TableType {

    Table("BASE TABLE", CgTableType.BASE_TABLE),
    View("VIEW", CgTableType.VIEW),
    SystemView("SYSTEM VIEW", null),;

    private final String      typeName;

    private final CgTableType cgTableType;

    MySQLTableType(String typeName, CgTableType cgTableType){
        this.typeName = typeName;
        this.cgTableType = cgTableType;
    }

    public String getTypeName() { return this.typeName; }

    public CgTableType getCgTableType() { return cgTableType; }

    public static MySQLTableType valueOfCgTableType(CgTableType cgTableType) {
        if (cgTableType == null) {
            return Table;
        }
        for (MySQLTableType tableType : MySQLTableType.values()) {
            if (tableType.cgTableType == cgTableType) {
                return tableType;
            }
        }
        return Table;
    }

    public static MySQLTableType valueOfCode(String code) {
        for (MySQLTableType tableType : MySQLTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
