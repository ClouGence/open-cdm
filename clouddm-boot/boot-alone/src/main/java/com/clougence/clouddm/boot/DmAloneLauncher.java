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
import com.clougence.clouddm.init.InitApplication;
import com.clougence.clouddm.init.service.InitDBStatusDetector;
import com.clougence.clouddm.init.service.SystemStatusResult;
import com.clougence.utils.format.DateFormatType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DmAloneLauncher {

    private static final String WORKER_PACKAGE_NAME = "com.clougence.clouddm.worker";

    public static void main(String[] args) throws Exception {
        System.setProperty("app.buildId", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.setProperty("app.buildVersion", "xxx.xxx.xxx(" + DateFormatType.s_yyyyMMdd.format(new Date()) + ")");
        System.setProperty("app.logPath", "logs/alone");
        System.setProperty("app.data", "data/alone");

        main(args, null);
    }

    public static void main(String[] args, ClassWorld world) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new PrintErrorUncaughtExcHandler());
        System.setProperty("spring.config.name", "default_alone,alone");
        System.setProperty("app.mode", "embedded");

        SystemStatusResult statusResult = //
                InitDBStatusDetector.detectDBStatus("default_alone.properties", "alone.properties");
        SystemStatus dbStatus = statusResult.getStatus();
        log.info("[DmAloneLauncher] Database status check: {}, reason={}, dbError={}", dbStatus, statusResult.getInitReason(), statusResult.getDbError());

        if (dbStatus == SystemStatus.Ready) {
            log.info("[DmAloneLauncher] Starting in FULL mode...");
            startApp(args, world);
        } else {
            log.info("[DmAloneLauncher] Starting in INIT mode (minimal web server)...");
            startInit(args, world);
        }
    }

    private static void startInit(String[] args, ClassWorld world) throws Exception {
        log.info("[DmAloneLauncher] Starting init application (separate Spring Boot app)...");
        InitApplication.main(args);
    }

    private static void startApp(String[] args, ClassWorld world) throws Exception {
        ClassLoader parentClassLoader = world != null ? world.getRealm("plexus.core") : Thread.currentThread().getContextClassLoader();
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(parentClassLoader);
        SpringApplication application = new SpringApplication(resourceLoader, FullAppConfig.class);
        ConfigurableApplicationContext context = application.run(args);

        // start:console
        context.getBean(EmbeddedWorkerBootstrap.class).init();
        context.getBean(DmAlonePluginLoader.class).loadPlugin(parentClassLoader);
        UnifiedPostConstructUtils.doPostConstruct(context, clz -> !isWorkerClass(clz));
        // start:worker
        embeddedWorker(context);

        log.info("[DmAloneLauncher] Alone All Context Inited.");
        ShutdownHook.joinShutdown();
        UnifiedPostConstructUtils.doDestroyConstruct(context);
    }

    // ========================================================================
    // inner
    // ========================================================================

    private static void embeddedWorker(ConfigurableApplicationContext context) {
        Thread workerInitThread = new Thread(() -> {
            try {
                UnifiedPostConstructUtils.doPostConstruct(context, DmAloneLauncher::isWorkerClass);
                log.info("[DmAloneLauncher] Embedded worker plugins finished.");
            } catch (Throwable e) {
                log.error("[DmAloneLauncher] Embedded worker bootstrap failed.", e);
            }
        }, "alone-worker-init");
        workerInitThread.setDaemon(true);
        workerInitThread.start();
    }

    private static boolean isWorkerClass(Class<?> pcClass) {
        Package beanPackage = pcClass.getPackage();
        String packageName = beanPackage == null ? "" : beanPackage.getName();
        return WORKER_PACKAGE_NAME.equals(packageName) || packageName.startsWith(WORKER_PACKAGE_NAME + ".");
    }
}
