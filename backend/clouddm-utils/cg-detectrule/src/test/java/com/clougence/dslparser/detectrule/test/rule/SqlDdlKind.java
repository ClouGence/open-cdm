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
package com.clougence.dslparser.detectrule.test.rule;

import com.clougence.detectrule.lang.EnumAccess;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public enum SqlDdlKind implements EnumAccess<SqlDdlKind> {

    Create("c"),
    Alter("a"),
    Drop("d"),
    Rename("r");

    private final String code;

    SqlDdlKind(String code){
        this.code = code;
    }

    @Override
    public String codeName() {
        return this.code;
    }

    @Override
    public SqlDdlKind valueOfCode(String codeString) {
        if (StringUtils.equalsIgnoreCase(codeString, "c")) {
            return Create;
        } else if (StringUtils.equalsIgnoreCase(codeString, "a")) {
            return Alter;
        } else if (StringUtils.equalsIgnoreCase(codeString, "d")) {
            return Drop;
        } else if (StringUtils.equalsIgnoreCase(codeString, "r")) {
            return Rename;
        } else {
            return null;
        }
    }
}
