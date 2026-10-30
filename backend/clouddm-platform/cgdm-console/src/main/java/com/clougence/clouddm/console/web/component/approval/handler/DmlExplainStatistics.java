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
package com.clougence.clouddm.console.web.component.approval.handler;

import java.util.concurrent.atomic.AtomicLong;

public final class DmlExplainStatistics {

    private final AtomicLong dmlCount       = new AtomicLong();
    private final AtomicLong cachedCount    = new AtomicLong();
    private final AtomicLong executedCount  = new AtomicLong();
    private final AtomicLong skippedBySize  = new AtomicLong();
    private final AtomicLong skippedByCount = new AtomicLong();
    private final AtomicLong failedCount    = new AtomicLong();

    public long getDmlCount() { return this.dmlCount.get(); }

    public long getCachedCount() { return this.cachedCount.get(); }

    public long getExecutedCount() { return this.executedCount.get(); }

    public long getSkippedBySize() { return this.skippedBySize.get(); }

    public long getSkippedByCount() { return this.skippedByCount.get(); }

    public long getFailedCount() { return this.failedCount.get(); }

    public void incrementDmlCount() {
        this.dmlCount.incrementAndGet();
    }

    public void incrementCachedCount() {
        this.cachedCount.incrementAndGet();
    }

    public void incrementExecutedCount() {
        this.executedCount.incrementAndGet();
    }

    public void incrementSkippedBySize() {
        this.skippedBySize.incrementAndGet();
    }

    public void incrementSkippedByCount() {
        this.skippedByCount.incrementAndGet();
    }

    public void incrementFailedCount() {
        this.failedCount.incrementAndGet();
    }
}
