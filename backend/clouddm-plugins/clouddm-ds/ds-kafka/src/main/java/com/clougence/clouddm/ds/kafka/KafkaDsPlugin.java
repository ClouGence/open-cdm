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
package com.clougence.clouddm.ds.kafka;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.kafka.definition.auth.KafkaAuthInfoSpi;
import com.clougence.clouddm.ds.kafka.definition.ui.KafkaDsBrowseSpi;
import com.clougence.clouddm.ds.kafka.dsconf.KafkaConfigSpi;
import com.clougence.clouddm.ds.kafka.dsconf.KafkaSerializationSpi;
import com.clougence.clouddm.ds.kafka.execute.KafkaSessionFactory;
import com.clougence.clouddm.ds.kafka.execute.KafkaSessionSpi;
import com.clougence.clouddm.ds.kafka.execute.KafkaSupportSpi;
import com.clougence.clouddm.ds.kafka.i18n.KafkaConfigI18nKeys;
import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.ds.kafka.language.KafkaLanguageSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;
import com.clougence.sql.kafka.KafkaSqlEngineSpi;

@Plugin(name = "i18n::" + KafkaDsI18nKeys.PLUGIN_NAME_KAFKA, includePackages = { "com.clougence.clouddm.ds.kafka.execute.*" }, dsProduct = DataSourceType.Kafka)
public class KafkaDsPlugin implements DsPlugin, SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Kafka);
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SchemaFramework.install(this);
        dsPlugin.bindDsDriverFamily("Kafka");
        dsPlugin.bindDsSessionFactory(KafkaSessionFactory.class);
        dsPlugin.addPluginSpi(new KafkaSessionSpi());
        dsPlugin.addPluginSpi(new KafkaSupportSpi());
        dsPlugin.addPluginSpi(new KafkaDsBrowseSpi());
        dsPlugin.addGlobalSpi(new KafkaAuthInfoSpi());
        dsPlugin.bindGlobalI18n(KafkaDsI18nKeys.class);
        dsPlugin.bindSqlEngine(KafkaSqlEngineSpi.NAME);
        dsPlugin.addPluginSpi(new KafkaLanguageSpi());
        dsPlugin.bindPluginI18n(KafkaDsI18nKeys.class, KafkaConfigI18nKeys.class);
        dsPlugin.addPluginSpi(new KafkaConfigSpi());
        dsPlugin.addPluginSpi(new KafkaSerializationSpi(dsPlugin.getPluginClassLoader()));
    }
}
