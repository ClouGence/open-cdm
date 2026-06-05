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
package com.clougence.clouddm.ds.clickhouse.i18n;

import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.utils.i18n.I18nResource;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
@I18nResource("/META-INF/clougence/i18n/ch-ui-editor-table")
public interface ChDsI18nKeys extends DsDataEditorI18nKeys, DsTableEditorI18nKeys {

    String REWRITE_LIMIT_LABEL = "REWRITE_LIMIT_LABEL";
}
