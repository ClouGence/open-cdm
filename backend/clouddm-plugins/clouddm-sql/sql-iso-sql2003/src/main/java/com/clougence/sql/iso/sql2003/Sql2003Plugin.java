/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.iso.sql2003;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.sql.iso.sql2003.i18n.Sql2003I18nKeys;

@Plugin(name = "ISO SQL-2003", display = false)
public class Sql2003Plugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(Sql2003I18nKeys.class);
        dsPlugin.addGlobalSpi(new Sql2003SqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
    }
}
