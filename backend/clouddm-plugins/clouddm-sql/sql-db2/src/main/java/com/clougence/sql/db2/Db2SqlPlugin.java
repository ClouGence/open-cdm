/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.db2;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.sql.db2.i18n.Db2SqlI18nKeys;

@Plugin(name = "IBM DB2 SQL", display = false)
public class Db2SqlPlugin implements DsPlugin {
    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(Db2SqlI18nKeys.class);
        dsPlugin.addGlobalSpi(new Db2SqlEngineSpi());
    }
}
