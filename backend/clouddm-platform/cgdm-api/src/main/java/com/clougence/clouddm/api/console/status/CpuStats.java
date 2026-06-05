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
package com.clougence.clouddm.api.console.status;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CpuStats {

    private BigDecimal oneMinuteAvgLoad     = new BigDecimal(0);

    private BigDecimal fiveMinuteAvgLoad    = new BigDecimal(0);

    private BigDecimal fifteenMinuteAvgLoad = new BigDecimal(0);

    private int        physicalCoreCount    = 0;

    private int        logicalCoreCount     = 0;

    private BigDecimal userRatio            = new BigDecimal(0);

    private BigDecimal niceRatio            = new BigDecimal(0);

    private BigDecimal sysRatio             = new BigDecimal(0);

    private BigDecimal idleRatio            = new BigDecimal(0);

    private BigDecimal ioWaitRatio          = new BigDecimal(0);

    private BigDecimal irqRatio             = new BigDecimal(0);

    private BigDecimal softIrqRatio         = new BigDecimal(0);

    private BigDecimal stealRatio           = new BigDecimal(0);

    private BigDecimal avgUsageRatio        = new BigDecimal(0);

    private BigDecimal avgLoadRatio         = new BigDecimal(0);

}
