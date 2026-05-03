package com.clougence.clouddm.console.web.service.system;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.DmEventType;
import com.clougence.clouddm.console.web.dal.model.AlertConfigDetailDO;
import com.clougence.clouddm.console.web.model.fo.cluster.OnOffWorkerAlertFO;
import com.clougence.clouddm.console.web.model.vo.AlertConfigVO;

/**
 * @author wanshao create time is 2020/4/13
 **/
public interface AlertConfigService {

    //    List<AlertConfigVO> listSystemAlertConfig();

    //    void updateByIdAndUid(AlertConfigVO alertConfigVO);

    void onOffWorkerAlert(OnOffWorkerAlertFO configFo, String uid);

    void addAlertConfig(List<AlertConfigDetailDO> alertConfigVOList, DmEventType eventType);

    //    List<AlertConfigDetailDO> getAlertConfigByEventType(DmEventType eventType);

    AlertConfigVO convertToAlertConfigVO(AlertConfigDetailDO alertConfigDetailDO);

    //    AlertConfigVO getWorkerAlertConfig(long workerId, String uid);

    //    AlertConfigDetailDO getWorkerAlertConfigDO(long workerId);

    void deleteByWorkerId(long workerId);
}
