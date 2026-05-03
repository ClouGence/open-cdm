package com.clougence.adapter.polar.pormy;

/**
 * MySQL 索引类型
 * 
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PolarDBMyIndexType {

    /** 普通索引 */
    Normal,
    /** 唯一索引 */
    Unique,
    /** 主键索引 */
    Primary,
    /** 外建索引 */
    Foreign,
    /** 全文索引 */
    FullText;

    public static PolarDBMyIndexType valueOfCode(String code) {
        for (PolarDBMyIndexType indexType : PolarDBMyIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        throw new UnsupportedOperationException("Unsupported PolarDBMy indexType " + code);
    }
}
