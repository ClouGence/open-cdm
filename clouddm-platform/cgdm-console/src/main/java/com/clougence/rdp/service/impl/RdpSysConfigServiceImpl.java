package com.clougence.rdp.service.impl;

import static com.clougence.rdp.controller.model.enumeration.SystemConfigEnum.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.rdp.controller.model.enumeration.SystemConfigEnum;
import com.clougence.rdp.controller.model.vo.SystemConfigVO;
import com.clougence.rdp.dal.mapper.RdpSysConfigMapper;
import com.clougence.rdp.dal.model.RdpSysConfigDO;
import com.clougence.rdp.service.RdpSysConfigService;

/**
 * @author wanshao create time is 2020/9/8
 **/
@Service
public class RdpSysConfigServiceImpl implements RdpSysConfigService {

    @Resource
    private RdpSysConfigMapper        rdpSysConfigMapper;

    /**
     * key is config name and value is config description. Config value is null when init. Config default value is decided by properties file
     */
    public static Map<String, String> defaultEmailConfigsMap;

    static {
        defaultEmailConfigsMap = new HashMap<>();
        defaultEmailConfigsMap.put(EMAIL_HOST_KEY.getConfigCode(), "邮箱SMTP服务器地址");
        defaultEmailConfigsMap.put(EMAIL_PORT_KEY.getConfigCode(), "邮箱SMTP服务端口");
        defaultEmailConfigsMap.put(EMAIL_USERNAME_KEY.getConfigCode(), "邮箱用户名");
        defaultEmailConfigsMap.put(EMAIL_PASSWORD_KEY.getConfigCode(), "邮箱密码");
        defaultEmailConfigsMap.put(EMAIL_FROM_KEY.getConfigCode(), "发件人邮箱地址");
    }

    public static Map<String, String> defaultDingDingConfigsMap;
    static {
        defaultDingDingConfigsMap = new HashMap<>();
        defaultDingDingConfigsMap.put(DINGDING_URL_TOKEN_KEY.getConfigCode(), "钉钉群机器人token");
    }

    @Override
    public void initUserSystemEnv(String uid) {
        for (String configName : defaultEmailConfigsMap.keySet()) {
            RdpSysConfigDO sysConf = new RdpSysConfigDO();
            sysConf.setConfigName(configName);
            sysConf.setConfigValue(null);
            sysConf.setDescription(defaultEmailConfigsMap.get(configName));
            sysConf.setUid(uid);
            rdpSysConfigMapper.insert(sysConf);
        }

        for (String configName : defaultDingDingConfigsMap.keySet()) {
            RdpSysConfigDO sysConf = new RdpSysConfigDO();
            sysConf.setConfigName(configName);
            // default use cloudcanal's dingding url
            sysConf.setConfigValue(null);
            sysConf.setDescription(defaultDingDingConfigsMap.get(configName));
            sysConf.setUid(uid);
            rdpSysConfigMapper.insert(sysConf);
        }
    }

    @Override
    public List<SystemConfigVO> list(String uid) {
        List<RdpSysConfigDO> allSystemConfig = rdpSysConfigMapper.queryByUid(uid);
        List<SystemConfigVO> allSystemConfigVOList = new ArrayList<>();

        for (RdpSysConfigDO systemConfigDO : allSystemConfig) {
            SystemConfigVO systemConfigVO = new SystemConfigVO();
            BeanUtils.copyProperties(systemConfigDO, systemConfigVO);
            allSystemConfigVOList.add(systemConfigVO);
        }
        return allSystemConfigVOList;
    }

    @Override
    public List<SystemConfigVO> listGlobalConf() {
        List<RdpSysConfigDO> systemConfigs = rdpSysConfigMapper.queryEmptyUidConf();
        List<SystemConfigVO> configs = new ArrayList<>();

        for (RdpSysConfigDO systemConfigDO : systemConfigs) {
            SystemConfigVO systemConfigVO = new SystemConfigVO();
            BeanUtils.copyProperties(systemConfigDO, systemConfigVO);
            configs.add(systemConfigVO);
        }
        return configs;
    }

    @Override
    public List<SystemConfigVO> listUserMailConfig(String uid) {
        List<RdpSysConfigDO> allSystemConfig = rdpSysConfigMapper.queryByUid(uid);
        List<SystemConfigVO> mailConfigs = new ArrayList<>();
        for (RdpSysConfigDO systemConfigDO : allSystemConfig) {
            if (defaultEmailConfigsMap.containsKey(systemConfigDO.getConfigName())) {
                decryptPasswd(systemConfigDO);
                SystemConfigVO systemConfigVO = new SystemConfigVO();
                BeanUtils.copyProperties(systemConfigDO, systemConfigVO);
                mailConfigs.add(systemConfigVO);
            }
        }
        return mailConfigs;
    }

    @Override
    public List<SystemConfigVO> listUserDingDingConfig(String uid) {
        List<RdpSysConfigDO> allSystemConfig = rdpSysConfigMapper.queryByUid(uid);
        List<SystemConfigVO> dingdingConfigs = new ArrayList<>();
        for (RdpSysConfigDO systemConfigDO : allSystemConfig) {
            if (defaultDingDingConfigsMap.containsKey(systemConfigDO.getConfigName())) {
                SystemConfigVO systemConfigVO = new SystemConfigVO();
                BeanUtils.copyProperties(systemConfigDO, systemConfigVO);
                dingdingConfigs.add(systemConfigVO);
            }
        }
        return dingdingConfigs;
    }

    @Override
    public SystemConfigVO queryConfigByName(String configName, String uid) {
        RdpSysConfigDO configDO = rdpSysConfigMapper.queryByConfigName(configName, uid);
        if (configDO == null) {
            return null;
        }

        decryptPasswd(configDO);
        SystemConfigVO systemConfigVO = new SystemConfigVO();
        BeanUtils.copyProperties(configDO, systemConfigVO);
        return systemConfigVO;
    }

    @Override
    public SystemConfigVO queryOrDefaultConfigByName(SystemConfigEnum configName, String uid, String defaultValue) {
        RdpSysConfigDO configDO = rdpSysConfigMapper.queryByConfigName(configName.getConfigCode(), uid);
        if (configDO == null) {
            SystemConfigVO configVO = new SystemConfigVO();
            configVO.setConfigName(configName.getConfigCode());
            configVO.setConfigValue(defaultValue);
            return configVO;
        }

        if (configDO.getConfigValue() == null) {
            configDO.setConfigValue(defaultValue);
        }

        decryptPasswd(configDO);
        SystemConfigVO systemConfigVO = new SystemConfigVO();
        BeanUtils.copyProperties(configDO, systemConfigVO);
        return systemConfigVO;
    }

    @Override
    public void updateUserSystemConfigs(String uid, List<SystemConfigVO> systemConfigs) {
        for (SystemConfigVO configs : systemConfigs) {
            if (configs.getConfigName().equals(EMAIL_PASSWORD_KEY.getConfigCode())) {
                String encryptPwd = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(configs.getConfigValue());
                configs.setConfigValue(encryptPwd);
            }

            if (configs.getConfigName() != null && configs.getConfigValue() != null) {
                rdpSysConfigMapper.updateUserConfig(uid, configs.getConfigName(), configs.getConfigValue());
            }
        }
    }

    private void decryptPasswd(RdpSysConfigDO systemConfigDO) {
        if (systemConfigDO.getConfigName().equals(EMAIL_PASSWORD_KEY.getConfigCode()) && systemConfigDO.getConfigValue() != null) {
            String decryptPwd = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(systemConfigDO.getConfigValue());
            systemConfigDO.setConfigValue(decryptPwd);
        }
    }
}
