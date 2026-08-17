/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.mysql;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.sql.mysql.execute.explain.MyExplainPlanSpi;
import com.clougence.sql.mysql.analysis.sysobj.MySysObjectRegistrySpi;
import com.clougence.sql.mysql.i18n.MySqlI18nKeys;

@Plugin(name = "MySQL SQL", display = false)
public class MySqlPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(MySqlI18nKeys.class);
        dsPlugin.addGlobalSpi(new MySqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addGlobalSpi(new MyExplainPlanSpi());
        dsPlugin.addGlobalSpi(new MySysObjectRegistrySpi());
    }
}
