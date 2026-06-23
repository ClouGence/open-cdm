package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.mapper.datasource.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataSourceDalImpl implements DataSourceDal {

    @Resource
    private DmDsMapper             dsMapper;
    @Resource
    private DmDsConfigKv4DmMapper  configKv4DmMapper;
    @Resource
    private DmDsMetaDataMapper     metaDataMapper;
    @Resource
    private DmDsTagMapper          tagMapper;

    @Override
    public DmDsMapper dsMapper() {
        return dsMapper;
    }

    public DmDsConfigKv4DmMapper configKv4DmMapper() {
        return configKv4DmMapper;
    }

    @Override
    public DmDsMetaDataMapper metaDataMapper() {
        return metaDataMapper;
    }

    @Override
    public DmDsTagMapper tagMapper() {
        return tagMapper;
    }

    // ---------- dal service methods ----------
}
