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
package com.clougence.clouddm.ds.tidb.sql;

import com.clougence.clouddm.ds.tidb.i18n.TiSqlI18nKeys;
import com.clougence.clouddm.ds.tidb.sql.analysis.sysobj.TiDBSysObjectRegistrySpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;

@Plugin(name = "i18n::" + TiSqlI18nKeys.SQL_ENGINE_TIDB_SQL, display = false)
public class TiSqlPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(TiSqlI18nKeys.class);
        dsPlugin.addGlobalSpi(new TiSqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addGlobalSpi(new TiDBSysObjectRegistrySpi());
    }
}
