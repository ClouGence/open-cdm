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
package com.clougence.clouddm.ds.gauss.i18n.gs;

import com.clougence.clouddm.dsfamily.postgres.i18n.PgDsI18nKeys;
import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/gs-ui-editor-table")
public interface GsDsI18nKeys extends PgDsI18nKeys {

    //
    String EDITOR_TABLEINFO_TABLE_ORIENTATION_TITLE  = "UI_EDITOR_TABLEINFO_TABLE_ORIENTATION_TITLE";
    String EDITOR_TABLEINFO_TABLE_ORIENTATION_DESC   = "UI_EDITOR_TABLEINFO_TABLE_ORIENTATION_DESC";
    String EDITOR_TABLEINFO_TABLE_STORAGE_TYPE_TITLE = "UI_EDITOR_TABLEINFO_TABLE_STORAGE_TYPE_TITLE";
    String EDITOR_TABLEINFO_TABLE_STORAGE_TYPE_DESC  = "UI_EDITOR_TABLEINFO_TABLE_STORAGE_TYPE_DESC";
    String EDITOR_TABLEINFO_ORIENTATION_DEFAULT      = "UI_EDITOR_TABLEINFO_ORIENTATION_DEFAULT";
    String EDITOR_TABLEINFO_ORIENTATION_ROW          = "UI_EDITOR_TABLEINFO_ORIENTATION_ROW";
    String EDITOR_TABLEINFO_ORIENTATION_COLUMN       = "UI_EDITOR_TABLEINFO_ORIENTATION_COLUMN";
    String EDITOR_TABLEINFO_STORAGE_DEFAULT          = "UI_EDITOR_TABLEINFO_STORAGE_DEFAULT";
    String EDITOR_TABLEINFO_STORAGE_ASTORE           = "UI_EDITOR_TABLEINFO_STORAGE_ASTORE";
    String EDITOR_TABLEINFO_STORAGE_USTORE           = "UI_EDITOR_TABLEINFO_STORAGE_USTORE";

    //
    String EDITOR_INDEXES_TYPE_UBTREE_LABEL          = "UI_EDITOR_INDEXES_TYPE_UBTREE_LABEL";
}
