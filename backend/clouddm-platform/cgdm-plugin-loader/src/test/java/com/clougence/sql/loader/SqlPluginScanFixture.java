/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.loader;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;

@Plugin(name = "SQL plugin scan fixture", display = false)
public class SqlPluginScanFixture implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        // test fixture
    }
}
