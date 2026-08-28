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
package com.clougence.clouddm.ds.goldendb.definition.mysql;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.goldendb.definition.mysql.ui.editor.table.GoldenDBMySQLTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.MyDefService;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

public class GoldenDBMySQLDefService extends MyDefService {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection connection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new GoldenDBMySQLTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, connection);
    }
}
