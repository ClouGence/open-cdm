package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Data;

/**
 * @author bucketli 2020/12/30 14:24
 */
@Data
public class ConnectTypeByDsFO {

    @NotNull(message = "{notnull.datasourcetype}")
    private DataSourceType dataSourceType;
}
