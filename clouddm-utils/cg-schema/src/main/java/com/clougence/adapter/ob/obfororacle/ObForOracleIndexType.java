package com.clougence.adapter.ob.obfororacle;

/**
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
public enum ObForOracleIndexType {

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

    ObForOracleIndexType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static ObForOracleIndexType valueOfCode(String code) {
        for (ObForOracleIndexType tableType : ObForOracleIndexType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
