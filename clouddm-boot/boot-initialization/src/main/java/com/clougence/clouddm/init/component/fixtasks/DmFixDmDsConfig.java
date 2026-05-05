package com.clougence.clouddm.init.component.fixtasks;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.console.web.dal.mapper.DmDsConfigMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmDsKvBaseConfigMapper;
import com.clougence.clouddm.console.web.dal.model.DmDsConfigDO;
import com.clougence.clouddm.console.web.dal.model.DmDsKvBaseConfigDO;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmFixDmDsConfig {

    @Resource
    private RdpDataSourceMapper    rdpDataSourceMapper;
    @Resource
    private DmDsConfigMapper       dmDsConfigMapper;
    @Resource
    private DmDsKvBaseConfigMapper dmDsKvBaseConfigMapper;

    public void init() {
        List<DmDsConfigDO> dmDsConfigDOS = dmDsConfigMapper.selectList(null);
        if (dmDsConfigDOS.isEmpty()) {
            return;
        }

        Map<Long, DmDsConfigDO> dsConfigDOMap = dmDsConfigDOS.stream().collect(Collectors.toMap(DmDsConfigDO::getDataSourceId, dsDO -> dsDO));
        List<Long> dataSourceIdList = dmDsConfigDOS.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());
        Map<Long, RdpDataSourceDO> dsMap = rdpDataSourceMapper.listByIds(dataSourceIdList).stream().collect(Collectors.toMap(RdpDataSourceDO::getId, dsDO -> dsDO));

        List<DmDsKvBaseConfigDO> dsConfList = dmDsKvBaseConfigMapper.listByConfigName(ConfigI18nKey.CONFIG_RDB_CONN_HOST_DESCRIPTION.name());
        for (DmDsKvBaseConfigDO dmDsKvBaseConfigDO : dsConfList) {
            Long dataSourceId = dmDsKvBaseConfigDO.getDataSourceId();
            DmDsConfigDO dmDsConfigDO = dsConfigDOMap.get(dataSourceId);
            RdpDataSourceDO rdpDataSourceDO = dsMap.get(dataSourceId);
            if (dmDsKvBaseConfigDO.getConfigValue() != null && rdpDataSourceDO != null) {
                if (dmDsKvBaseConfigDO.getConfigValue().equals(rdpDataSourceDO.getPrivateHost())) {
                    dmDsConfigMapper.updateHostTypeById(dmDsConfigDO.getId(), HostType.PRIVATE);
                } else {
                    dmDsConfigMapper.updateHostTypeById(dmDsConfigDO.getId(), HostType.PUBLIC);
                }
            }
        }

    }
}
