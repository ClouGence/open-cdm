package com.clougence.rdp.component.dskvconfig;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

/**
 * @author bucketli 2023/8/16 14:17:10
 */
public interface RdpDsResourceService {

    RdpDsExtraConfGen getDsExtraConfGen(DataSourceType dsType);
}
