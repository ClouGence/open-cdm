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
package com.clougence.adapter.postgre;

import com.clougence.schema.metadata.CgTableType;
import com.clougence.schema.metadata.TableType;
import com.clougence.utils.StringUtils;

/**
 * Postgres 的表类型
 * @version : 2021-05-10
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PostgresTableType implements TableType {

    Table("BASE TABLE", CgTableType.BASE_TABLE),
    View("VIEW", CgTableType.VIEW),
    ForeignTable("FOREIGN", null),
    LocalTemporary("LOCAL TEMPORARY", null),
    Materialized("MATERIALIZED VIEW", null),;

    private final String typeName;

    private final CgTableType cgTableType;

    PostgresTableType(String typeName, CgTableType cgTableType){
        this.typeName = typeName;
        this.cgTableType = cgTableType;
    }

    public String getTypeName() { return this.typeName; }

    public CgTableType getCgTableType() { return cgTableType; }

    public static PostgresTableType valueOfCgTableType(CgTableType cgTableType) {
        if (cgTableType == null) {
            return Table;
        }
        for (PostgresTableType tableType : PostgresTableType.values()) {
            if (tableType.cgTableType == cgTableType) {
                return tableType;
            }
        }
        return Table;
    }

    public static PostgresTableType valueOfCode(String code) {
        if (StringUtils.equalsIgnoreCase("r", code) || StringUtils.equalsIgnoreCase("p", code)) {
            return Table;
        } else if (StringUtils.equalsIgnoreCase("v", code)) {
            return View;
        } else if (StringUtils.equalsIgnoreCase("f", code)) {
            return ForeignTable;
        }

        for (PostgresTableType tableType : PostgresTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
