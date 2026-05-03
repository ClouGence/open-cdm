package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.S3FileExtraConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2025/2/27 12:26:49
 */
@Service
@Slf4j
public class S3FileExtraConfGen extends FileExtraConfGen {

    @Override
    public DsExtraConfig newDsExtraConfig() {
        return new S3FileExtraConfig();
    }
}
