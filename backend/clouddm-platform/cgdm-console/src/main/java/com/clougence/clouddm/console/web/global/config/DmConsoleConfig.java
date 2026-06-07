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
package com.clougence.clouddm.console.web.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.clougence.clouddm.console.web.constants.DmModeFeatured;
import com.clougence.rdp.global.config.RdpPackageMode;

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
    protected int          asyncThreadCount;
    @Value("${clouddm.consolejob.engine.async.queue:500}")
    protected int          asyncTaskQueueSize;
    @Value("${clouddm.consolejob.engine.async.dock_size:40}")
    protected int          asyncTaskDockSize;

    // compatibility for legacy RDP async task config
    @Value("${clougence.rdp.async.queue:500}")
    private int            rdpAsyncTaskQueueSize;

    @Value("${clouddm.upgrade.server:127.0.0.1:8113}")
    private String         upgradeServer;

    // for rsocket
    @Value("${clouddm.rsocket.dns:clouddm_console}")
    private String         consoleRsocketDns;
    @Value("${clouddm.rsocket.printargs:false}")
    private boolean        consoleRsocketPrintArgs;
    @Value("${clouddm.rsocket.console.port:8008}")
    private int            rsocketConsolePort;

    @Value("${clouddm.mode.featured:basic}")
    private DmModeFeatured dmModeFeatured;

    // for console query
    @Value("${clouddm.console.query_queue_size:5000}")
    private int            consoleQueryQueueSize;
    // for Product
    @Value("${clouddm.features.auto_upgrade_sec_rules:true}")
    private boolean        autoUpdateInnerRules;

    @Value("${clougence.clouddm.console.openapi.timeout:120}")
    private Integer        openApiTimeout;

    @Value("${clougence.rdp.console.csrf:false}")
    private Boolean        activeCsrfCheck;
    @Value("${clougence.rdp.login.retry.max-count:5}")
    private int            retryLoginMaxCount;
    @Value("${clougence.rdp.login.reset.period.minuetes:5}")
    private String         resetLoginLimitationWaitTimeMin;
    @Value("${clougence.rdp.deploy.context-path:#{NULL}}")
    private String         deployContextPath;
    @Value("${clougence.rdp.black.user_config:#{NULL}}")
    private String         userConfigBlacklist;
    @Value("${clougence.rdp.console.oppassword:false}")
    private boolean        oppassword;
    @Value("${clougence.rdp.inner-roles:Manager,DBA,Developers,PM}")
    private List<String>   innerRoles;
    @Value("${clougence.rdp.console.enable_watermark:false}")
    private boolean        enableWaterMark;
    @Value("${clougence.rdp.console.enable_product_cluster:false}")
    private boolean        enableProductCluster;
    @Value("${clougence.rdp.login.expire.sec:86400}")
    private int            loginExpireTimeSec;
    @Value("${clougence.rdp.login.cookie.domain:#{NULL}}")
    private String         loginCookieDomain;
    @Value("${console.config.package.mode:TGZ}")
    private RdpPackageMode consolePackageMode;
    @Value("${clougence.rdp.crypt.publicKey:0443a779e887425e2fa3bcc92ed70e0fd17647c82191ef9b82e4ecc98cb83cd74b7e40b3334bfff818504f21e583a3846d236782bb12f36fb6663d7c391d232699}")
    private String         publicKey;
    @Value("${clougence.rdp.crypt.privateKey:48d75671c9f650e3f0d689b1ef213eecb3cfb7c9a3d41044aef6b71cc84b0880}")
    private String         privateKey;
    @Value("${clougence.rdp.dsconfig.validate.enable:true}")
    private Boolean        rdpDsConfigValidateEnable;
    @Value("${clougence.rdp.audit.export.max_export_size:100000}")
    private Integer        maxExportSize;
}
