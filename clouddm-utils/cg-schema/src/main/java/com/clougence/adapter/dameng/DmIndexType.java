package com.clougence.adapter.dameng;

/**
 * 达梦 索引类型
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
public enum DmIndexType {

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

    DmIndexType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static DmIndexType valueOfCode(String code) {
        for (DmIndexType tableType : DmIndexType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
