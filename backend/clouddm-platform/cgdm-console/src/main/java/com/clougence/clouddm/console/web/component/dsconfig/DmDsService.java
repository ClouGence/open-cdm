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
package com.clougence.clouddm.console.web.component.dsconfig;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.model.fo.datasource.ConnectDsFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.schema.umi.struts.UmiTypes;

/**
 * @author bucketli 2020-01-13 18:08
 * @since 1.1.3
 */
public interface DmDsService {

    DmDsDO fetchAndCheckById(Long dataSourceId);

    DmDsDO fetchByInstanceId(String instanceId);

    String testConnect(String puid, long dsId, long clusterId);

    String testConnect(String uid, ConnectDsFO fo);

    String testConnect(String uid, long clusterId, String driver, DataSourceConfig dsConfig);

    void updateDsTag(long dsId, String uid, String remark);

    void testConnect(String puid, String uid, DsLevels dsLevels);

    void handleException(String uid, DataSourceConfig dsConfig, Throwable e);

    void resetStatus(String uid, DataSourceConfig dsConfig);

    void changeStatusIfNecessary(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam);
}
