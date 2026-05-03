package com.clougence.rdp.component.alert;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.rdp.enumeration.AlarmLevel;
import com.clougence.rdp.component.alert.model.SendMsgResult;
import com.clougence.rdp.dal.model.RdpUserDO;

/**
 * @author bucketli 2020-01-30 16:40
 * @since 1.1.3
 */
public interface RdpImAlertService {

    //    String signName = "【CloudCanal】";

    /**
     * use IM to send alert to one or more Chat Group e.g.,WeChat,DingDing and so on...
     * or use custom Url to send alert
     */
    SendMsgResult sendMsg(String signName, String msg, Map<String, Object> msgParams, RdpUserDO owner, List<RdpUserDO> receivers, AlarmLevel alarmLevel, boolean atAll);

    SendMsgResult sendMsgWithWebHook(String webHook, String proxyAddr, String signName, String msg, Map<String, Object> msgParams, RdpUserDO owner, List<RdpUserDO> receivers,
                                     AlarmLevel alarmLevel, boolean atAll);

    SendMsgResult sendMsgToOwner(String signName, String msg, Map<String, Object> msgParams, RdpUserDO owner, AlarmLevel alarmLevel, boolean atAll);

    SendMsgResult sendMsgToSys(String signName, String msg, Map<String, Object> msgParams, AlarmLevel alarmLevel, String uid, boolean atAll);
}
