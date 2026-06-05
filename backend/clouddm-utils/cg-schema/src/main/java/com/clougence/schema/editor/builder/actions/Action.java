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
package com.clougence.schema.editor.builder.actions;

import java.util.List;

import lombok.Getter;

/**
 * @author mode 2021/6/8 19:56
 */
@Getter
public class Action {

    private final List<String> sqlString;
    private final String       catalog;
    private final String       schema;
    private final String       table;

    public Action(List<String> sqlString, String catalog, String schema, String table){
        this.sqlString = sqlString;
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
    }

    public final WarnLevel getWarnLevel() { return WarnLevel.Normal; }
}
