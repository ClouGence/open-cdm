/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.dameng.sql;

import com.clougence.clouddm.ds.dameng.i18n.DmSqlI18nKeys;
import com.clougence.clouddm.ds.dameng.sql.analysis.sysobj.DmSysObjectRegistrySpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;

@Plugin(name = "i18n::" + DmSqlI18nKeys.SQL_ENGINE_DAMENG_SQL, display = false)
public class DmSqlPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(DmSqlI18nKeys.class);
        dsPlugin.addGlobalSpi(new DmSqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addGlobalSpi(new DmSysObjectRegistrySpi());
    }
}
