package com.clougence.clouddm.boot;

import java.util.Date;

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
import com.clougence.clouddm.init.service.InitDBStatusDetector;
import com.clougence.clouddm.init.service.SystemStatusResult;
import com.clougence.utils.format.DateFormatType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DmConsoleLauncher {

    public static void main(String[] args) throws Exception {
        System.setProperty("app.buildId", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.setProperty("app.buildVersion", "xxx.xxx.xxx(" + DateFormatType.s_yyyyMMdd.format(new Date()) + ")");
        System.setProperty("app.logPath", "logs/console");
        System.setProperty("app.data", "data/console");

        main(args, null);
    }

    public static void main(String[] args, ClassWorld world) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new PrintErrorUncaughtExcHandler());
        System.setProperty("spring.config.name", "default_console,console");

        SystemStatusResult statusResult =//
                InitDBStatusDetector.detectDBStatus("default_console.properties", "console.properties");
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
        ConfigurableApplicationContext context = application.run(args);

        context.getBean(DmConsolePluginLoader.class).loadPlugin(parentClassLoader);
        UnifiedPostConstructUtils.doPostConstruct(context);
        context.getBean(RSocketServerServiceImpl.class).init();

        log.info("[DmConsoleLauncher] Console All Context Inited.");
        ShutdownHook.joinShutdown();
        UnifiedPostConstructUtils.doDestroyConstruct(context);
    }
}
