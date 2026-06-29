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
import com.clougence.clouddm.console.web.constants.SystemStatus;
import com.clougence.clouddm.console.web.global.exception.PrintErrorUncaughtExcHandler;
import com.clougence.clouddm.console.web.global.rsocket.RSocketServerServiceImpl;
import com.clougence.clouddm.init.InitApplication;
import com.clougence.clouddm.init.model.SystemStatusResult;
import com.clougence.clouddm.init.service.InitDBStatusDetector;
import com.clougence.clouddm.init.service.SysInitDefService;
import com.clougence.utils.ShutdownHook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DmConsoleLauncher {

    public static void main(String[] args) throws Exception {
        System.setProperty("app.logPath", prepareRuntimePath("logs", "console"));
        System.setProperty("app.data", prepareRuntimePath("data", "console"));

        main(args, null);
    }

    public static void main(String[] args, ClassWorld world) throws Exception {
        String action = (args != null && args.length > 0) ? args[0] : "start";

        if ("stop".equalsIgnoreCase(action)) {
            doStop();
            return;
        }

        if (!"start".equalsIgnoreCase(action)) {
            throw new UnsupportedOperationException("Unsupported '" + action + "' command.");
        }

        Thread.setDefaultUncaughtExceptionHandler(new PrintErrorUncaughtExcHandler());
        System.setProperty("spring.config.name", "default_console,console");

        SystemStatusResult statusResult = InitDBStatusDetector.detectDBStatus(new SysInitDefService().loadSystemProperties());
        SystemStatus dbStatus = statusResult.getStatus();
        log.info("[DmConsoleLauncher] Database status check: {}, reason={}, dbError={}", dbStatus, statusResult.getInitReason(), statusResult.getDbError());

        if (dbStatus == SystemStatus.Ready) {
            log.info("[DmConsoleLauncher] Starting in FULL mode...");
            startApp(args, world);
        } else {
            log.info("[DmConsoleLauncher] Starting in INIT mode (minimal web server)...");
            startInit(args, world);
        }
    }

    private static void startInit(String[] args, ClassWorld world) throws Exception {
        log.info("[DmConsoleLauncher] Starting init application (separate Spring Boot app)...");
        InitApplication.main(args);
    }

    private static void startApp(String[] args, ClassWorld world) throws Exception {
        ClassLoader parentClassLoader = world != null ? world.getRealm("plexus.core") : Thread.currentThread().getContextClassLoader();
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(parentClassLoader);
        SpringApplication application = new SpringApplication(resourceLoader, FullAppConfig.class);
        application.setRegisterShutdownHook(false);
        ConfigurableApplicationContext context = application.run(args);

        context.getBean(DmConsolePluginLoader.class).loadPlugin(parentClassLoader);
        UnifiedPostConstructUtils.doPostConstruct(context);
        context.getBean(RSocketServerServiceImpl.class).init();

        log.info("[DmConsoleLauncher] Console All Context Inited.");
        ShutdownHook.joinShutdown();
        UnifiedPostConstructUtils.doDestroyConstruct(context);
    }

    private static void doStop() {
        log.info("[DmConsoleLauncher] Stop command acknowledged. Delegate shutdown to catalina.sh stop/kill flow.");
        System.exit(1);
    }

    private static String prepareRuntimePath(String first, String... more) throws Exception {
        Path path = Paths.get(first, more).toAbsolutePath().normalize();
        Files.createDirectories(path);
        return path.toString();
    }
}
