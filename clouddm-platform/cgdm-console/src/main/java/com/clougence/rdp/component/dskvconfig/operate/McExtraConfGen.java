package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.McExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class McExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public McExtraConfig newDsExtraConfig() {
        return new McExtraConfig();
    }

    @Override
    public McExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        McExtraConfig extraConfig = newDsExtraConfig();

        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(extraConfig, f.getConfigName(), f.getConfigValue());
        }

        return extraConfig;
    }

    @Override
    public McExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        McExtraConfig extraConfig = newDsExtraConfig();

        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(extraConfig, f.getConfigName(), f.getConfigValue());
        }

        return extraConfig;
    }

    protected void fillEntry(McExtraConfig config, String key, String val) {
        if (key.equals(McExtraConfig.Fields.sdkEndpoint)) {
            config.setSdkEndpoint(val);
        }
        if (key.equals(McExtraConfig.Fields.schemaStyle)) {
            config.setSchemaStyle(StringUtils.isNotBlank(val) && Boolean.parseBoolean(val));
        }
    }
}
