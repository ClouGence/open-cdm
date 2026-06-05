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
package com.clougence.adapter.tidb;

import com.clougence.schema.metadata.TableType;

/**
 * TiDB table type
 * @version : 2021-09-27
 * @author mode
 */
public enum TiDBTableType implements TableType {

    Table("BASE TABLE"),
    View("VIEW"),
    SystemView("SYSTEM VIEW");

    private final String typeName;

    TiDBTableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static TiDBTableType valueOfCode(String code) {
        for (TiDBTableType tableType : TiDBTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported tidb tableType " + code);
    }
}
