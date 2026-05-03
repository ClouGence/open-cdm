package com.clougence.clouddm.console.web.model.fo.datasource;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.clougence.rdp.dal.enumeration.HostType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/26 16:23
 */
@Getter
@Setter
public class EnableDsQueryFO {

    @Min(value = 1, message = "datasource id must large than 0.")
    private long     dataSourceId;

    @Min(value = 1, message = "cluster id must large than 0.")
    private long     clusterId;

    @NotNull(message = "hostType can not be null.")
    private HostType hostType;
}
