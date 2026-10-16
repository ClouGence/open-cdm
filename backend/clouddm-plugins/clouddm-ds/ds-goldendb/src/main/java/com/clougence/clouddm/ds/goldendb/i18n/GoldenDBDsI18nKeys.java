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
package com.clougence.clouddm.ds.goldendb.i18n;

import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/goldendb-ui-editor-table")
public interface GoldenDBDsI18nKeys extends MyDsI18nKeys {

    String PLUGIN_NAME_GOLDENDB_MYSQL           = "PLUGIN_NAME_GOLDENDB_MYSQL";
    String PLUGIN_NAME_GOLDENDB_ORACLE          = "PLUGIN_NAME_GOLDENDB_ORACLE";
    String EDITOR_DISTRIBUTION_TYPE_TITLE       = "EDITOR_DISTRIBUTION_TYPE_TITLE";
    String EDITOR_DISTRIBUTION_TYPE_DESC        = "EDITOR_DISTRIBUTION_TYPE_DESC";
    String EDITOR_DISTRIBUTION_COLUMNS_TITLE    = "EDITOR_DISTRIBUTION_COLUMNS_TITLE";
    String EDITOR_DISTRIBUTION_COLUMNS_DESC     = "EDITOR_DISTRIBUTION_COLUMNS_DESC";
    String EDITOR_DISTRIBUTION_EXPRESSION_TITLE = "EDITOR_DISTRIBUTION_EXPRESSION_TITLE";
    String EDITOR_DISTRIBUTION_EXPRESSION_DESC  = "EDITOR_DISTRIBUTION_EXPRESSION_DESC";
    String EDITOR_DISTRIBUTION_GROUPS_TITLE     = "EDITOR_DISTRIBUTION_GROUPS_TITLE";
    String EDITOR_DISTRIBUTION_GROUPS_DESC      = "EDITOR_DISTRIBUTION_GROUPS_DESC";
    String EDITOR_DISTRIBUTION_EMPTY            = "EDITOR_DISTRIBUTION_EMPTY";
    String EDITOR_DISTRIBUTION_HASH             = "EDITOR_DISTRIBUTION_HASH";
    String EDITOR_DISTRIBUTION_RANGE            = "EDITOR_DISTRIBUTION_RANGE";
    String EDITOR_DISTRIBUTION_LIST             = "EDITOR_DISTRIBUTION_LIST";
    String EDITOR_DISTRIBUTION_DUPLICATE        = "EDITOR_DISTRIBUTION_DUPLICATE";
}
