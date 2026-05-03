package com.clougence.clouddm.console.web.service.envparam;

import java.util.List;

import com.clougence.clouddm.console.web.model.fo.envparam.DmBindEnvParamFO;
import com.clougence.clouddm.console.web.model.fo.envparam.DmUnbindEnvParamFO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamOpenVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamTicketDesVO;
import com.clougence.rdp.dal.model.RdpDsEnvDO;

/**
 * @Author: Ekko
 * @Date: 2024-05-31 10:04
 */
public interface DmEnvParamService {

    void bindEnvParam(String puid, String uid, DmBindEnvParamFO fo);

    void unbindEnvParam(String puid, String uid, DmUnbindEnvParamFO fo);

    List<DmEnvParamOpenVO> listEnvParamOpen(String puid, String uid);

    List<RdpDsEnvDO> queryListByParamKeyValue(String puid, String paramKey, String paramValue);

    String queryParam(String puid, long envID, String paramKey);

    List<RdpDsEnvDO> queryListByParamKey(String puid, String paramKey);

    //
    DmEnvParamTicketDesVO queryAuthTicketInfoParam(String ownerUid, long envId);

    DmEnvParamTicketDesVO querySqlTicketInfoParam(String ownerUid, long envId);

    DmEnvParamTicketDesVO queryChangeTicketInfoParam(String ownerUid, long envId);
}
