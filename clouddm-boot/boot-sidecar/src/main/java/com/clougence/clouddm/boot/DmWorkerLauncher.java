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
package com.clougence.clouddm.boot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.codehaus.plexus.classworlds.ClassWorld;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstructUtils;
import com.clougence.clouddm.boot.config.FullAppConfig;
import com.clougence.clouddm.worker.component.rsocket.RSocketClientServiceImpl;
import com.clougence.utils.ShutdownHook;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/9
 */
@Slf4j
public class DmWorkerLauncher {

    public static void main(String[] args) throws Exception {
        System.setProperty("app.logPath", prepareRuntimePath("logs", "sidecar"));

        main(args, null);
    }

    public static void main(String[] args, ClassWorld world) throws Exception {
        if (args == null || args.length == 0) {
            args = new String[] { "start" };
        }

        String action = args[0];

        if ("start".equalsIgnoreCase(action)) {
            Thread.setDefaultUncaughtExceptionHandler(new PrintErrorUncaughtExcHandler());
            System.setProperty("spring.config.name", "default_sidecar,sidecar");

            // system loader
            ClassLoader parentClassLoader = world != null ? world.getRealm("plexus.core") : Thread.currentThread().getContextClassLoader();
            ConfigurableApplicationContext context = initSpring(args, parentClassLoader);

            doStart(context, parentClassLoader);
            return;
        } else if ("stop".equalsIgnoreCase(action)) {
            doStop(args, world);
            return;
        }

        throw new UnsupportedOperationException("Unsupported '" + action + "' command.");
    }

    private static ConfigurableApplicationContext initSpring(String[] args, ClassLoader parentClassLoader) {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(parentClassLoader);
        SpringApplication application = new SpringApplication(resourceLoader, FullAppConfig.class);
        application.setRegisterShutdownHook(false);
        ConfigurableApplicationContext context = application.run(args);
        ClassLoader parentLoader = context.getClassLoader();
        log.info("main classloader is " + parentLoader);
        return context;
    }

    private static void doStart(ConfigurableApplicationContext spring, ClassLoader classLoader) throws Exception {
        // start context
        spring.getBean(DmWorkerPluginLoader.class).loadPlugin(classLoader);
        UnifiedPostConstructUtils.doPostConstruct(spring);
        spring.getBean(RSocketClientServiceImpl.class).init();

        // the following code, Don't change easily, team install.sh rely on it
        log.info("DmWorkerStarter start successfully.");

        ShutdownHook.joinShutdown();
        UnifiedPostConstructUtils.doDestroyConstruct(spring);
    }

    private static void doStop(String[] args, ClassWorld world) {
        log.info("DmWorkerStarter stop command acknowledged. Delegating shutdown to catalina.sh stop/kill flow.");
        System.exit(1);
    }

    private static String prepareRuntimePath(String first, String... more) throws Exception {
        Path path = Paths.get(first, more).toAbsolutePath().normalize();
        Files.createDirectories(path);
        return path.toString();
    }
}
