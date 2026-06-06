package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.mapper.system.*;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemDalImpl implements SystemDal {

    @Resource
    private DmSysClusterMapper   clusterMapper;
    @Resource
    private DmSysConfMapper      confMapper;
    @Resource
    private DmSysEnvMapper       envMapper;
    @Resource
    private DmSysEnvParamMapper  envParamMapper;
    @Resource
    private DmSysMessengerMapper messengerMapper;
    @Resource
    private DmSysUserConfMapper  userConfMapper;
    @Resource
    private DmSysWorkerMapper    workerMapper;
    @Resource
    private AuthDal              authDal;

    @Override
    public DmSysClusterMapper clusterMapper() {
        return clusterMapper;
    }

    @Override
    public DmSysConfMapper confMapper() {
        return confMapper;
    }

    @Override
    public DmSysEnvMapper envMapper() {
        return envMapper;
    }

    @Override
    public DmSysEnvParamMapper envParamMapper() {
        return envParamMapper;
    }

    @Override
    public DmSysMessengerMapper messengerMapper() {
        return messengerMapper;
    }

    @Override
    public DmSysUserConfMapper userConfMapper() {
        return userConfMapper;
    }

    @Override
    public DmSysWorkerMapper workerMapper() {
        return workerMapper;
    }

    // ---------- dal service methods ----------

    @Override
    public DmSysUserConfDO getSpecifiedConfig(String uid, String configName) {
        DmSysUserConfDO configDO = userConfMapper.queryByUidAndConfigName(uid, configName);
        if (configDO != null && configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
            String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
            configDO.setConfigValue(val);
        }
        return configDO;
    }

    @Override
    public String fetchSystemConf(String configName) {
        DmAuthUserDO rootUser = this.authDal.queryRootUser();
        String rootUid = rootUser == null ? AuthDal.ROOT_USER_UID : rootUser.getUid();
        DmSysUserConfDO config = getSpecifiedConfig(rootUid, configName);
        return config == null ? null : config.getConfigValue();
    }
}
