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
package com.clougence.sql.postgres;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.sql.postgres.analysis.sysobj.PgSysObjectRegistrySpi;
import com.clougence.sql.postgres.i18n.PgSqlI18nKeys;

@Plugin(name = "PostgreSQL SQL", display = false)
public class PgSqlPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(PgSqlI18nKeys.class);
        dsPlugin.addGlobalSpi(new PgSqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addGlobalSpi(new PgSysObjectRegistrySpi());
    }
}
