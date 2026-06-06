package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.mapper.approval.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalDalImpl implements ApprovalDal {

    @Resource
    private DmApprovalMapper                approvalMapper;
    @Resource
    private DmApprovalPersonMapper          personMapper;
    @Resource
    private DmApprovalProcessActivityMapper activityMapper;
    @Resource
    private DmApprovalProcessMapper         processMapper;
    @Resource
    private DmApprovalTemplateMapper        templateMapper;

    @Override
    public DmApprovalMapper approvalMapper() {
        return approvalMapper;
    }

    @Override
    public DmApprovalPersonMapper personMapper() {
        return personMapper;
    }

    @Override
    public DmApprovalProcessActivityMapper activityMapper() {
        return activityMapper;
    }

    @Override
    public DmApprovalProcessMapper processMapper() {
        return processMapper;
    }

    @Override
    public DmApprovalTemplateMapper templateMapper() {
        return templateMapper;
    }

    // ---------- dal service methods ----------
}
