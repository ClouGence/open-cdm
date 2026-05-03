package com.clougence.clouddm.init;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.init.config.DmFlywayInit;

import lombok.extern.slf4j.Slf4j;

/**
 * Unified entry point for system initialization work.
 * <p>
 * Boot modules (boot-console, boot-alone) should reference this service
 * for Flyway database migration operations.
 * </p>
 */
@Slf4j
@Service
public class DmInitWork {

    @Resource
    private DmFlywayInit dmFlywayInit;

    /**
     * Execute Flyway database migrations for both DM and RDP modules.
     */
    public void doUpgrade() {
        dmFlywayInit.doUpgrade();
    }
}
