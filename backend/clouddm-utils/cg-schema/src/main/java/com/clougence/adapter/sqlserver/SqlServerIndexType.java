/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
