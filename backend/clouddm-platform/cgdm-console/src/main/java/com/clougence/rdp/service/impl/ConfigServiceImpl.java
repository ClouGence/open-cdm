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
package com.clougence.clouddm.console.web.component.config.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.config.UserConfigService;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.RoleData;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode
 * @version 2020-01-17 15:29
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConsoleConfigService {
    @Resource
    private AuthDal           authDal;
    @Resource
    private UserConfigService userConfigService;

    @Override
    public List<ConfigData> fetchSettings(String ownerUid, List<String> names) {
        List<DmSysUserConfDO> configList = this.userConfigService.getSpecifiedConfigs(ownerUid, names);
        if (CollectionUtils.isEmpty(configList)) {
            return Collections.emptyList();
        } else {
            return configList.stream().map(RdpConvertUtils::convertToRdpConfigData).collect(Collectors.toList());
        }
    }

    @Override
    public Map<String, String> fetchSettingsMap(String ownerUid, List<String> names) {
        List<ConfigData> configList = this.fetchSettings(ownerUid, Arrays.asList(//
                UserDefinedConfig.Fields.defaultColumnDisplayChars, //
                UserDefinedConfig.Fields.onlineMaxRecordCount,      //
                UserDefinedConfig.Fields.onlineMaxResultSetMegaByte,//
                UserDefinedConfig.Fields.onlineMaxColumnMegaByte,   //
                UserDefinedConfig.Fields.onlineMaxElementMegaByte)  //
        );
        Map<String, String> configMap = new HashMap<>();
        for (ConfigData c : configList) {
            configMap.put(c.getConfigName(), c.getConfigValue());
        }
        return configMap;
    }

    @Override
    public UserData findUserByUID(String uid) {
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            return null;
        }

        return RdpConvertUtils.convertToRdpUserData(userDO);
    }

    @Override
    public List<RoleData> findRoleByName(String ownerUid, String roleName) {
        List<DmAuthRoleDO> roles = this.authDal.roleMapper().queryByRoleName(ownerUid, roleName);
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        } else {
            return roles.stream().map(RdpConvertUtils::convertToRdpRoleData).collect(Collectors.toList());
        }
    }
}
