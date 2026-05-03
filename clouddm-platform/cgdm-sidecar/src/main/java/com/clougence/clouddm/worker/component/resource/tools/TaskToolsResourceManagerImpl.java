package com.clougence.clouddm.worker.component.resource.tools;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.worker.component.resource.TaskToolResourceManager;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.utils.timer.HashedWheelTimer;
import com.clougence.utils.timer.Timeout;
import com.clougence.utils.timer.Timer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskToolsResourceManagerImpl extends AbstractToolsResourceManager implements TaskToolResourceManager, UnifiedPostConstruct {

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
        return dsConfig.getExportMaxConcurrent();
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
