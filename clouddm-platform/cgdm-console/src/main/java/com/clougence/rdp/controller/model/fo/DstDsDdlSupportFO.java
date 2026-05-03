package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/12/30 14:24
 */
@Getter
@Setter
public class DstDsDdlSupportFO {

    @NotNull()
    private DataSourceType dataSourceType;
}
