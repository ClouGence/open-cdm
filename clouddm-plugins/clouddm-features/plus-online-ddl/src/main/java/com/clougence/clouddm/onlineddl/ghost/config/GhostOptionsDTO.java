package com.clougence.clouddm.sdk.model.onlineddl;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/6/1 16:40:19
 */
@Getter
@Setter
public class GhostOptionsDTO {

    private Boolean          assumeRbr               = true;

    private Boolean          allowOnMaster           = true;

    private Integer          cutOverLockTimeoutSec   = 10;

    private Integer          dmlBatchSize            = 10;

    private Integer          chunkSize               = 3000;

    private Boolean          verbose                 = false;

    private GhostCutOverType cutOverType             = GhostCutOverType.DEFAULT;

    private Boolean          exactRowCount           = false;

    private Boolean          concurrentRowCount      = false;

    private Integer          hbIntervalMs            = 2000;

    private Boolean          timestampOldTable       = true;

    private Boolean          initiallyDropGhostTable = true;

    private Boolean          assumeDsProxyInFront    = true;
}
