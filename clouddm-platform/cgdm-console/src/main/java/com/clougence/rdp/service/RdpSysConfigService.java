package com.clougence.rdp.service;

import java.util.List;

import com.clougence.rdp.controller.model.enumeration.SystemConfigEnum;
import com.clougence.rdp.controller.model.vo.SystemConfigVO;

/**
 * @author wanshao create time is 2020/9/8
 **/
public interface RdpSysConfigService {

    /**
     * every user have its own system env, need to init them after register
     */
    void initUserSystemEnv(String uid);

    List<SystemConfigVO> list(String uid);

    List<SystemConfigVO> listGlobalConf();

    List<SystemConfigVO> listUserMailConfig(String uid);

    List<SystemConfigVO> listUserDingDingConfig(String uid);

    SystemConfigVO queryConfigByName(String configName, String uid);

    SystemConfigVO queryOrDefaultConfigByName(SystemConfigEnum configName, String uid, String defaultValue);

    void updateUserSystemConfigs(String uid, List<SystemConfigVO> configs);
}
