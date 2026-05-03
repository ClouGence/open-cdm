package com.clougence.clouddm.sdk.security.auth;

/**
 * @author mode create time is 2021/2/23
 **/
public enum AuthElementType {

    DataJob("DataJob"),

    // need sync for UmiTypes
    Instance("INSTANCE"),
    Catalog("CATALOG"),
    Schema("SCHEMA"),
    Table("TABLE"),
    Column("COLUMN"),
    //
    View("VIEW"),
    Materialized("MATERIALIZED"),
    Sequence("SEQUENCE"),
    Synonym("SYNONYM"),
    Function("FUNC"),
    Procedure("PROC"),
    Trigger("TRIGGER"),;

    private final String code;

    AuthElementType(String code){
        this.code = code;
    }
}
