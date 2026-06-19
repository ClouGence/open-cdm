package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.datasource.*;

public interface DataSourceDal {

    DmDsMapper dsMapper();

    DmDsBlobResourceMapper blobResourceMapper();

    DmDsConfigKv4DmMapper configKv4DmMapper();

    DmDsMetaDataMapper metaDataMapper();

    DmDsTagMapper tagMapper();

    DmDsUsageMapper usageMapper();

    // ---------- dal service methods ----------
}
