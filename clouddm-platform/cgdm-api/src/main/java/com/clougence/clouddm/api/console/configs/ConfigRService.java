package com.clougence.clouddm.api.console.configs;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.sdk.service.secrules.SensitiveConfig;
import com.clougence.clouddm.sdk.service.config.ConfigData;

/**
 * @author bucketli 2021/1/16 11:44
 */
@RSocketApiClass
public interface ConfigRService {

    List<ConfigData> fetchSettings(String ownerUid, List<String> names);

    DataSourceConfig fetchDsConfig(long dsId, DataSourceType dsType);

    ToolConfig fetchToolConfig(String toolName);

    SensitiveConfig fetchSensitiveConfigByDs(long dsId);

    byte[] fetchDsFile(String instanceId, ResourceType resourceType, SecurityFileType fileType);
}
