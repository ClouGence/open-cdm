package com.clougence.rdp.component.alert;

import java.util.List;
import java.util.Map;

import com.clougence.rdp.component.alert.model.SendMsgResult;
import com.clougence.rdp.dal.model.RdpUserDO;

/**
 * @author bucketli 2020-01-31 11:59
 * @since 1.1.3
 */
public interface RdpSmsAlertService {

    /**
     * Use owner config (SMS service ak / sk / code)
     */
    SendMsgResult sendUserMsg(List<RdpUserDO> sendUsers, String msgTemplateCode, Map<String, String> templateParams, String ownerUid);

    /**
     * Use CloudCanal system config (SMS service ak / sk / code...)
     */
    SendMsgResult sendSysMsg(List<String> phones, String msgTemplateCode, Map<String, String> templateParams);
}
