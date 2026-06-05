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
package com.clougence.schema.umi.special.rdb;

public enum RdbParamMode {

    IN("IN"),
    OUT("OUT"),
    INOUT("INOUT"),
    // for pg;
    VARIADIC("VARIADIC"),
    // for DB2
    ResultBeforeCasting("R"),
    ResultAfterCasting("C"),
    AggregationStateVar("S"),;

    private final String modeName;

    RdbParamMode(String modeName){
        this.modeName = modeName;
    }

    public String getModeName() { return this.modeName; }

    public static RdbParamMode valueOfCode(String code) {
        for (RdbParamMode paramMode : RdbParamMode.values()) {
            if (paramMode.modeName.equals(code)) {
                return paramMode;
            }
        }
        return null;
    }
}
