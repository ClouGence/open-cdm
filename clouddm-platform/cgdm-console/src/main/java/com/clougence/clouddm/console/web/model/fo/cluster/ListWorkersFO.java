package com.clougence.clouddm.console.web.model.fo.cluster;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/7 16:58
 */
@Getter
@Setter
public class ListWorkersFO {

    @Min(value = 1, message = "cluster id must be large than 0.")
    private long clusterId;
}
