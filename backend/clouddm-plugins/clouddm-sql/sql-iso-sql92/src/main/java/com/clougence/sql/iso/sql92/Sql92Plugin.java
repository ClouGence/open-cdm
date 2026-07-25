/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql92;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.sql.iso.sql92.i18n.Sql92I18nKeys;

@Plugin(name = "ISO SQL-92", display = false)
public class Sql92Plugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(Sql92I18nKeys.class);
        dsPlugin.addGlobalSpi(new Sql92SqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
    }
}
