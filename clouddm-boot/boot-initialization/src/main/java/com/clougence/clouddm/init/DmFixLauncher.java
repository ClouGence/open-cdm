package com.clougence.clouddm.init;

import org.springframework.context.ConfigurableApplicationContext;

import com.clougence.clouddm.init.fix.DmFixDmDsConfig;
import com.clougence.clouddm.init.fix.DmFixSecRules;
import com.clougence.clouddm.init.fix.RdpFixInnerUser;
import com.clougence.clouddm.init.fix.RdpFixUserRole;

import lombok.extern.slf4j.Slf4j;

/**
 * Standalone Spring Boot launcher for the &quot;fix&quot; command mode.
 * <p>
 * Starts a non-web Spring application, runs Flyway upgrade and all
 * database fix/revision tasks, then exits.
 * </p>
 *
 * Boot modules (boot-console, boot-alone) simply delegate to this launcher:
 */
@Slf4j
public class DmFixLauncher {

    public static void initDB(ConfigurableApplicationContext spring) {
        log.info("[DmFixLauncher] run initDB...");

        try {
            spring.getBean(DmInitWork.class).doUpgrade();
        } catch (Exception e) {
            log.error("[DmFixLauncher] run initDB failed.", e);
            System.exit(1);
        }
    }

    public static void doFix(ConfigurableApplicationContext spring) {
        log.info("[DmFixLauncher] run fixWorker...");

        try {
            spring.getBean(RdpFixInnerUser.class).init();
            spring.getBean(RdpFixUserRole.class).init();
            spring.getBean(DmFixSecRules.class).init();
            spring.getBean(DmFixDmDsConfig.class).init();
        } catch (Exception e) {
            log.error("[DmFixLauncher] run initDB failed.", e);
            System.exit(1);
        }
    }
}
