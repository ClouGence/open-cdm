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
package com.clougence.clouddm.ds.redshift;

import com.clougence.adapter.postgre.PostgreSchemaPlugin;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsFeatureIDs;
import com.clougence.clouddm.ds.redshift.execute.RedshiftSessionFactory;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PostgresCmdTemplateSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.UiPluginBinder;
import com.clougence.drivers.factory.def.DriverDescription;
import com.clougence.reactor.mappings.postgres_src.Postgres2PostgresSchemaPlugin;
import com.clougence.schema.SchemaFramework;

/**
 * @author bucketli 2021/4/25 15:13
 */
@Plugin(dsProduct = DataSourceType.Redshift)
public class RedshiftDsPlugin implements DsPlugin, DsFeatureIDs {

    @Override
    public void loadPlugin(ClassLoader useLoader) {
        // init schema plugin
        SchemaFramework.install(new PostgreSchemaPlugin());
        SchemaFramework.install(new Postgres2PostgresSchemaPlugin());
    }

    @Override
    public void initDsPlugin(DsPluginBinder binder, ClassLoader useLoader, DriverDescription useDriver) {
        // execute
        binder.bindDriverFamily("Redshift JDBC");
        binder.bindDriverFamily("PostgreSQL JDBC");
        binder.bindSessionFactory(new RedshiftSessionFactory(), new RdbSessionSpi());
    }

    @Override
    public void initUiPlugin(UiPluginBinder binder, ClassLoader useLoader) {
        // UI Definition
        //binder.bindDefService(new RedshiftDefService());

        // SPIs
        //binder.addTableEditorUiDataSpi(new PostgresTableEditorUiDataSpi());
        binder.addCmdTemplateSpi(new PostgresCmdTemplateSpi());

        this.configFeatures(binder);
    }

    private void configFeatures(UiPluginBinder binder) {
        binder.addFeature(MENU_SCHEMAS_DATABASE_CREATE, MENU_SCHEMAS_DATABASE_DROP, MENU_SCHEMAS_SCHEMA_CREATE, MENU_SCHEMAS_SCHEMA_DROP, //
                /*MENU_SCHEMAS_TABLE_CREATE, MENU_SCHEMAS_TABLE_ALTER,*/ MENU_SCHEMAS_TABLE_DELETE, MENU_SCHEMAS_TABLE_TRUNCATE);
    }
}
