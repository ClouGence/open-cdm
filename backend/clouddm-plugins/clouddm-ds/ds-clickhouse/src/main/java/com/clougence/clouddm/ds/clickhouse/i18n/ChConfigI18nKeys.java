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

import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/ch-config")
public interface ChConfigI18nKeys extends ConfigI18nKey {

    String CONFIG_CLICKHOUSE_SESSION_TIME_OUT       = "CONFIG_CLICKHOUSE_SESSION_TIME_OUT";
    String CONFIG_CLICKHOUSE_SESSION_TIME_OUT_LABEL = "CONFIG_CLICKHOUSE_SESSION_TIME_OUT_LABEL";
    String CONFIG_CLICKHOUSE_SESSION_TIME_OUT_DESC  = "CONFIG_CLICKHOUSE_SESSION_TIME_OUT_DESC";
}
