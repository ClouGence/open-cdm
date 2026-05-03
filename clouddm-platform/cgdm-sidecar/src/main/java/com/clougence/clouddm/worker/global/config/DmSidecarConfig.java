package com.clougence.clouddm.worker.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-04 09:44
 * @since 1.1.3
 */
@Getter
@Setter
@Configuration
public class DmSidecarConfig {

    @Value("${dw.sidecar.session.max-count}")
    private int     maxSessionCount     = 1000;
    @Value("${dw.sidecar.tool-session.max-count}")
    private int     maxToolsCount       = 1000;

    @Value("${dw.sidecar.online.max-queue}")
    private int     onlineMaxQueue      = 4000;
    @Value("${dw.sidecar.online.max-executor}")
    private int     onlineMaxExecutor   = 200;
    @Value("${dw.sidecar.online.max-fetch-count}")
    private int     onlineMaxFetchCount = 500;

    @Value("${dw.sidecar.export.max-queue}")
    private int     exportMaxQueue      = 4000;
    @Value("${dw.sidecar.export.max-executor}")
    private int     exportMaxExecutor   = 200;

    @Value("${dm.sidecar.audit-report:true}")
    private boolean auditReport         = true;

    @Value("${dm.sidecar.metric-report:true}")
    private boolean metricReport        = true;
}
