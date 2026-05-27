package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.datasource.*;

public interface DataSourceDal {

    DmDsMapper dsMapper();

    DmDsBlobResourceMapper blobResourceMapper();

    DmDsConfigMapper configMapper();

    DmDsConfigKv4DmMapper configKv4DmMapper();

    DmDsConfigKv4RdpMapper configKv4RdpMapper();

    DmDsMetaDataMapper metaDataMapper();

    DmDsTagMapper tagMapper();

    DmDsUsageMapper usageMapper();
}
