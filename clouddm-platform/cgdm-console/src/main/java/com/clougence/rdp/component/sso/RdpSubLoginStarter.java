package com.clougence.rdp.component.sso;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeployMode;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpSubLoginStarter implements UnifiedPostConstruct {

    @Resource
    private RdpUserMapper      rdpUserMapper;
    @Resource
    private RdpSubLoginService loginService;

    @Override
    public void init() throws Exception {
        ThreadUtils.runDaemonThread(this::initLogin);
    }

    @Override
    public void stop() {

    }

    private void initLogin() {
        List<String> serviceNames;
        try {
            serviceNames = PluginManager.getSpiNamesByType(LoginProviderSpi.class);
            if (CollectionUtils.isNotEmpty(serviceNames)) {
                log.info("[RdpSubLoginStarter] SsoService found " + serviceNames.size() + " provider is " + StringUtils.join(serviceNames.toArray(), ","));
            } else {
                log.error("[RdpSubLoginStarter] SsoService not found any provider.");
                return;
            }
        } catch (Exception e) {
            log.error("[RdpSubLoginStarter] SsoService found provider error " + e.getMessage(), e);
            return;
        }

        if (GlobalDeployMode.inPrivate()) {
            // start plugin for user.
            List<RdpUserDO> users = this.rdpUserMapper.listPrimaryAccount();
            for (RdpUserDO rdpUserDO : users) {
                for (String serviceName : serviceNames) {
                    LoginProvider providerType = LoginProvider.valueOfCode(serviceName);

                    //except managed
                    if (providerType == LoginProvider.MANAGED) {
                        continue;
                    }

                    startServiceForUser(providerType, rdpUserDO.getUid());
                }
            }
        }
    }

    protected void startServiceForUser(LoginProvider providerType, String primaryUid) {
        if (!this.loginService.checkLoginEnable(primaryUid, providerType)) {
            return;
        }

        LoginProviderSpi service = PluginManager.findSpi(LoginProviderSpi.class, providerType.name());
        if (service == null) {
            return;
        }

        try {
            service.start(primaryUid, new LifeSpiRequest());
        } catch (Exception e) {
            log.error("[RdpSubLoginStarter] SsoService switch " + providerType.name() + "client was failed", e);
        }
    }
}
