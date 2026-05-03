package com.clougence.adapter.oracle;

/**
 * Oracle 索引类型
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
public enum OracleIndexType {

    Lob("LOB"),
    Normal("NORMAL"),
    NormalRev("NORMAL/REV"),
    Bitmap("BITMAP"),
    FunctionBasedNormal("FUNCTION-BASED NORMAL"),
    FunctionBasedNormalRev("FUNCTION-BASED NORMAL/REV"),
    FunctionBasedBitmap("FUNCTION-BASED BITMAP"),
    FunctionBasedDomain("FUNCTION-BASED DOMAIN"),
    Cluster("CLUSTER"),
    IotTop("IOT - TOP"),
    Domain("DOMAIN"),;

    private final String typeName;

    OracleIndexType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static OracleIndexType valueOfCode(String code) {
        for (OracleIndexType tableType : OracleIndexType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
