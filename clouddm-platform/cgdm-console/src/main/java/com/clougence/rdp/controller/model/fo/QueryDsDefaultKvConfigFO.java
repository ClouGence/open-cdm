package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.dal.enumeration.DeployEnvType;

import lombok.Data;

/**
 * @author bucketli 2021/1/9 15:35
 */
@Data
public class QueryDsDefaultKvConfigFO {

    @NotNull(message = "{notnull.datasourcetype}")
    DataSourceType        dataSourceType;

    private DeployEnvType deployEnvType;
}
