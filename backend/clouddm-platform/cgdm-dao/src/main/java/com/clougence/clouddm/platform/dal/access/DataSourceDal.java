package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.datasource.DmDsConfigKv4DmMapper;
import com.clougence.clouddm.platform.dal.mapper.datasource.DmDsMapper;
import com.clougence.clouddm.platform.dal.mapper.datasource.DmDsMetaDataMapper;
import com.clougence.clouddm.platform.dal.mapper.datasource.DmDsTagMapper;

public interface DataSourceDal {

    DmDsMapper dsMapper();

    DmDsConfigKv4DmMapper configKv4DmMapper();

    DmDsMetaDataMapper metaDataMapper();

    DmDsTagMapper tagMapper();

    // ---------- dal service methods ----------
}
