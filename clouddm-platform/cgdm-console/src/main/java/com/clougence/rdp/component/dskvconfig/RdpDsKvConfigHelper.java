package com.clougence.rdp.component.dskvconfig;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;

/**
 * @author bucketli 2020/11/7 18:01
 */
public interface RdpDsKvConfigHelper {

    void fillFieldValue(Object instance, Map<String, String> configMap);

    List<RdpDsKvBaseConfigDO> collectConfigs(Object instance, Long dataSourceId, DataSourceType dsType);
}
