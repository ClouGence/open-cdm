package com.clougence.clouddm.console.web.component.dsconfig;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.schema.umi.struts.UmiTypes;

public interface DmDsStatusService {

    void handleException(String uid, DataSourceConfig dsConfig, Throwable e);

    void resetStatus(String uid, DataSourceConfig dsConfig);

    void changeStatusIfNecessary(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam);
}
