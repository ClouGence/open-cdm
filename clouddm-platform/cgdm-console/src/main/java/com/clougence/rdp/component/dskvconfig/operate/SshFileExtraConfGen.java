package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.FileExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.SshFileExtraConfig;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2025/2/27 12:26:49
 */
@Service
@Slf4j
public class SshFileExtraConfGen extends FileExtraConfGen {

    @Override
    public DsExtraConfig newDsExtraConfig() {
        return new SshFileExtraConfig();
    }

    @Override
    protected void validate(RdpDataSourceDO dsDo, FileExtraConfig extraConfig) {
        super.validate(dsDo, extraConfig);

        String dbsJson = extraConfig.getDbsJson();
        if (StringUtils.isBlank(dbsJson)) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " dbsJson can not blank");
        }
    }
}
