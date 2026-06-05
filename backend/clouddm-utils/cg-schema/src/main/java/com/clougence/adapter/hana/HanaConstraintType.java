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
package com.clougence.adapter.hana;

public enum HanaConstraintType {

    /** pk */
    PRIMARY_KEY("PRIMARY KEY"),
    /** uk */
    UNIQUE("UNIQUE"),
    NOT_NULL_UNIQUE("NOT NULL UNIQUE"),
    /** fk */
    FOREIGN_KEY("FOREIGN KEY"),
    /** Specifies a predicate to use as a table check constraint.
     * A table check constraint is satisfied if <search_condition> evaluates to true.
     */
    CHECK_CONSTRAINT("CHECK CONSTRAINT"),

    ;

    private final String typeName;

    HanaConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static HanaConstraintType valueOfCode(String code) {
        for (HanaConstraintType constraintType : HanaConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }

}
