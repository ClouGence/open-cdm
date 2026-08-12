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
package com.clougence.clouddm.ds.valkey.definition.ui.editor.keys;

import com.clougence.clouddm.ds.valkey.dialect.ValkeyDialect;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.clouddm.dsfamily.schema.sqlbuilder.AbstractSqlBuilder;
import com.clougence.schema.editor.provider.SqlBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Betacat0
*/
@Slf4j
public class ValkeyEditorProvider extends AbstractSqlBuilder implements SqlBuilder {

    public static final SqlBuilder INSTANCE = new ValkeyEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.Valkey; }

    public Dialect getDialect() { return ValkeyDialect.INSTANCE; }

}
