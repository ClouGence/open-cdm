package com.clougence.adapter.ob.obfororacle;

import com.clougence.schema.metadata.TableType;

/**
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
public enum ObForOracleTableType implements TableType {

    Table("TABLE"),
    TEMPORARY("TEMPORARY"),
    IOT("IOT"),
    View("VIEW");

    private final String typeName;

    ObForOracleTableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static ObForOracleTableType valueOfCode(String code) {
        for (ObForOracleTableType tableType : ObForOracleTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
