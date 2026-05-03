package com.clougence.adapter.ob.obformysql;

import com.clougence.schema.metadata.TableType;

/**
 * @author wanshao
 */
public enum ObForMySQLTableType implements TableType {

    Table("BASE TABLE"),
    View("VIEW"),
    SystemTable("SYSTEM TABLE"),
    SystemView("SYSTEM VIEW");

    private final String typeName;

    ObForMySQLTableType(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() { return this.typeName; }

    public static ObForMySQLTableType valueOfCode(String code) {
        for (ObForMySQLTableType tableType : ObForMySQLTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported tidb tableType " + code);
    }
}
