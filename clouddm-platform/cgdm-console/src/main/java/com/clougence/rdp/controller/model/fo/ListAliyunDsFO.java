package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Data;

/**
 * @author bucketli 2021/1/30 17:17
 */
@Data
public class ListAliyunDsFO {

    @NotNull(message = "{notnull.region}")
    private String         region;

    @NotNull(message = "{notnull.datasourcetype}")
    private DataSourceType dataSourceType;

    private String         searchKey;
}
