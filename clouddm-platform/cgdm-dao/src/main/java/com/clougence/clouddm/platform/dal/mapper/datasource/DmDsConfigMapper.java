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
package com.clougence.clouddm.platform.dal.mapper.datasource;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.datasource.DataSourceStatus;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.datasource.HostType;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmDsConfigMapper extends BaseMapper<DmDsConfigDO> {

    Long queryEnableDsCount();

    DmDsConfigDO queryById(String ownerUid, long dataSourceId);

    List<DmDsConfigDO> queryByIds(String ownerUid, List<Long> ids);

    List<DmDsConfigDO> queryByUid(String ownerUid);

    void deleteByDisable(String ownerUid, long dataSourceId);

    List<DmDsConfigDO> queryByClusterId(long clusterId);

    void updateStatusByDataSourceId(long dataSourceId, DataSourceStatus status);

    void updateHostTypeById(long id, HostType hostType);

    DmDsConfigDO queryByDataSourceId(long dataSourceId);

    List<DmDsConfigDO> queryByDataSourceIds(List<Long> dataSourceIds);

    void updateDevOps(String ownerUid, long dataSourceId, boolean enableDevops);
}
