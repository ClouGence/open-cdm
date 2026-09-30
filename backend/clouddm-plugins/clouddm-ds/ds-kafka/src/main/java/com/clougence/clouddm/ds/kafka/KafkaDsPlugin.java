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

import com.clougence.adapter.kafka.KafkaTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.kafka.definition.secrules.KafkaSecRulesSupportSpi;
import com.clougence.clouddm.ds.kafka.definition.ui.browser.KafkaDsBrowseSpi;
import com.clougence.clouddm.ds.kafka.definition.ui.template.KafkaCmdTemplateSpi;
import com.clougence.clouddm.ds.kafka.dialect.KafkaDialect;
import com.clougence.clouddm.ds.kafka.dsconf.KafkaConfigSpi;
import com.clougence.clouddm.ds.kafka.dsconf.KafkaSerializationSpi;
import com.clougence.clouddm.ds.kafka.execute.KafkaOpsSpiImpl;
import com.clougence.clouddm.ds.kafka.execute.KafkaSessionFactory;
import com.clougence.clouddm.ds.kafka.execute.KafkaSessionSpi;
import com.clougence.clouddm.ds.kafka.execute.KafkaSupportSpi;
import com.clougence.clouddm.ds.kafka.i18n.KafkaConfigI18nKeys;
import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.ds.kafka.language.KafkaLanguageSpi;
import com.clougence.clouddm.ds.kafka.resource.KafkaEditorResourceSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(name = "i18n::" + KafkaDsI18nKeys.PLUGIN_NAME_KAFKA,            //
        includePackages = { "com.clougence.clouddm.dsfamily.execute.*", //
                            "com.clougence.clouddm.ds.kafka.execute.*"  //
        }, dsProduct = DataSourceType.Kafka)
public class KafkaDsPlugin implements DsPlugin, SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Kafka);
        binder.bindTypes(DsType.Kafka, KafkaTypes.values(), KafkaTypes::valueOfCode);
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SchemaFramework.install(this);

        this.configBasic(dsPlugin);
        this.configExecute(dsPlugin);
        this.configUi(dsPlugin);
        this.configEditor(dsPlugin);
        this.configTeam(dsPlugin);
    }

    private void configBasic(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new KafkaConfigSpi());
        dsPlugin.addPluginSpi(new KafkaSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(KafkaSessionFactory.class);
        dsPlugin.bindDsDriverFamily("Kafka Clients");
        // no bindSqlEngine: object tree is the primary interaction in milestone-1

        dsPlugin.addPluginSpi(new KafkaSessionSpi());
        dsPlugin.addPluginSpi(new KafkaSupportSpi());
        dsPlugin.addPluginSpi(new KafkaOpsSpiImpl());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        dsPlugin.bindPluginI18n(KafkaDsI18nKeys.class);
        dsPlugin.bindPluginI18n(KafkaConfigI18nKeys.class);
        dsPlugin.bindDsDialect(KafkaDialect.INSTANCE);
        dsPlugin.addPluginSpi(new KafkaDsBrowseSpi());
        dsPlugin.addPluginSpi(new KafkaCmdTemplateSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new KafkaLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new KafkaEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new KafkaSecRulesSupportSpi());
    }
}
