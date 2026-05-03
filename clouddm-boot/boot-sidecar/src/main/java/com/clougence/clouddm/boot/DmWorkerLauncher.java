package com.clougence.clouddm.boot;

import org.codehaus.plexus.classworlds.ClassWorld;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.DefaultResourceLoader;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstructUtils;
import com.clougence.clouddm.api.common.exception.PrintErrorUncaughtExcHandler;
import com.clougence.clouddm.worker.component.rsocket.RSocketClientServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/9
 */
@Slf4j
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ComponentScan({ "com.clougence.clouddm.boot", "com.clougence.clouddm.worker", "com.clougence.clouddm.worker.*", "com.clougence.clouddm.comm.*" })
public class DmWorkerLauncher {

    public static void main(String[] args) throws Exception {
        System.setProperty("app.logPath", "logs/sidecar");

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
        System.setProperty("spring.config.name", "default_sidecar,sidecar");
        String action = args[0];

        if ("start".equalsIgnoreCase(action)) {
            // system loader
            ClassLoader parentClassLoader = world != null ? world.getRealm("plexus.core") : Thread.currentThread().getContextClassLoader();
            ConfigurableApplicationContext context = initSpring(args, parentClassLoader);

            doStart(context, parentClassLoader);
        } else if ("stop".equalsIgnoreCase(action)) {
            doStop(args, world);
        }

        throw new UnsupportedOperationException("Unsupported '" + action + "' command.");
    }

    private static ConfigurableApplicationContext initSpring(String[] args, ClassLoader parentClassLoader) {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(parentClassLoader);
        SpringApplication application = new SpringApplication(resourceLoader, DmWorkerLauncher.class);
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
        System.exit(1);
    }
}
