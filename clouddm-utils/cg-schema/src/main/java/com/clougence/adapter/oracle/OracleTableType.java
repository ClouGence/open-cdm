package com.clougence.adapter.oracle;

import com.clougence.schema.metadata.CgTableType;
import com.clougence.schema.metadata.TableType;

/**
 * Oracle 表类型
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
public enum OracleTableType implements TableType {

    Table("TABLE", CgTableType.BASE_TABLE),
    TEMPORARY("TEMPORARY", null),
    IOT("IOT", null),
    View("VIEW", CgTableType.VIEW);

    private final String typeName;

    private final CgTableType cgTableType;

    OracleTableType(String typeName, CgTableType cgTableType){
        this.typeName = typeName;
        this.cgTableType = cgTableType;
    }

    public CgTableType getCgTableType() { return cgTableType; }

    public String getTypeName() { return this.typeName; }

    public static OracleTableType valueOfCgTableType(CgTableType cgTableType) {
        if (cgTableType == null) {
            return Table;
        }
        for (OracleTableType tableType : OracleTableType.values()) {
            if (tableType.cgTableType == cgTableType) {
                return tableType;
            }
        }
        return Table;
    }

    public static OracleTableType valueOfCode(String code) {
        for (OracleTableType tableType : OracleTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
