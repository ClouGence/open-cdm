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
package com.clougence.rdp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.alert.RdpImAlertService;
import com.clougence.clouddm.console.web.component.alert.RdpMailAlertService;
import com.clougence.clouddm.console.web.component.alert.RdpSmsAlertService;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.service.auth.RdpUserConfigService;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpUserAlertService;
import com.clougence.rdp.service.enumeration.AlertImType;
import com.clougence.rdp.service.enumeration.AlertSmsType;
import com.clougence.rdp.service.enumeration.AlertVoiceType;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2023/12/26 14:36:03
 */
@Service
@Slf4j
public class RdpUserAlertServiceImpl implements RdpUserAlertService {

    //    @Autowired
    //    @Qualifier("RdpAliyunSmsAlertService")
    //    @Setter
    //    protected RdpSmsAlertService   rdpAliyunSmsAlertService;
    //
    //    @Autowired
    //    @Qualifier("RdpAliyunVoiceAlertService")
    //    @Setter
    //    protected RdpVoiceAlertService rdpAliyunVoiceAlertService;

    @Autowired
    @Qualifier("RdpAlibabaMailAlertService")
    @Setter
    protected RdpMailAlertService  rdpMailAlertService;

    @Autowired
    @Qualifier("RdpSlackImAlertService")
    @Setter
    protected RdpImAlertService    rdpSlackImAlertService;

    @Autowired
    @Qualifier("RdpDiscordImAlertService")
    @Setter
    protected RdpImAlertService    rdpDiscordImAlertService;

    @Autowired
    @Qualifier("RdpDingTalkImAlertService")
    @Setter
    private RdpImAlertService      rdpDingTalkImAlertService;

    @Autowired
    @Qualifier("RdpWeixinImAlertService")
    @Setter
    private RdpImAlertService      rdpWeixinImAlertService;

    @Autowired
    @Qualifier("RdpFeishuImAlertService")
    @Setter
    private RdpImAlertService      rdpFeishuImAlertService;

    @Autowired
    @Qualifier("RdpCustomAlertService")
    @Setter
    private RdpImAlertService      rdpCustomAlertService;

    @Resource
    @Setter
    protected RdpUserConfigService rdpUserConfigService;

    @Resource
    private DmConsoleConfig        rdpConfig;

    @Override
    public RdpImAlertService chooseImAlertService(String uid) {
        AlertImType alertImType = fetchUserImAlertType(uid);
        return chooseImAlertService(alertImType);
    }

    @Override
    public RdpImAlertService chooseImAlertService(AlertImType alertImType) {
        if (alertImType == null || alertImType == AlertImType.dingtalk) {
            return rdpDingTalkImAlertService;
        } else if (alertImType == AlertImType.weixin) {
            return rdpWeixinImAlertService;
        } else if (alertImType == AlertImType.feishu) {
            return rdpFeishuImAlertService;
        } else if (alertImType == AlertImType.slack) {
            return rdpSlackImAlertService;
        } else if (alertImType == AlertImType.discord) {
            return rdpDiscordImAlertService;
        } else if (alertImType == AlertImType.custom) {
            return rdpCustomAlertService;
        } else {
            throw new IllegalArgumentException("unsupported AlertImType:" + alertImType);
        }
    }

    protected AlertImType fetchUserImAlertType(String uid) {
        if (StringUtils.isBlank(uid)) {
            return rdpConfig.getAlertImType();
        }

        DmSysUserConfDO configDO = rdpUserConfigService.getSpecifiedConfig(uid, UserDefinedConfig.Fields.alertImType);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return rdpConfig.getAlertImType();
        }

        return AlertImType.valueOf(configDO.getConfigValue());
    }

    public boolean fetchUserImAlertAtAll(String uid) {
        if (StringUtils.isBlank(uid)) {
            return false;
        }

        DmSysUserConfDO configDO = rdpUserConfigService.getSpecifiedConfig(uid, UserDefinedConfig.Fields.imAlertAtAll);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return false;
        }

        return Boolean.parseBoolean(configDO.getConfigValue());
    }

    @Override
    public RdpMailAlertService chooseMailAlertService() {
        return rdpMailAlertService;
    }

    @Override
    public RdpSmsAlertService chooseSmsAlertService(String uid) {
        AlertSmsType alertSmsType = fetchUserSmsAlertType(uid);
        //        if (alertSmsType == null || alertSmsType == AlertSmsType.alibaba_cloud) {
        //            return rdpAliyunSmsAlertService;
        //        } else {
        throw new IllegalArgumentException("unsupported AlertSmsType:" + alertSmsType);
        //        }
    }

    protected AlertSmsType fetchUserSmsAlertType(String uid) {
        if (StringUtils.isBlank(uid)) {
            return AlertSmsType.alibaba_cloud;
        }

        DmSysUserConfDO configDO = rdpUserConfigService.getSpecifiedConfig(uid, UserDefinedConfig.Fields.alertSmsType);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return AlertSmsType.alibaba_cloud;
        }

        return AlertSmsType.valueOf(configDO.getConfigValue());
    }

    //    @Override
    //    public RdpVoiceAlertService chooseVoiceAlertService(String uid) {
    //        AlertVoiceType alertVoiceType = fetchUserVoiceAlertType(uid);
    //        if (alertVoiceType == null || alertVoiceType == AlertVoiceType.alibaba_cloud) {
    //            return rdpAliyunVoiceAlertService;
    //        } else {
    //            throw new IllegalArgumentException("unsupported AlertVoiceType:" + alertVoiceType);
    //        }
    //    }

    protected AlertVoiceType fetchUserVoiceAlertType(String uid) {
        if (StringUtils.isBlank(uid)) {
            return AlertVoiceType.alibaba_cloud;
        }

        DmSysUserConfDO configDO = rdpUserConfigService.getSpecifiedConfig(uid, UserDefinedConfig.Fields.alertMobileType);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return AlertVoiceType.alibaba_cloud;
        }

        return AlertVoiceType.valueOf(configDO.getConfigValue());
    }
}
