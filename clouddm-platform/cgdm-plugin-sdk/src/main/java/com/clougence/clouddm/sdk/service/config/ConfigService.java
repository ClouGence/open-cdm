package com.clougence.clouddm.sdk.service.config;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.sdk.service.Service;
import com.clougence.clouddm.sdk.service.secrules.SensitiveConfig;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;

public interface ConfigService extends Service {

    List<ConfigData> fetchSettings(String ownerUid, List<String> names);

    DataSourceConfig fetchDsConfig(long dsId, DataSourceType dsType);

    ToolConfig fetchToolConfig(String toolName);

    Dialect findDialectByDsType(DataSourceType dsType);

    SensitiveConfig fetchSensitiveConfigByDs(long dsId);

    List<UmiTypes> fetchDsLevelDef(DataSourceType dsType);
}
