package com.clougence.adapter.mysql;

/**
 * MySQL 索引类型
 * 
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum MySQLIndexType {

    /** 普通索引 */
    Normal,
    /** 唯一索引 */
    Unique,
    /** 主键索引 */
    Primary,
    /** 外建索引 */
    Foreign,
    /** 全文索引 */
    FullText,
    /** 空间索引 */
    SPATIAL;

    public static MySQLIndexType valueOfCode(String code) {
        for (MySQLIndexType indexType : MySQLIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        throw new UnsupportedOperationException("Unsupported mysql indexType " + code);
    }
}
