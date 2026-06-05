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

import lombok.Getter;

@Getter
public enum SqlServerConstraintType {

    PrimaryKey("PK", "PRIMARY KEY"),
    Unique("UQ", "UNIQUE"),
    ForeignKey("F", "FOREIGN KEY"),;

    private final String typeName;

    private final String fullName;

    SqlServerConstraintType(String typeName, String fullName){
        this.typeName = typeName;
        this.fullName = fullName;
    }

    public static SqlServerConstraintType valueOfCode(String code) {
        for (SqlServerConstraintType constraintType : SqlServerConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }

    public static SqlServerConstraintType valueOfFullCode(String code) {
        for (SqlServerConstraintType constraintType : SqlServerConstraintType.values()) {
            if (constraintType.fullName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
