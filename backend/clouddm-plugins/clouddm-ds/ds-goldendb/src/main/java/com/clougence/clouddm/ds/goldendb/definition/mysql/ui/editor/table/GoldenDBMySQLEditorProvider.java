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
package com.clougence.clouddm.ds.goldendb.definition.mysql.ui.editor.table;

import java.util.List;

import com.clougence.clouddm.ds.goldendb.dialect.mysql.GoldenDBMySQLDialect;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyEditorProvider;
import com.clougence.schema.DsType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.editor.triggers.TriggerContext;

public class GoldenDBMySQLEditorProvider extends MyEditorProvider {

    public static final SqlBuilder INSTANCE = new GoldenDBMySQLEditorProvider();

    @Override
    public DsType getDataSourceType() { return DsType.GoldenDB; }

    @Override
    public Dialect getDialect() { return GoldenDBMySQLDialect.INSTANCE; }

    @Override
    public List<String> tableCreate(TriggerContext buildContext, String catalog, String schema, String table, ETable eTable) {
        return new GoldenDBMySQLCreateUtils().buildCreate(buildContext, catalog, schema, table, eTable);
    }
}
