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

package com.clougence.clouddm.ds.greptimedb.metadata;

import java.util.List;

import com.clougence.clouddm.ds.greptimedb.i18n.GreptimeConfigI18nKeys;
import com.clougence.clouddm.sdk.ui.template.CmdExample;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class GreptimeCmdTemplateSpi implements CmdTemplateSpi {

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        return "SELECT 1;";
    }

    @Override
    public List<CmdExample> getExamples() {
        return List.of(
            new CmdExample(GreptimeConfigI18nKeys.GREPTIMEDB_EXAMPLE_CREATE_DATABASE, "CREATE DATABASE IF NOT EXISTS demo;"),
            new CmdExample(GreptimeConfigI18nKeys.GREPTIMEDB_EXAMPLE_CREATE_TABLE,
                "CREATE TABLE demo.metrics (ts TIMESTAMP(3) TIME INDEX, host STRING, reading DOUBLE, PRIMARY KEY (host));"),
            new CmdExample(GreptimeConfigI18nKeys.GREPTIMEDB_EXAMPLE_INSERT,
                "INSERT INTO demo.metrics (ts, host, reading) VALUES ('2026-01-01 00:00:00', 'host1', 21.5);"),
            new CmdExample(GreptimeConfigI18nKeys.GREPTIMEDB_EXAMPLE_DELETE,
                "DELETE FROM demo.metrics WHERE host = 'host1' AND ts = '2026-01-01 00:00:00';"),
            new CmdExample(GreptimeConfigI18nKeys.GREPTIMEDB_EXAMPLE_ADD_COLUMN, "ALTER TABLE demo.metrics ADD COLUMN humidity DOUBLE;"),
            new CmdExample(GreptimeConfigI18nKeys.GREPTIMEDB_EXAMPLE_DROP_COLUMN, "ALTER TABLE demo.metrics DROP COLUMN humidity;"),
            new CmdExample(GreptimeConfigI18nKeys.GREPTIMEDB_EXAMPLE_DROP_TABLE, "DROP TABLE IF EXISTS demo.metrics;"),
            new CmdExample(GreptimeConfigI18nKeys.GREPTIMEDB_EXAMPLE_DROP_VIEW, "DROP VIEW IF EXISTS demo.metrics_view;"),
            new CmdExample(GreptimeConfigI18nKeys.GREPTIMEDB_EXAMPLE_DROP_DATABASE, "DROP DATABASE IF EXISTS demo;"));
    }
}
