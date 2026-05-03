package com.clougence.clouddm.console.web.model.fo.cluster;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-20 13:32
 * @since 1.1.3
 */
@Getter
@Setter
public class WorkerNetVO {

    private Long   id;

    private String privateIp;

    private String publicIp;
}
