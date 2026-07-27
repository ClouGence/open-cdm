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
package com.clougence.clouddm.platform.dal.handler.json;

import java.lang.reflect.Field;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.api.console.sqlaudit.SqlAuditRequestDTO;
import com.fasterxml.jackson.core.type.TypeReference;

public class SqlAuditRequestListTypeHandler extends JacksonTypeHandler {

    private static final java.lang.reflect.Type LIST_TYPE = new TypeReference<List<SqlAuditRequestDTO>>() {}.getType();

    public SqlAuditRequestListTypeHandler(Class<?> type){
        super(type);
        this.genericType = LIST_TYPE;
    }

    public SqlAuditRequestListTypeHandler(Class<?> type, Field field){
        super(type, field);
    }
}
