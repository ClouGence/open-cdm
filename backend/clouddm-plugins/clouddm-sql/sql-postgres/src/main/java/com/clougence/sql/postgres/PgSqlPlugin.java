/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
        dsPlugin.addGlobalSpi(new PgSysObjectRegistrySpi());
        dsPlugin.addGlobalSpi(new PgSqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
    }
}
