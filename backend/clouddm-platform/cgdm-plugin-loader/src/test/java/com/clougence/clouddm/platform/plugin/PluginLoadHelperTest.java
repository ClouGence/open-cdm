/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.platform.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
        File pluginJar = temporaryFolder.newFile("sql-plugin.jar");
        writeSqlPlugin(pluginJar);

        Set<String> pluginClasses;
        try (JarResourceLoader resourceLoader = new JarResourceLoader(pluginJar)) {
            pluginClasses = PluginLoadHelper.scanPluginClasses(resourceLoader);
        }

        assertTrue(pluginClasses.contains(SqlPluginScanFixture.class.getName()));
    }

    @Test
    public void shouldLoadOnlyTheSelectedPluginFile() throws Exception {
        File directory = temporaryFolder.newFolder("selected-plugin");
        writeSqlPlugin(new File(directory, "selected.jar"));
        ClassLoader loader = getClass().getClassLoader();
        assertEquals(0, PluginLoadHelper.loadPlugins(loader, "missing.jar", directory));
        assertEquals(1, PluginLoadHelper.loadPlugins(loader, "selected.jar", directory));
    }

    private void writeSqlPlugin(File file) throws IOException {
        String classResource = SqlPluginScanFixture.class.getName().replace('.', '/') + ".class";
        try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(file.toPath()));
                InputStream input = SqlPluginScanFixture.class.getClassLoader().getResourceAsStream(classResource)) {
            output.putNextEntry(new JarEntry(classResource));
            input.transferTo(output);
            output.closeEntry();
        }
    }

}
