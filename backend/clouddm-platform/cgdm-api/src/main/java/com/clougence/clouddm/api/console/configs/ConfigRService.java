/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.api.console.configs;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.secrules.SensitiveConfig;

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
