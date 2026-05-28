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
package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070019__file_src extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table rdp_ds_kv_base_config add column `conf_val_type` varchar(64) NOT NULL DEFAULT "TEXT"
                """, """
                    alter table rdp_data_source add column `info_fetch_type` varchar(64) DEFAULT NULL
                """, """
                    alter table rdp_data_source_history add column `info_fetch_type` varchar(64) DEFAULT NULL
                """);
    }
}
