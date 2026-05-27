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
    private DmDsBlobResourceMapper blobResourceMapper;
    @Resource
    private DmDsConfigMapper       configMapper;
    @Resource
    private DmDsConfigKv4DmMapper  configKv4DmMapper;
    @Resource
    private DmDsConfigKv4RdpMapper configKv4RdpMapper;
    @Resource
    private DmDsMetaDataMapper     metaDataMapper;
    @Resource
    private DmDsTagMapper          tagMapper;
    @Resource
    private DmDsUsageMapper        usageMapper;

    @Override
    public DmDsMapper dsMapper() {
        return dsMapper;
    }

    @Override
    public DmDsBlobResourceMapper blobResourceMapper() {
        return blobResourceMapper;
    }

    @Override
    public DmDsConfigMapper configMapper() {
        return configMapper;
    }

    @Override
    public DmDsConfigKv4DmMapper configKv4DmMapper() {
        return configKv4DmMapper;
    }

    @Override
    public DmDsConfigKv4RdpMapper configKv4RdpMapper() {
        return configKv4RdpMapper;
    }

    @Override
    public DmDsMetaDataMapper metaDataMapper() {
        return metaDataMapper;
    }

    @Override
    public DmDsTagMapper tagMapper() {
        return tagMapper;
    }

    @Override
    public DmDsUsageMapper usageMapper() {
        return usageMapper;
    }
}
