package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.mapper.system.*;

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
}
