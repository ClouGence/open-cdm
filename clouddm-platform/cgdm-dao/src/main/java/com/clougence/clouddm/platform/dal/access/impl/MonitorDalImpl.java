package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.mapper.monitor.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MonitorDalImpl implements MonitorDal {

    @Resource
    private DmMonAlertConfigDetailMapper alertConfigDetailMapper;
    @Resource
    private DmMonAlertEventLogMapper      alertEventLogMapper;
    @Resource
    private DmMonBizLogMapper             bizLogMapper;
    @Resource
    private DmMonOpAuditMapper            opAuditMapper;
    @Resource
    private DmMonWebViewLogMapper         webViewLogMapper;

    @Override
    public DmMonAlertConfigDetailMapper alertConfigDetailMapper() {
        return alertConfigDetailMapper;
    }

    @Override
    public DmMonAlertEventLogMapper alertEventLogMapper() {
        return alertEventLogMapper;
    }

    @Override
    public DmMonBizLogMapper bizLogMapper() {
        return bizLogMapper;
    }

    @Override
    public DmMonOpAuditMapper opAuditMapper() {
        return opAuditMapper;
    }

    @Override
    public DmMonWebViewLogMapper webViewLogMapper() {
        return webViewLogMapper;
    }
}
