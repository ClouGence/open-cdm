package com.clougence.adapter.polar.porpg;

/**
 * Postgres 索引类型
 * 
 * @version : 2021-05-17
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PolarDBPgIndexType {

    /** 普通索引 */
    Normal,
    /** 唯一索引 */
    Unique,;

    public static PolarDBPgIndexType valueOfCode(String code) {
        for (PolarDBPgIndexType indexType : PolarDBPgIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        return null;
    }
}
