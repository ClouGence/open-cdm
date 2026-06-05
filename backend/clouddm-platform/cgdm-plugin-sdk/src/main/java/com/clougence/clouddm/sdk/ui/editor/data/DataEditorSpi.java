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
package com.clougence.clouddm.sdk.ui.editor.data;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.utils.i18n.I18nUtils;

public interface DataEditorSpi extends Spi {

    String buildInsert(RdbTable tableMeta, Map<String, String> recordData);

    String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData);

    String buildDelete(RdbTable tableMeta, Map<String, String> whereData);

    String buildSelect(RdbTable tableMeta, String condition, String orderBy, Integer offset, Integer limit);

    String buildSelectCount(RdbTable tableMeta, String condition);

    void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils);
}
