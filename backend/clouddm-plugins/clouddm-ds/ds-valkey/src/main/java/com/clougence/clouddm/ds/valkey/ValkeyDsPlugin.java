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
package com.clougence.clouddm.ds.valkey;

import com.clougence.adapter.redis.RedisTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.valkey.definition.auth.ValkeyAuthInfoSpi;
import com.clougence.clouddm.ds.valkey.definition.secrules.ValkeySecRulesSupportSpi;
import com.clougence.clouddm.ds.valkey.definition.ui.browser.ValkeyDsBrowseSpi;
import com.clougence.clouddm.ds.valkey.definition.ui.exception.ValkeyDetermineExceptionSpi;
import com.clougence.clouddm.ds.valkey.definition.ui.template.ValkeyCmdTemplateSpi;
import com.clougence.clouddm.ds.valkey.dialect.ValkeyDialect;
import com.clougence.clouddm.ds.valkey.dsconf.ValkeyConfigSpi;
import com.clougence.clouddm.ds.valkey.dsconf.ValkeySerializationSpi;
import com.clougence.clouddm.ds.valkey.execute.ValkeySessionFactory;
import com.clougence.clouddm.ds.valkey.execute.ValkeySessionSpi;
import com.clougence.clouddm.ds.valkey.execute.ValkeySupportSpi;
import com.clougence.clouddm.ds.valkey.i18n.ValkeyConfigI18nKeys;
import com.clougence.clouddm.ds.valkey.i18n.ValkeyDsI18nKeys;
import com.clougence.clouddm.ds.valkey.language.ValkeyLanguageSpi;
import com.clougence.clouddm.ds.valkey.resource.ValkeyEditorResourceSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/**
 * Valkey datasource plugin based on the Valkey GLIDE Java SDK: the protocol is fully Redis
 * compatible and schema types reuse RedisTypes (mounted under DsType.Valkey).
 * When CONFIG is unavailable, selectSchemas falls back to probing the real database count with SELECT.
 */
@Plugin(name = "i18n::" + ValkeyDsI18nKeys.PLUGIN_NAME_VALKEY,            //
        includePackages = { "com.clougence.clouddm.dsfamily.execute.*", //
                            "com.clougence.clouddm.ds.valkey.execute.*" //
        }, dsProduct = DataSourceType.Valkey)
public class ValkeyDsPlugin implements DsPlugin, SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Valkey);
        binder.bindTypes(DsType.Valkey, RedisTypes.values(), RedisTypes::valueOfCode);
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        // init schema plugin
        SchemaFramework.install(this);

        this.configBasic(dsPlugin);
        this.configExecute(dsPlugin);
        this.configUi(dsPlugin);
        this.configEditor(dsPlugin);
        this.configTeam(dsPlugin);
        this.configFeature(dsPlugin);
    }

    private void configBasic(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new ValkeyConfigSpi());
        dsPlugin.addPluginSpi(new ValkeySerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(ValkeySessionFactory.class);
        dsPlugin.bindDsDriverFamily("Valkey GLIDE");
        dsPlugin.bindSqlEngine("Redis Commands");

        dsPlugin.addPluginSpi(new ValkeySessionSpi());
        dsPlugin.addPluginSpi(new ValkeySupportSpi());
        dsPlugin.addGlobalSpi(new ValkeyAuthInfoSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(ValkeyDsI18nKeys.class);
        dsPlugin.bindPluginI18n(ValkeyConfigI18nKeys.class);
        dsPlugin.bindDsDialect(ValkeyDialect.INSTANCE);

        // SPIs
        dsPlugin.addPluginSpi(new ValkeyDsBrowseSpi());
        dsPlugin.addPluginSpi(new ValkeyCmdTemplateSpi());
        dsPlugin.addPluginSpi(new ValkeyDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new ValkeyLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ValkeyEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new ValkeySecRulesSupportSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        // no feature
    }
}
