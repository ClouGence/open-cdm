/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.mongodb;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.sql.mongodb.analysis.sysobj.MongoSysObjectRegistrySpi;
import com.clougence.sql.mongodb.i18n.MongoSqlI18nKeys;

@Plugin(name = "MongoDB DSL", display = false)
public class MongoSqlPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(MongoSqlI18nKeys.class);
        dsPlugin.addGlobalSpi(new MongoSqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addGlobalSpi(new MongoSysObjectRegistrySpi());
    }
}
