package com.clougence.clouddm.console.web.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.constants.DmModeFeatured;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-04 09:44
 * @since 1.1.3
 */
@Getter
@Setter
@Configuration
public class DmConsoleConfig {

    // for Async Task
    @Value("${clouddm.consolejob.engine.async.threadpool:30}")
    protected int            asyncThreadCount;
    @Value("${clouddm.consolejob.engine.async.queue:500}")
    protected int            asyncTaskQueueSize;
    @Value("${clouddm.consolejob.engine.async.dock_size:40}")
    protected int            asyncTaskDockSize;

    // for install
    @Value("${clougence.rdp.install.oss.ak:ossDownloadAk}")
    private String           ossDownloadAk;
    @Value("${clougence.rdp.install.oss.sk:ossDownloadSk}")
    private String           ossDownloadSk;
    @Value("${clouddm.install.aliyun.oss.package:ossDownloadPackageName}")
    private String           ossDownloadPackageName;
    @Value("${clouddm.install.aliyun.oss.download_site:ossDownloadSite}")
    private String           ossDownloadSite;
    @Value("${clouddm.install.aliyun.oss.download_region:hangzhou}")
    private String           ossDownloadRegion;
    @Value("${clouddm.upgrade.server:127.0.0.1:8113}")
    private String           upgradeServer;

    // for rsocket
    @Value("${clouddm.rsocket.dns:clouddm_console}")
    private String           consoleRsocketDns;
    @Value("${clouddm.rsocket.printargs:false}")
    private boolean          consoleRsocketPrintArgs;
    @Value("${clouddm.rsocket.console.port:8008}")
    private int              rsocketConsolePort;

    // for personal_desktop
    @Value("${clouddm.mode.type:output}")
    private DmMode           dmMode;
    @Value("${clouddm.mode.featured:basic}")
    private DmModeFeatured   dmModeFeatured;
    private DmPersonalConfig personalConfig = new DmPersonalConfig();

    // for console query
    @Value("${clouddm.console.query_queue_size:5000}")
    private int              consoleQueryQueueSize;
    @Value("${clouddm.console.cross.origins:#{null}}")
    private String           consoleAllowedOrigins;

    // for Product
    @Value("${clouddm.features.auto_upgrade_sec_rules:true}")
    private boolean          autoUpdateInnerRules;

    @Value("${clougence.clouddm.console.openapi.timeout:120}")
    private Integer          openApiTimeout;
}
