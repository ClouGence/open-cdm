package com.clougence.rdp.service;

import java.util.List;

import com.clougence.rdp.controller.model.fo.UpdateDsEnvFO;
import com.clougence.rdp.controller.model.lo.UpdateDsEnvLO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsEnvDO;

/**
 * @author wanshao create time is 2021/1/18
 **/
public interface RdpDsEnvService {

    List<RdpDsEnvDO> listDsEnv(String puid, String uid, String match);

    RdpDsEnvDO queryByUserAndId(String puid, String uid, long envID);

    int initPrimaryUserDefaultEnv(String puid, String uid);

    int addEnvDs(String puid, String uid, RdpDsEnvDO dsEnvDO);

    int deleteDsEnv(String puid, String uid, Long dsEnvId);

    UpdateDsEnvLO updateDsEnv(String puid, String uid, UpdateDsEnvFO updateDsEnvFO);

    void fillDsEnvInfo(List<RdpDataSourceDO> dss);

}
