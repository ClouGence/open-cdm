package com.clougence.rdp.service;

import java.util.List;

import com.clougence.rdp.dal.model.RdpAlertEventLogDO;
import com.clougence.rdp.service.enumeration.AlertEventStatus;
import com.clougence.rdp.service.enumeration.AlertMediaType;

/**
 * @author wanshao create time is 2020/4/15
 **/
public interface RdpAlertEventLogService {

    List<RdpAlertEventLogDO> listAlertEventLogs(Long startTimeMillis, Long endTimeMillis, AlertEventStatus status, String uid, long startId, int pageSize);

    void save(AlertEventStatus alertEventStatus, String content, String errMsg, AlertMediaType alertMediaType, List<String> sendUids);
}
