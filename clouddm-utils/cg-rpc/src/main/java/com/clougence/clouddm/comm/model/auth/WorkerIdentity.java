package com.clougence.clouddm.comm.model.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/8/5 09:36
 */
@Getter
@Setter
public class WorkerIdentity {

    private String workerSeqNumber;

    private String accessKey;

    private String localIp;
}
