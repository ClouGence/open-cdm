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
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiskStats {

    private AtomicLong freeDiskGB  = new AtomicLong(0);

    private AtomicLong usedDiskGB  = new AtomicLong(0);

    private AtomicLong totalDiskGB = new AtomicLong(0);

    private BigDecimal diskUsage   = new BigDecimal(0);

}
