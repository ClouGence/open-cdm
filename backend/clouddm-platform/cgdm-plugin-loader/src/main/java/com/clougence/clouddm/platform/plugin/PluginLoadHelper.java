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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clougence.SchemaInitPlugin;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.plugin.info.*;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.schema.SchemaFramework;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.io.input.AutoCloseInputStream;
import com.clougence.utils.jar.JarFile;
import com.clougence.utils.loader.CgResourceScanner;
import com.clougence.utils.loader.ClassMatcher.ClassInfo;
import com.clougence.utils.loader.ResourceLoader;
import com.clougence.utils.loader.providers.JarResourceLoader;
import com.clougence.utils.loader.providers.MultiResourceLoader;
import com.clougence.utils.loader.providers.PathResourceLoader;
import com.clougence.utils.reflect.Annotation;
import com.clougence.utils.reflect.Annotations;

/**
 * Load external datasource service jar for clouddm
 *
 * @author mode 2021/01/11
 */
public class PluginLoadHelper {

    private static final Logger log             = LoggerFactory.getLogger(PluginLoadHelper.class);
    private static final String DRIVER_XML_PATH = "META-INF/clougence/drivers.xml";

    // ---------------------------------------------
    //                                          Scan
    // ---------------------------------------------

    public static int loadPlugins(ClassLoader appClassLoader, File... pluginPaths) {
        return loadPlugins(appClassLoader, null, pluginPaths);
    }

    public static synchronized int loadPlugins(ClassLoader appClassLoader, String pluginFileName, File... pluginPaths) {
        PluginManager.markStarting();
        List<BaseMeta> allPlugins = new ArrayList<>();
        List<PluginLoadResult> loadResults = new ArrayList<>();
        try {
            for (File path : pluginPaths) {
                allPlugins.addAll(scanPluginDirectory(appClassLoader, path, pluginFileName, loadResults));
            }

            allPlugins.sort(Comparator.comparingInt(BaseMeta::getOrder));

            log.info("Total plugins discovered: {}", allPlugins.size());
            for (BaseMeta plugin : allPlugins) {
                activatePlugin(plugin, loadResults);
            }
            SchemaFramework.install(new SchemaInitPlugin());
        } finally {
            PluginManager.publishPluginLoadResults(loadResults);
        }
        PluginManager.markReady();
        return allPlugins.size();
    }

    private static List<BaseMeta> scanPluginDirectory(ClassLoader appClassLoader, File pluginPath, String pluginFileName, List<PluginLoadResult> loadResults) {
        List<BaseMeta> result = new ArrayList<>();
        if (pluginPath == null || !pluginPath.exists() || !pluginPath.isDirectory()) {
            return result;
        }

        File[] pluginEntries = pluginPath.listFiles(file -> file.exists() && !file.isHidden() && isTargetPlugin(file, pluginFileName));
        if (pluginEntries == null || pluginEntries.length == 0) {
            return Collections.emptyList();
        }

        for (File physicalPlugin : pluginEntries) {
            try {
                ResourceLoader pluginLoader = scanPhysicalPlugin(physicalPlugin, loadResults);
                if (pluginLoader != null) {
                    result.addAll(loadPhysicalPluginFromLoader(appClassLoader, physicalPlugin, pluginLoader, loadResults));
                }
            } catch (IOException | RuntimeException e) {
                recordLoadFailure(loadResults, physicalPlugin, null, null, null, e);
            }
        }

        return result;
    }

    private static boolean isTargetPlugin(File file, String pluginFileName) {
        return StringUtils.isEmpty(pluginFileName) || pluginFileName.equals(file.getName());
    }

    private static ResourceLoader scanPhysicalPlugin(File physicalPlugin, List<PluginLoadResult> loadResults) throws IOException {
        if (physicalPlugin.isDirectory()) {
            return loadPluginsFromDir(physicalPlugin, loadResults);
        } else if (isJarFile(physicalPlugin)) {
            return loadPluginsFromJar(physicalPlugin);
        } else {
            return null;
        }
    }

    private static ResourceLoader loadPluginsFromDir(File physicalPlugin, List<PluginLoadResult> loadResults) {
        MultiResourceLoader pluginLoader = new MultiResourceLoader();
        pluginLoader.addLoader(new PathResourceLoader(physicalPlugin));

        File[] classpathJarFiles = physicalPlugin.listFiles(file -> file.exists() && file.isFile() && isJarFile(file));
        if (classpathJarFiles != null && classpathJarFiles.length > 0) {
            Arrays.sort(classpathJarFiles, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
            for (File classpathJarFile : classpathJarFiles) {
                try {
                    pluginLoader.addLoader(loadPluginsFromJar(classpathJarFile));
                } catch (IOException | RuntimeException e) {
                    recordLoadFailure(loadResults, classpathJarFile, null, null, null, e);
                }
            }
        }

        return pluginLoader;
    }

    private static ResourceLoader loadPluginsFromJar(File jarFile) throws IOException {
        List<String> nestedList = new ArrayList<>();
        try (JarFile jarFileItem = new JarFile(jarFile)) {
            for (JarEntry jarEntry : jarFileItem) {
                String jarEntryName = jarEntry.getName();
                if (StringUtils.endsWith(jarEntryName, ".jar") && !jarEntryName.contains("/")) {
                    nestedList.add(jarEntryName);
                }
            }
        }
        return new JarResourceLoader(jarFile, nestedList);
    }

    private static boolean isJarFile(File file) {
        return file != null && file.isFile() && StringUtils.endsWithIgnoreCase(file.getName(), "jar");
    }

    // ---------------------------------------------
    //                          Load Physical Plugin
    // ---------------------------------------------

    private static List<BaseMeta> loadPhysicalPluginFromLoader(ClassLoader appClassLoader, File physicalPlugin, ResourceLoader pluginLoader,
                                                               List<PluginLoadResult> loadResults) throws IOException {
        LoadDef loadDef = new LoadDef(appClassLoader, pluginLoader, physicalPlugin);

        // load driver SPI
        try {
            PluginManager.driverLoader().loadDsFactory(loadDef.pluginLoader);
        } catch (Throwable e) {
            log.error("load plugin drivers failed, {}", e.getMessage(), e);
        }

        // load drivers.xml
        try {
            for (InputStream driverXmlInput : loadDef.pluginResource.getResourcesAsStream(DRIVER_XML_PATH)) {
                try {
                    if (driverXmlInput != null) {
                        PluginManager.driverLoader().loadDriverXml(driverXmlInput);
                    }
                } finally {
                    IOUtils.closeQuietly(driverXmlInput);
                }
            }
        } catch (Throwable e) {
            log.error("load plugin drivers.xml failed, {}", e.getMessage(), e);
        }

        List<BaseMeta> result = new ArrayList<>();
        List<String> sortedPluginClassNames = new ArrayList<>(scanPluginClasses(pluginLoader));
        Collections.sort(sortedPluginClassNames);

        for (String pluginClassName : sortedPluginClassNames) {
            try {
                result.add(lookupMeta(pluginClassName, loadDef));
            } catch (IOException | RuntimeException e) {
                recordLoadFailure(loadResults, physicalPlugin, pluginClassName, null, null, e);
            }
        }
        return result;
    }

    static Set<String> scanPluginClasses(ResourceLoader pluginLoader) throws IOException {
        return new CgResourceScanner(pluginLoader).getClassNamesSet(//
                new String[] { "com/clougence/" },//
                c -> testPlugin(c.getClassInfo()));
    }

    private static boolean testPlugin(ClassInfo classInfo) {
        if (classInfo != null) {
            for (String anno : classInfo.annos) {
                if (anno.equals(Plugin.class.getName())) {
                    return true;
                }
            }
            for (String faces : classInfo.interFaces) {
                if (faces.equals(DsPlugin.class.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static BaseMeta lookupMeta(String pluginClass, LoadDef loadDef) throws IOException {
        InputStream pluginClassStream = new AutoCloseInputStream(loadDef.pluginResource.getResourceAsStream(pluginClass.replace(".", "/") + ".class"));
        Annotations pluginMetadata = Annotations.ofClass(pluginClassStream);

        Annotation pluginInfo = pluginMetadata.getAnnotation(Plugin.class);
        if (pluginInfo == null) {
            throw new IllegalArgumentException("Missing @Plugin annotation.");
        }
        List<Enum<?>> dsProduct = pluginInfo.getEnumArray("dsProduct", DataSourceType.values());
        if (dsProduct.isEmpty()) {
            return new FooMeta(pluginClass, pluginInfo, PluginManager.globalMeta(), loadDef);
        }
        if (dsProduct.size() == 1) {
            return new DsMeta(pluginClass, pluginInfo, PluginManager.globalMeta(), loadDef);
        }
        throw new IllegalArgumentException("A data source plugin must declare exactly one dsProduct.");
    }

    // ---------------------------------------------
    //                               activate Plugin
    // ---------------------------------------------

    private static void activatePlugin(BaseMeta plugin, List<PluginLoadResult> loadResults) {
        File pluginFile = plugin.getPluginFile();
        String version = null;
        try {
            version = plugin.getPluginVersion();
        } catch (IOException e) {
            log.warn("Read plugin version from '{}' failed.", pluginFile, e);
        }

        String pluginName = plugin.getPluginName();
        try {
            loadOnePlugin(plugin);
            if (plugin instanceof DsMeta dsMeta) {
                PluginManager.addPlugin(dsMeta);
            }
        } catch (Throwable e) {
            recordLoadFailure(loadResults, pluginFile, plugin.getPluginClass(), pluginName, version, e);
            return;
        }

        loadResults.add(new PluginLoadResult(pluginFile.getAbsolutePath(), plugin.getPluginClass(), pluginName, version, true, null));
        log.info("Loaded plugin '{}' (version {}) from '{}'.", pluginName, version, pluginFile);
    }

    private static void recordLoadFailure(List<PluginLoadResult> loadResults, File pluginFile, String pluginClass, String pluginName, String version, Throwable error) {
        String reason = ExceptionUtils.getRootCause(error).toString();
        loadResults.add(new PluginLoadResult(pluginFile.getAbsolutePath(), pluginClass, pluginName, version, false, reason));
        String pluginLabel = StringUtils.defaultIfBlank(pluginClass, pluginFile.getName());
        log.error("Load plugin '{}' from '{}' failed: {}", pluginLabel, pluginFile, reason, error);
    }

    private static void loadOnePlugin(BaseMeta plugin) throws ReflectiveOperationException {
        Class<?> pluginType = plugin.getPlusClassLoader().loadClass(plugin.getPluginClass());
        if (!DsPlugin.class.isAssignableFrom(pluginType)) {
            throw new IllegalStateException(pluginType.getName() + " is not a DsPlugin.");
        }

        DsPlugin pluginInstance = (DsPlugin) pluginType.getDeclaredConstructor().newInstance();
        pluginInstance.loadPlugin(createPluginBinder(plugin));
    }

    private static DsPluginBinder createPluginBinder(BaseMeta plugin) {
        if (plugin instanceof DsMeta) {
            return new DsMetaBinder(PluginManager.globalMeta(), (DsMeta) plugin);
        }
        if (plugin instanceof FooMeta) {
            return new FooMetaBinder(PluginManager.globalMeta(), (FooMeta) plugin);
        }
        throw new IllegalArgumentException("unsupported plugin meta type: " + plugin.getClass().getName());
    }

}
