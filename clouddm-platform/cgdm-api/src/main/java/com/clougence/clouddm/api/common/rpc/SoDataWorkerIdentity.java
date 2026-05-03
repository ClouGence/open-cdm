package com.clougence.clouddm.api.common.rpc;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author bucketli 2020/8/5 09:36
 */
@Data
@Builder
public class SoDataWorkerIdentity {

    @Tolerate
    public SoDataWorkerIdentity(){
    }

    private String wsn;

    private String accessKey;

    private String localIp;
}
