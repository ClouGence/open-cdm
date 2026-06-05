/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
