/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.platform.plugin;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.clougence.sql.loader.SqlPluginScanFixture;
import com.clougence.utils.loader.providers.JarResourceLoader;

public class PluginLoadHelperTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldDiscoverSqlPluginsOutsideClouddmPackage() throws Exception {
        String classResource = SqlPluginScanFixture.class.getName().replace('.', '/') + ".class";
        File pluginJar = temporaryFolder.newFile("sql-plugin.jar");
        try (JarOutputStream output = new JarOutputStream(java.nio.file.Files.newOutputStream(pluginJar.toPath()));
                InputStream input = SqlPluginScanFixture.class.getClassLoader().getResourceAsStream(classResource)) {
            output.putNextEntry(new JarEntry(classResource));
            input.transferTo(output);
            output.closeEntry();
        }

        Set<String> pluginClasses;
        try (JarResourceLoader resourceLoader = new JarResourceLoader(pluginJar)) {
            pluginClasses = PluginLoadHelper.scanPluginClasses(resourceLoader);
        }

        assertTrue(pluginClasses.contains(SqlPluginScanFixture.class.getName()));
    }
}
