package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.RedisExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private RdpConsoleConfig rdpConfig;

    @Override
    public RedisExtraConfig newDsExtraConfig() {
        return new RedisExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        RedisExtraConfig config = newDsExtraConfig();

        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        if (rdpConfig.getRdpDsConfigValidateEnable()) {
            validate(config);
        }
        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
        RedisExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(RedisExtraConfig config, String key, String val) {
        if (key.equals(RedisExtraConfig.Fields.isSentinel)) {
            if (StringUtils.isNotBlank(val)) {
                config.setIsSentinel(Boolean.valueOf(val));
            }
        } else if (key.equals(RedisExtraConfig.Fields.sentinelMasterName)) {
            config.setSentinelMasterName(val);
        } else if (key.equals(RedisExtraConfig.Fields.sentinelUser)) {
            config.setSentinelUser(val);
        } else if (key.equals(RedisExtraConfig.Fields.sentinelPassword)) {
            config.setSentinelPassword(val);
        } else if (key.equals(RedisExtraConfig.Fields.useTLS)) {
            config.setUseTLS(Boolean.parseBoolean(val));
        }
    }

    protected void validate(RedisExtraConfig extraConfig) {
        if (extraConfig.getIsSentinel() != null && extraConfig.getIsSentinel()) {
            if (StringUtils.isBlank(extraConfig.getSentinelMasterName())) {
                throw new IllegalArgumentException("DataSource config isSentinel is true, but sentinelMasterName is blank!");
            }
        }
    }
}
