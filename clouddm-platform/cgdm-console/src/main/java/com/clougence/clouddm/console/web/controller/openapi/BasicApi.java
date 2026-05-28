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
package com.clougence.clouddm.console.web.controller.openapi;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.service.auth.RdpUserConfigService;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
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
        DmSysUserConfDO configDO = this.rdpUserConfigService.getSpecifiedConfig(puid, UserDefinedConfig.Fields.dmEnableMCP);
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
