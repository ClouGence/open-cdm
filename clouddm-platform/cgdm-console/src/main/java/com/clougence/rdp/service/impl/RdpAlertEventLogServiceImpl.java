package com.clougence.rdp.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.rdp.dal.mapper.RdpAlertEventLogMapper;
import com.clougence.rdp.dal.model.RdpAlertEventLogDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.service.RdpAlertEventLogService;
import com.clougence.rdp.service.enumeration.AlertEventStatus;
import com.clougence.rdp.service.enumeration.AlertMediaType;
import com.clougence.utils.CollectionUtils;

@Service
public class RdpAlertEventLogServiceImpl implements RdpAlertEventLogService {

    @Resource
    private RdpAlertEventLogMapper rdpAlertEventLogMapper;

    @Resource
    private RdpConsoleConfig       rdpConfig;

    @Override
    public List<RdpAlertEventLogDO> listAlertEventLogs(Long startTimeMillis, Long endTimeMillis, AlertEventStatus status, String uid, long startId, int pageSize) {
        Date startTime = null;
        if (startTimeMillis != null) {
            startTime = new Date(startTimeMillis);
        }

        Date endTime = null;
        if (endTimeMillis != null) {
            endTime = new Date(endTimeMillis);
        }

        return rdpAlertEventLogMapper.queryPageAlertEventLogs(startId, pageSize, startTime, endTime, status, uid);
    }

    @Override
    public void save(AlertEventStatus alertEventStatus, String content, String errMsg, AlertMediaType alertMediaType, List<String> sendUids) {
        RdpAlertEventLogDO alertLogDO = new RdpAlertEventLogDO();
        alertLogDO.setStatus(alertEventStatus);
        alertLogDO.setContent(content);
        alertLogDO.setIp(rdpConfig.getConsolePackageMode().getLocalIpOrHostName());

        alertLogDO.setErrMsg(errMsg);
        alertLogDO.setAlertMediaType(alertMediaType);

        if (CollectionUtils.isNotEmpty(sendUids)) {
            for (String uid : sendUids) {
                alertLogDO.setId(null);
                alertLogDO.setUid(uid);
                rdpAlertEventLogMapper.insert(alertLogDO);
            }
        } else {
            rdpAlertEventLogMapper.insert(alertLogDO);
        }
    }
}
