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
package com.clougence.clouddm.init.component.fixtasks;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4DmDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.datasource.HostType;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmFixDmDsConfig {
    @Resource
    private DataSourceDal dsDal;

    public void init() {
        List<DmDsConfigDO> dmDsConfigDOS = dsDal.configMapper().selectList(null);
        if (dmDsConfigDOS.isEmpty()) {
            return;
        }

        Map<Long, DmDsConfigDO> dsConfigDOMap = dmDsConfigDOS.stream().collect(Collectors.toMap(DmDsConfigDO::getDataSourceId, dsDO -> dsDO));
        List<Long> dataSourceIdList = dmDsConfigDOS.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());
        Map<Long, DmDsDO> dsMap = dsDal.dsMapper().listByIds(dataSourceIdList).stream().collect(Collectors.toMap(DmDsDO::getId, dsDO -> dsDO));

        List<DmDsConfigKv4DmDO> dsConfList = dsDal.configKv4DmMapper().listByConfigName(ConfigI18nKey.CONFIG_RDB_CONN_HOST_DESCRIPTION.name());
        for (DmDsConfigKv4DmDO dmDsKvBaseConfigDO : dsConfList) {
            Long dataSourceId = dmDsKvBaseConfigDO.getDataSourceId();
            DmDsConfigDO dmDsConfigDO = dsConfigDOMap.get(dataSourceId);
            DmDsDO rdpDataSourceDO = dsMap.get(dataSourceId);
            if (dmDsKvBaseConfigDO.getConfigValue() != null && rdpDataSourceDO != null) {
                if (dmDsKvBaseConfigDO.getConfigValue().equals(rdpDataSourceDO.getPrivateHost())) {
                    dsDal.configMapper().updateHostTypeById(dmDsConfigDO.getId(), HostType.PRIVATE);
                } else {
                    dsDal.configMapper().updateHostTypeById(dmDsConfigDO.getId(), HostType.PUBLIC);
                }
            }
        }

    }
}
