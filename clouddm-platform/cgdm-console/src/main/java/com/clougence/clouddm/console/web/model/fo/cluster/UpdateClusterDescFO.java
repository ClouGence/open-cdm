package com.clougence.clouddm.console.web.model.fo.cluster;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/2/1 12:35
 */
@Getter
@Setter
public class UpdateClusterDescFO {

    @Min(value = 1, message = "{min.clusterid}")
    private long   clusterId;

    @NotBlank(message = "{notblank.clusterdesc}")
    private String clusterDesc;
}
