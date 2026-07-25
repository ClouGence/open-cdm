/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.sqlserver;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.sql.sqlserver.i18n.MsSqlSqlI18nKeys;

@Plugin(name = "MS T-SQL", display = false)
public class MsSqlSqlPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(MsSqlSqlI18nKeys.class);
        dsPlugin.addGlobalSpi(new MsSqlSqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
    }
}
