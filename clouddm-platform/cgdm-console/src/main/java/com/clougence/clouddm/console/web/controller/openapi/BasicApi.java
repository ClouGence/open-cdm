package com.clougence.clouddm.console.web.controller.openapi;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2025/12/9 16:38:05
 */
@Slf4j
@Service
public class BasicApi {

    @Resource
    private RdpUserConfigService rdpUserConfigService;

    public boolean isEnableMcp(String puid) {
        RdpUserKvBaseConfigDO configDO = this.rdpUserConfigService.getSpecifiedConfig(puid, UserDefinedConfig.Fields.dmEnableMCP);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return false;
        }
        try {
            return Boolean.parseBoolean(configDO.getConfigValue());
        } catch (Exception e) {
            return false;
        }
    }
}
