package com.clougence.rdp.component.dskvconfig;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;

public interface RdpDsExtraConfGen {

    default DsExtraConfig newDsExtraConfigForDefaultVal(DataSourceType dsType) {
        return this.newDsExtraConfig();
    }

    DsExtraConfig newDsExtraConfig();

    DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos);

    DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs);
}
