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
package com.clougence.clouddm.worker.component.resource.tools;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.worker.component.resource.OnlineToolResourceManager;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.utils.timer.HashedWheelTimer;
import com.clougence.utils.timer.Timeout;
import com.clougence.utils.timer.Timer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OnlineToolsResourceManagerImpl extends AbstractToolsResourceManager implements OnlineToolResourceManager, UnifiedPostConstruct {

    private final AtomicBoolean inited = new AtomicBoolean(false);
    private Timer               timer  = null;

    @Override
    public void init() throws Exception {
        if (this.inited.compareAndSet(false, true)) {
            this.timer = new HashedWheelTimer();
        }
    }

    @Override
    public void stop() {
        if (this.inited.compareAndSet(true, false)) {
            Set<Timeout> unprocessed = this.timer.stop();
        }
    }

    @Override
    protected int getMaxConcurrent(ToolConfig dsConfig) {
        return dsConfig.getOnlineMaxConcurrent();
    }

    @Override
    public <C extends ToolConfig> Timer getTimer(C dbConfig) {
        return this.timer;
    }

    @Override
    public boolean isBackground() { return true; }

    @Override
    public boolean isReady() { return this.inited.get(); }
}
