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

    //0 = Heap
    Heap("0"),
    //1 = B-tree (clustered rowstore)
    Clustered("1"),
    //2 = Nonclustered rowstore (B-tree)
    NonClustered("2"),
    //3 = XML
    Xml("3"),
    //4 = Spatial
    Spatial("4"),
    //5 = Clustered columnstore index. Applies to: SQL Server 2014 (12.x) and later.
    ClusteredColumnStore("5"),
    //6 = Nonclustered columnstore index. Applies to: SQL Server 2012 (11.x) and later.
    NonClusteredColumnStore("6"),
    //7 = Nonclustered hash index. Applies to: SQL Server 2014 (12.x) and later.
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
