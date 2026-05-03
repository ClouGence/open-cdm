package com.clougence.rdp.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.clougence.rdp.dal.enumeration.RdpProduct;
import com.clougence.rdp.service.enumeration.AlertImType;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * @author bucketli 2020-01-04 09:44
 * @since 1.1.3
 */
@Data
@Configuration
@FieldNameConstants
public class RdpConsoleConfig {

    @Value("${clougence.rdp.async.queue:500}")
    private int            asyncTaskQueueSize;

    @Value("${clougence.rdp.async.threadpool:10}")
    protected int          asyncThreadCount;

    @Value("${clougence.rdp.async.dock_size:40}")
    protected int          asyncTaskDockSize;

    @Value("${clougence.rdp.console.csrf:false}")
    private Boolean        activeCsrfCheck;

    @Value("${clougence.rdp.login.retry.max-count:5}")
    private int            retryLoginMaxCount;

    @Value("${clougence.rdp.login.reset.period.minuetes:5}")
    private String         resetLoginLimitationWaitTimeMin;

    @Value("${clougence.rdp.deploy.env}")
    private RdpDeployEnv   deployEnv;

    @Value("${clougence.rdp.deploy.context-path:#{NULL}}")
    private String         deployContextPath;              // like  "http://nihao.com/clouddm/"

    @Value("${clougence.rdp.black.ds.self:#{NULL}}")
    private String         dsBlacklistBySelf;

    @Value("${clougence.rdp.black.ds.aliyun:#{NULL}}")
    private String         dsBlacklistByAliyun;

    @Value("${clougence.rdp.black.fetchtype.aliyun:#{NULL}}")
    private String         dsBlackFetchTypeByAliyun;

    @Value("${clougence.rdp.black.ds.aws:#{NULL}}")
    private String         dsBlacklistByAws;

    @Value("${clougence.rdp.black.ds.azure:#{NULL}}")
    private String         dsBlacklistByAzure;

    @Value("${clougence.rdp.black.ds.huawei:#{NULL}}")
    private String         dsBlacklistByHuawei;

    @Value("${clougence.rdp.black.ds.tencent:#{NULL}}")
    private String         dsBlacklistByTencent;

    @Value("${clougence.rdp.black.ds.independent:#{NULL}}")
    private String         dsBlacklistByIndependent;

    @Value("${clougence.rdp.black.deploy:#{NULL}}")
    private String         deployBlacklist;

    @Value("${clougence.rdp.black.user_config:#{NULL}}")
    private String         userConfigBlacklist;

    @Value("${clougence.rdp.black.menu_category:#{NULL}}")
    private String         menuCategoryBlacklist;

    @Value("${clougence.rdp.user.domain_suffix:clougence.com}")
    private String         userDomainSuffix;

    @Value("${clougence.rdp.product.trial:true}")
    private boolean        productTrial;

    @Value("${clougence.rdp.product.trial.verify_code:777777}")
    private String         productTrialVerifyCode;

    @Value("${clougence.rdp.console.oppassword:false}")
    private boolean        oppassword;

    @Value("${clougence.rdp.inner-roles:Manager,DBA,Developers,PM}")
    private List<String>   innerRoles;

    @Value("${clougence.rdp.console.enable_watermark:false}")
    private boolean        enableWaterMark;

    @Value("${clougence.rdp.console.enable_product_cluster:false}")
    private boolean        enableProductCluster;

    @Value("${clougence.rdp.console.default_product:CloudCanal}")
    private RdpProduct     defaultProduct;

    @Value("${clougence.rdp.login.expire.sec:86400}")
    private int            loginExpireTimeSec;             // default is 24*3600

    @Value("${clougence.rdp.login.cookie.domain:#{NULL}}")
    private String         loginCookieDomain;

    //e.g. clougence.rdp.user.autoreset_pwd=test@clougence.com:clougence2021
    @Value("${clougence.rdp.user.autoreset_pwd:#{NULL}}")
    private String         autoResetUserPwd;

    @Value("${clougence.rdp.api.aliyun.invpc:false}")
    private boolean        inAliyunVpc;

    @Value("${clougence.rdp.install.oss.special:false}")
    private boolean        specialOss;

    @Value("${clougence.rdp.install.oss.special.endpoint:#{NULL}}")
    private String         specialOssEndPoint;

    @Value("${clougence.rdp.install.oss.special.bucket:#{NULL}}")
    private String         specialOssBucket;

    @Value("${clougence.rdp.consoletask.engine.async.threadpool:30}")
    protected int          asyncThdCount;

    @Value("${clougence.rdp.consoletask.engine.async.lifetimehr:84}")
    protected int          asyncExecLifetimeHr;

    @Value("${clougence.rdp.consoletask.engine.async.fallbackmi:2}")
    protected int          asyncExecFallBackMi;

    @Value("${clougence.rdp.license.server.address:http://127.0.0.1:8333}")
    private String         licenseServerAddress;

    @Value("${spring.profiles.active:Unknown}")
    private String         activeProfile;

    @Value("${clougence.rdp.install.oss.ak:ossDownloadAk}")
    private String         ossDownloadAk;

    @Value("${clougence.rdp.install.oss.sk:ossDownloadSk}")
    private String         ossDownloadSk;

    @Value("${clougence.rdp.install.s3.ak:s3DownloadAk}")
    private String         s3DownloadAk;

    @Value("${clougence.rdp.install.s3.sk:s3DownloadSk}")
    private String         s3DownloadSk;

    @Value("${clougence.rdp.saas.marketplace.aws.ak:#{NULL}}")
    private String         awsMarketplaceAk;

    @Value("${clougence.rdp.saas.marketplace.aws.sk:#{NULL}}")
    private String         awsMarketplaceSk;

    @Value("${clougence.rdp.saas.marketplace.aws.region:#{NULL}}")
    private String         awsMarketplaceRegion;

    @Value("${clougence.saas.managed.primaryuid:#{NULL}}")
    private String         saasManagedPrimaryUid;

    // DEFAULT SMS ALERT CONFIG WITH ALIYUN SMS SERVICE

    @Value("${clougence.rdp.sms.aliyun.ak:accessKeyId}")
    private String         aliyunAlertAk;

    @Value("${clougence.rdp.sms.aliyun.sk:accessSecret}")
    private String         aliyunAlertSk;

    // DEFAULT IM ALERT CONFIG

    @Value("${clougence.rdp.alert.type:#{NULL}}")
    protected AlertImType  alertImType;

    @Value("${clougence.rdp.alert.dingtalk.alerturl:#{NULL}}")
    private String         dingTalkAlertUrl;

    @Value("${clougence.rdp.alert.weixin.alerturl:#{NULL}}")
    private String         weixinAlertUrl;

    @Value("${clougence.rdp.alert.slack.alerturl:#{NULL}}")
    private String         slackAlertUrl;

    @Value("${clougence.rdp.alert.discord.alerturl:#{NULL}}")
    private String         discordAlertUrl;

    @Value("${clougence.rdp.alert.feishu.alerturl:#{NULL}}")
    private String         feishuAlertUrl;

    @Value("${clougence.rdp.alert.custom.alerturl:#{NULL}}")
    private String         customAlertUrl;

    @Value("${clougence.rdp.alert.timeout.ms:5000}")
    protected String       alertImTimeoutMs;

    @Value("${console.config.package.mode:TGZ}")
    private RdpPackageMode consolePackageMode;

    @Value("${clougence.rdp.sms.verify_code:true}")
    private boolean        verifyCodeEnable;

    // DEFAULT MAIL ALERT CONFIG

    @Value("${spring.mail.host:#{NULL}}")
    private String         emailHostConfigKey;

    @Value("${spring.mail.port:#{NULL}}")
    private String         emailPortConfigKey;

    @Value("${spring.mail.username:#{NULL}}")
    private String         emailUserNameConfigKey;

    @Value("${spring.mail.password:#{NULL}}")
    private String         emailPasswordConfigKey;

    @Value("${spring.mail.properties.from:#{NULL}}")
    private String         emailFromConfigKey;

    @Value("${spring.mail.properties.display:ClouGence}")
    private String         emailDisplayConfigKey;

    @Value("${mail.smtp.auth:true}")
    private String         emailSmtpAuthKey;

    @Value("${mail.smtp.starttls.enable:true}")
    private String         emailSmtpStarttlsEnableKey;

    @Value("${mail.smtp.starttls.required:true}")
    private String         emailSmtpStarttlsRequiredKey;

    @Value("${mail.smtp.ssl.enable:true}")
    private String         emailSmtpSslEnableKey;

    @Value("${mail.transport.protocol:smtps}")
    private String         emailTransportProtocolKey;

    @Value("${clougence.rdp.crypt.publicKey:0443a779e887425e2fa3bcc92ed70e0fd17647c82191ef9b82e4ecc98cb83cd74b7e40b3334bfff818504f21e583a3846d236782bb12f36fb6663d7c391d232699}")
    private String         publicKey;

    @Value("${clougence.rdp.crypt.privateKey:48d75671c9f650e3f0d689b1ef213eecb3cfb7c9a3d41044aef6b71cc84b0880}")
    private String         privateKey;

    @Value("${clougence.rdp.dsconfig.validate.enable:true}")
    private Boolean        rdpDsConfigValidateEnable;

    @Value("${clougence.rdp.audit.export.max_export_size:100000}")
    private Integer        maxExportSize;

    @Value("${clougence.license.payment.callback.redirect_url:#{NULL}}")
    private String         licensePaymentRedirectUrl;

    @Value("${clougence.prepay.payment.callback.redirect_url:#{NULL}}")
    private String         prepayPaymentRedirectUrl;

    @Value("${console.config.marketplace.aws.redirect.url:#{NULL}}")
    private String         marketplaceAwsRedirectUrl;

    @Value("${console.config.official.site.redirect.url:#{NULL}}")
    private String         officialSiteRedirectUrl;

    @Value("${clougence.bind.card.redirect_url:#{NULL}}")
    private String         bindCardRedirectUrl;

    @Value("${clougence.official.site.address:https://cloudcanal.clougence.com}")
    private String         officialSiteAddress;
}
