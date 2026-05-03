package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.dal.enumeration.DeployEnvInfoFetchType;
import com.clougence.rdp.dal.enumeration.DeployEnvType;

import lombok.Data;

/**
 * @author bucketli 2020/12/30 14:24
 */
@Data
public class DsSecurityOptionFO {

    @NotNull(message = "{notnull.deployenvtype}")
    private DeployEnvType          deployEnvType;

    @NotNull(message = "{notnull.datasourcetype}")
    private DataSourceType         dataSourceType;

    private DeployEnvInfoFetchType deployFetchType;
}
