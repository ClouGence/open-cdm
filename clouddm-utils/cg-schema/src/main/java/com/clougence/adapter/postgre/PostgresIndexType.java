package com.clougence.adapter.postgre;

/**
 * Postgres 索引类型
 * 
 * @version : 2021-05-17
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PostgresIndexType {

    /** 普通索引 */
    Normal,
    /** 唯一索引 */
    Unique,;

    public static PostgresIndexType valueOfCode(String code) {
        for (PostgresIndexType indexType : PostgresIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        return null;
    }
}
