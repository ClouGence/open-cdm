package com.clougence.rdp.controller.model.vo;

import java.util.Date;

import com.clougence.rdp.dal.model.RdpAlertEventLogDO;
import com.clougence.rdp.service.enumeration.AlertEventStatus;
import com.clougence.rdp.service.enumeration.AlertMediaType;

import lombok.Data;

/**
 * @author wanshao create time is 2020/4/15
 **/
@Data
public class AlertEventLogVO {

    private long             id;

    private Date             gmtCreate;

    private AlertEventStatus alertEventStatus;

    private AlertMediaType   alertMediaType;

    private String           content;

    private String           errMsg;

    /**
     * console ip
     */
    private String           ip;

    public void convertFromDO(RdpAlertEventLogDO logDO) {
        this.id = logDO.getId();
        this.alertEventStatus = logDO.getStatus();
        this.alertMediaType = logDO.getAlertMediaType();
        this.gmtCreate = logDO.getGmtCreate();
        this.ip = logDO.getIp();
        this.content = logDO.getContent();
        this.errMsg = logDO.getErrMsg();
    }
}
