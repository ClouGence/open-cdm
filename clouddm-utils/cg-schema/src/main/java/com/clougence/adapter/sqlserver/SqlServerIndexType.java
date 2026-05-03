package com.clougence.adapter.sqlserver;

/**
 * SqlServer index type
 * https://docs.microsoft.com/zh-cn/sql/relational-databases/system-catalog-views/sys-indexes-transact-sql?view=sql-server-ver16
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
public enum SqlServerIndexType {

    //0 = 堆
    Heap("0"),
    //1 = B 树 (聚集行存储)
    Clustered("1"),
    //2 = 非聚集行存储 (B 树)
    NonClustered("2"),
    //3 = XML
    Xml("3"),
    //4 = 空间
    Spatial("4"),
    //5 = 聚集列存储索引。 适用于：SQL Server 2014 (12.x) 及更高版本。
    ClusteredColumnStore("5"),
    //6 = 非聚集列存储索引。 适用于：SQL Server 2012 (11.x) 及更高版本。
    NonClusteredColumnStore("6"),
    //7 = 非聚集哈希索引。 适用于：SQL Server 2014 (12.x) 及更高版本。
    NonClusteredHash("7"),;

    private final String typeName;

    SqlServerIndexType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static SqlServerIndexType valueOfCode(String code) {
        for (SqlServerIndexType tableType : SqlServerIndexType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
