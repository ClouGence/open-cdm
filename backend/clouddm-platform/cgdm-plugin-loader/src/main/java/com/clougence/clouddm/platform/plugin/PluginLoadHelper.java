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
import java.util.zip.ZipException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clougence.SchemaInitPlugin;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.plugin.info.*;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.schema.SchemaFramework;
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

    public static void loadPlugins(ClassLoader appClassLoader, File... pluginPaths) {
        PluginManager.markStarting();
        try {
            List<BaseMeta> allPlugins = new ArrayList<>();
            for (File path : pluginPaths) {
                allPlugins.addAll(scanPluginDirectory(appClassLoader, path));
            }

            allPlugins.sort(Comparator.comparingInt(BaseMeta::getOrder));

            log.info("Total plugins discovered: {}", allPlugins.size());
            for (BaseMeta plugin : allPlugins) {
                activatePlugin(plugin);
            }
            SchemaFramework.install(new SchemaInitPlugin());
            PluginManager.markReady();
        } catch (Throwable e) {
            PluginManager.markStarting();
            throw e;
        }
    }

    private static List<BaseMeta> scanPluginDirectory(ClassLoader appClassLoader, File pluginPath) {
        List<BaseMeta> result = new ArrayList<>();
        if (pluginPath == null || !pluginPath.exists() || !pluginPath.isDirectory()) {
            return result;
        }

        File[] pluginEntries = pluginPath.listFiles(file -> file.exists() && !file.isHidden());
        if (pluginEntries == null || pluginEntries.length == 0) {
            return Collections.emptyList();
        }

        for (File physicalPlugin : pluginEntries) {
            ResourceLoader pluginLoader = scanPhysicalPlugin(physicalPlugin);
            if (pluginLoader != null) {
                result.addAll(loadPhysicalPluginFromLoader(appClassLoader, physicalPlugin, pluginLoader));
            }
        }

        return result;
    }

    private static ResourceLoader scanPhysicalPlugin(File physicalPlugin) {
        if (physicalPlugin.isDirectory()) {
            return loadPluginsFromDir(physicalPlugin);
        } else if (isJarFile(physicalPlugin)) {
            return loadPluginsFromJar(physicalPlugin);
        } else {
            return null;
        }
    }

    private static ResourceLoader loadPluginsFromDir(File physicalPlugin) {
        MultiResourceLoader pluginLoader = new MultiResourceLoader();
        pluginLoader.addLoader(new PathResourceLoader(physicalPlugin));

        File[] nestedJarFiles = physicalPlugin.listFiles(file -> file.exists() && file.isFile() && isJarFile(file));
        if (nestedJarFiles != null && nestedJarFiles.length > 0) {
            Arrays.sort(nestedJarFiles, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
            for (File nestedJarFile : nestedJarFiles) {
                ResourceLoader nestedLoader = loadPluginsFromJar(nestedJarFile);
                if (nestedLoader != null) {
                    pluginLoader.addLoader(nestedLoader);
                }
            }
        }

        return pluginLoader;
    }

    private static ResourceLoader loadPluginsFromJar(File jarFile) {
        List<String> nestedList = new ArrayList<>();
        try (JarFile jarFileItem = new JarFile(jarFile)) {
            for (JarEntry jarEntry : jarFileItem) {
                String jarEntryName = jarEntry.getName();
                if (StringUtils.endsWith(jarEntryName, ".jar") && !jarEntryName.contains("/")) {
                    nestedList.add(jarEntryName);
                }
            }
        } catch (ZipException e) {
            log.error("the plugin package '{}' is corrupted, error: {}", jarFile, e.getMessage(), e);
            return null;
        } catch (IOException e) {
            log.error("scan plugin package '{}' failed, error: {}", jarFile, e.getMessage(), e);
            return null;
        }

        try {
            return new JarResourceLoader(jarFile, nestedList);
        } catch (IOException e) {
            log.error("create resource loader from '{}' failed, error: {}", jarFile, e.getMessage(), e);
            return null;
        }
    }

    private static boolean isJarFile(File file) {
        return file != null && file.isFile() && StringUtils.endsWithIgnoreCase(file.getName(), "jar");
    }

    // ---------------------------------------------
    //                          Load Physical Plugin
    // ---------------------------------------------

    private static List<BaseMeta> loadPhysicalPluginFromLoader(ClassLoader appClassLoader, File physicalPlugin, ResourceLoader pluginLoader) {
        LoadDef loadDef = new LoadDef(appClassLoader, pluginLoader);

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

        // load Plugins
        try {
            List<BaseMeta> result = new ArrayList<>();
            Set<String> dsPluginClasses = new CgResourceScanner(pluginLoader).getClassNamesSet(//
                    new String[] { "com/clougence/clouddm/" },//
                    c -> testPlugin(c.getClassInfo()));

            List<String> sortedPluginClassNames = new ArrayList<>(dsPluginClasses);
            Collections.sort(sortedPluginClassNames);

            for (String pluginClassName : sortedPluginClassNames) {
                result.add(lookupMeta(pluginClassName, loadDef));
            }
            return result;
        } catch (IOException e) {
            log.error("scan plugins from '{}' failed, error: {}", physicalPlugin, e.getMessage(), e);
            return Collections.emptyList();
        }
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
        if (pluginInfo != null) {
            List<Enum<?>> dsProduct = pluginInfo.getEnumArray("dsProduct", DataSourceType.values());
            if (dsProduct.isEmpty()) {
                return new FooMeta(pluginClass, pluginInfo, PluginManager.globalMeta(), loadDef);
            } else if (dsProduct.size() == 1) {
                return new DsMeta(pluginClass, pluginInfo, PluginManager.globalMeta(), loadDef);
            } else {
                log.error("Plugin dsProduct not support multi dsProduct, class=" + pluginClass);
            }
        }
        return null;
    }

    // ---------------------------------------------
    //                               activate Plugin
    // ---------------------------------------------

    private static void activatePlugin(BaseMeta plugin) {
        try {
            loadOnePlugin(plugin);
        } catch (Throwable e) {
            log.error("load plugin failed, {}", e.getMessage(), e);
            return;
        }

        if (plugin instanceof DsMeta) {
            PluginManager.addPlugin((DsMeta) plugin);
        }
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
