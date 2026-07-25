/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.oracle;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.sql.oracle.i18n.OraSqlI18nKeys;

@Plugin(name = "Oracle SQL", display = false)
public class OraSqlPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(OraSqlI18nKeys.class);
        dsPlugin.addGlobalSpi(new OraSqlEngineSpi(dsPlugin.findGlobalService(MetaService.class)));
    }
}
