package com.clougence.clouddm.console.web.model.fo.cluster;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/7 11:48
 */
@Getter
@Setter
public class WaitToOnlineFO {

    @Min(value = 1, message = "worker id must be large than 0.")
    private long workerId;
}
