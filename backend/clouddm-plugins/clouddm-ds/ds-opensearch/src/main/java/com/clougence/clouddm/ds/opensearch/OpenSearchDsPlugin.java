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
package com.clougence.clouddm.ds.opensearch;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfigSpi;
import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchSerializationSpi;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(name = "i18n::" + OpenSearchDsI18nKeys.PLUGIN_NAME_OPENSEARCH, display = false,
        includePackages = { "com.clougence.clouddm.ds.opensearch.execute.*", "org.apache.hc.*" }, dsProduct = DataSourceType.OpenSearch)
public class OpenSearchDsPlugin implements DsPlugin, SchemaPlugin {
    public static final String DRIVER_FAMILY = "OpenSearch HTTP Client";

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.OpenSearch);
    }

    @Override
    public void loadPlugin(DsPluginBinder binder) {
        SchemaFramework.install(this);
        binder.bindDsDriverFamily(DRIVER_FAMILY);
        binder.bindPluginI18n(OpenSearchDsI18nKeys.class);
        binder.addPluginSpi(new OpenSearchConfigSpi());
        binder.addPluginSpi(new OpenSearchSerializationSpi(binder.getPluginClassLoader()));
    }
}
