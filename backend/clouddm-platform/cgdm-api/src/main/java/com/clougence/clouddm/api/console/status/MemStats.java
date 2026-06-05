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
public class MemStats {

    private BigDecimal totalMemoryGb    = new BigDecimal(0);
    private BigDecimal usedMemoryGb     = new BigDecimal(0);
    private BigDecimal freeMemoryGb     = new BigDecimal(0);
    private BigDecimal totalMemoryMb    = new BigDecimal(0);
    private BigDecimal usedMemoryMb     = new BigDecimal(0);
    private BigDecimal freeMemoryMb     = new BigDecimal(0);
    private BigDecimal memoryUsage      = new BigDecimal(0);

    private BigDecimal jvmTotalMemoryMb = new BigDecimal(0);
    private BigDecimal jvmUsedMemoryMb  = new BigDecimal(0);
    private BigDecimal jvmFreeMemoryMb  = new BigDecimal(0);
    private BigDecimal jvmMemoryUsage   = new BigDecimal(0);
}
