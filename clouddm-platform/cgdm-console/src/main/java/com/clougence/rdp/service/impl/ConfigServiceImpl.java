package com.clougence.rdp.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.RoleData;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.rdp.dal.mapper.RdpRoleMapper;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpRoleDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.rdp.util.RdpConvertUtils;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode
 * @version 2020-01-17 15:29
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConsoleConfigService {

    @Resource
    private RdpRoleMapper        rdpRoleMapper;
    @Resource
    private RdpUserMapper        rdpUserMapper;
    @Resource
    private RdpUserConfigService rdpUserConfigService;

    @Override
    public List<ConfigData> fetchSettings(String ownerUid, List<String> names) {
        List<RdpUserKvBaseConfigDO> configList = this.rdpUserConfigService.getSpecifiedConfigs(ownerUid, names);
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
        RdpUserDO userDO = this.rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            return null;
        }

        return RdpConvertUtils.convertToRdpUserData(userDO);
    }

    @Override
    public List<RoleData> findRoleByName(String ownerUid, String roleName) {
        List<RdpRoleDO> roles = this.rdpRoleMapper.queryByRoleName(ownerUid, roleName);
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        } else {
            return roles.stream().map(RdpConvertUtils::convertToRdpRoleData).collect(Collectors.toList());
        }
    }
}
