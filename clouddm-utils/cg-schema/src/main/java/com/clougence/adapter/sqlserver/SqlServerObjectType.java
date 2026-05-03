package com.clougence.adapter.sqlserver;

import com.clougence.schema.metadata.TableType;

/**
 * https://docs.microsoft.com/zh-cn/sql/relational-databases/system-catalog-views/sys-objects-transact-sql?view=sql-server-ver16
 * @author mode 2021/11/16 11:15:51
 */
public enum SqlServerObjectType implements TableType {

    AF("AF"),
    C("C"),
    D("D"),
    F("F"),
    FN("FN"),
    FS("FS"),
    FT("FT"),
    IF("IF"),
    IT("IT"),
    P("P"),
    PC("PC"),
    PG("PG"),
    PK("PK"),
    R("R"),
    RF("RF"),
    S("S"),
    SN("SN"),
    SO("SO"),
    U("U"),
    V("V"),
    EC("EC"),
    SQ("SQ"),
    TA("TA"),
    TF("TF"),
    TR("TR"),
    TT("TT"),
    UQ("UQ"),
    X("X"),
    ST("ST"),
    ET("ET"),;

    private final String typeName;

    SqlServerObjectType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static SqlServerObjectType valueOfCode(String code) {
        for (SqlServerObjectType tableType : SqlServerObjectType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code.trim())) {
                return tableType;
            }
        }
        return null;
    }
}
