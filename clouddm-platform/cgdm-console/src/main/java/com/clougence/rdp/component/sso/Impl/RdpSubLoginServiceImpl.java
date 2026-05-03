package com.clougence.rdp.component.sso.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.rdp.component.sso.RdpSubLoginService;
import com.clougence.rdp.dal.mapper.RdpUserKvBaseConfigMapper;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service()
public class RdpSubLoginServiceImpl implements RdpSubLoginService {

    @Resource
    private RdpUserKvBaseConfigMapper rdpUserKvBaseConfigMapper;

    @Override
    public boolean checkLoginEnable(String ownerUid, LoginProvider type) {
        String cfgKey = UserDefinedConfig.Fields.subAccountAuthType;
        RdpUserKvBaseConfigDO configDO = this.rdpUserKvBaseConfigMapper.queryByUidAndConfigName(ownerUid, cfgKey);
        return configDO != null && StringUtils.equalsIgnoreCase(configDO.getConfigValue().trim(), type.name());
    }
}
