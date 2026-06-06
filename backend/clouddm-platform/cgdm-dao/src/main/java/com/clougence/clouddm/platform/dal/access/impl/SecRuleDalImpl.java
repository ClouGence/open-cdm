package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.SecRuleDal;
import com.clougence.clouddm.platform.dal.mapper.secrule.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecRuleDalImpl implements SecRuleDal {

    @Resource
    private DmSecRangeMapper     rangeMapper;
    @Resource
    private DmSecRefererMapper   refererMapper;
    @Resource
    private DmSecRulesMapper     rulesMapper;
    @Resource
    private DmSecSensitiveMapper sensitiveMapper;
    @Resource
    private DmSecSpecMapper      specMapper;

    @Override
    public DmSecRangeMapper rangeMapper() {
        return rangeMapper;
    }

    @Override
    public DmSecRefererMapper refererMapper() {
        return refererMapper;
    }

    @Override
    public DmSecRulesMapper rulesMapper() {
        return rulesMapper;
    }

    @Override
    public DmSecSensitiveMapper sensitiveMapper() {
        return sensitiveMapper;
    }

    @Override
    public DmSecSpecMapper specMapper() {
        return specMapper;
    }

    // ---------- dal service methods ----------
}
