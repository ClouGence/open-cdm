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
package com.clougence.clouddm.console.web.service.ai;

import java.util.concurrent.CancellationException;

public class AiPermit implements AutoCloseable {
    private final Runnable release;
    private boolean        closed;
    private int            references = 1;

    AiPermit(Runnable release){
        this.release = release;
    }

    public synchronized AiPermit retain() {
        if (closed) {
            throw new CancellationException("AI operation released");
        }
        references++;
        return new AiPermit(this::releaseReference);
    }

    private synchronized void releaseReference() {
        references--;
        if (references == 0) {
            release.run();
        }
    }

    @Override
    public synchronized void close() {
        if (!closed) {
            closed = true;
            releaseReference();
        }
    }
}
