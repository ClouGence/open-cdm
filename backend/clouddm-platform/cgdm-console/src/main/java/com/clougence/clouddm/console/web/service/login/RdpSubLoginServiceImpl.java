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
package com.clougence.clouddm.console.web.service.login;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service()
public class RdpSubLoginServiceImpl implements RdpSubLoginService {
    @Resource
    private SystemDal systemDal;

    @Override
    public boolean checkLoginEnable(String ownerUid, LoginProvider type) {
        String cfgKey = UserDefinedConfig.Fields.subAccountAuthType;
        DmSysUserConfDO configDO = this.systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, cfgKey);
        return configDO != null && StringUtils.equalsIgnoreCase(configDO.getConfigValue().trim(), type.name());
    }
}
