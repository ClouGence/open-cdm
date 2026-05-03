package com.clougence.clouddm.console.web.model.fo.cluster;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/7 19:18
 */
@Getter
@Setter
public class OnOffWorkerAlertFO {

    private boolean phone;

    private boolean email;

    private boolean dingding;

    private boolean sms;

    @Min(value = 1, message = "worker id should be large than 0.")
    private Long    workerId;
}
