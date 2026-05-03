package com.clougence.clouddm.boot;

import java.util.Date;

import org.codehaus.plexus.classworlds.ClassWorld;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstructUtils;
import com.clougence.clouddm.console.web.global.exception.PrintErrorUncaughtExcHandler;
import com.clougence.clouddm.init.DmFixLauncher;
import com.clougence.utils.format.DateFormatType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@SpringBootConfiguration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class })
@ComponentScan({ "com.clougence.clouddm.boot", "com.clougence.clouddm.console.web", "com.clougence.clouddm.console.web.*", //
                 "com.clougence.clouddm.worker", "com.clougence.clouddm.worker.*",           //
                 "com.clougence.clouddm.comm.component.*", "com.clougence.clouddm.init",     //
                 "com.clougence.rdp.*" })
public class DmAloneLauncher implements WebMvcConfigurer {

    private static final String WORKER_PACKAGE_NAME = "com.clougence.clouddm.worker";
    private static boolean      usingDevMain        = false;

    public static void main(String[] args) throws Exception {
        System.setProperty("app.buildId", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.setProperty("app.buildVersion", "xxx.xxx.xxx(" + DateFormatType.s_yyyyMMdd.format(new Date()) + ")");
        System.setProperty("app.logPath", "logs/alone");
        System.setProperty("app.data", "data/alone");
        usingDevMain = true;

        if (args == null || args.length == 0) {
            args = new String[] { "start" };
        }

        main(args, null);
    }

    public static void main(String[] args, ClassWorld world) throws Exception {
        if (args == null || args.length == 0) {
            args = new String[] { "start" };
        }

        Thread.setDefaultUncaughtExceptionHandler(new PrintErrorUncaughtExcHandler());
        System.setProperty("spring.config.name", "default_alone,alone");
        System.setProperty("app.mode", "embedded");
        String action = args[0];

        if ("start".equalsIgnoreCase(action) || "fix".equalsIgnoreCase(action)) {
            // system loader
            ClassLoader parentClassLoader = world != null ? world.getRealm("plexus.core") : Thread.currentThread().getContextClassLoader();
            ConfigurableApplicationContext context = initSpring(args, parentClassLoader);

            if ("start".equalsIgnoreCase(action)) {
                doStart(context, parentClassLoader);
                return;
            } else if ("fix".equalsIgnoreCase(action)) {
                doFix(context, parentClassLoader);
                return;
            }
        } else if ("stop".equalsIgnoreCase(action)) {
            doStop(args, world);
            return;
        }

        throw new UnsupportedOperationException("Unsupported '" + action + "' command.");
    }

    private static ConfigurableApplicationContext initSpring(String[] args, ClassLoader parentClassLoader) {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(parentClassLoader);
        SpringApplication application = new SpringApplication(resourceLoader, DmAloneLauncher.class);
        ConfigurableApplicationContext context = application.run(args);
        ClassLoader parentLoader = context.getClassLoader();
        log.info("main classloader is " + parentLoader);
        return context;
    }

    private static void doStart(ConfigurableApplicationContext spring, ClassLoader classLoader) throws Exception {
        // start context: embedded worker bootstrap first (populates GlobalConfUtils.config)
        if (usingDevMain) {
            DmFixLauncher.initDB(spring);
        }

        spring.getBean(EmbeddedWorkerBootstrap.class).init();
        spring.getBean(DmAlonePluginLoader.class).loadPlugin(classLoader);
        UnifiedPostConstructUtils.doPostConstruct(spring, DmAloneLauncher::isNonWorkerPostConstruct);

        // start context: start
        if (usingDevMain) {
            log.info("main doFixWork...");
            DmFixLauncher.doFix(spring);
        }

        embeddedWorker(spring);

        log.info("[DmAloneLauncher] Alone All Context Inited.");
        ShutdownHook.joinShutdown();
        UnifiedPostConstructUtils.doDestroyConstruct(spring);
    }

    private static void doFix(ConfigurableApplicationContext spring, ClassLoader classLoader) throws Exception {
        //
        DmFixLauncher.initDB(spring);

        //
        spring.getBean(EmbeddedWorkerBootstrap.class).init();
        spring.getBean(DmAlonePluginLoader.class).loadPlugin(classLoader);
        UnifiedPostConstructUtils.doPostConstruct(spring);
        //spring.getBean(RSocketServerServiceImpl.class).init();

        //
        DmFixLauncher.doFix(spring);
        UnifiedPostConstructUtils.doDestroyConstruct(spring);
        System.exit(0);
    }

    private static void embeddedWorker(ConfigurableApplicationContext context) {
        Thread workerInitThread = new Thread(() -> {
            try {
                UnifiedPostConstructUtils.doPostConstruct(context, DmAloneLauncher::isWorkerPostConstruct);
                log.info("[DmAloneLauncher] Embedded worker plugins finished.");
            } catch (Throwable e) {
                log.error("[DmAloneLauncher] Embedded worker bootstrap failed.", e);
            }
        }, "alone-worker-init");
        workerInitThread.setDaemon(true);
        workerInitThread.start();
    }

    private static boolean isNonWorkerPostConstruct(Class<?> pcClass) {
        return !isWorkerPostConstruct(pcClass);
    }

    private static boolean isWorkerPostConstruct(Class<?> pcClass) {
        Package beanPackage = pcClass.getPackage();
        String packageName = beanPackage == null ? "" : beanPackage.getName();
        return WORKER_PACKAGE_NAME.equals(packageName) || packageName.startsWith(WORKER_PACKAGE_NAME + ".");
    }

    private static void doStop(String[] args, ClassWorld world) {
        System.exit(1);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
