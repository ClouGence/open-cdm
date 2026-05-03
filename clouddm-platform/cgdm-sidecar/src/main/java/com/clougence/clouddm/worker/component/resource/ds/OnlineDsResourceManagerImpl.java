package com.clougence.clouddm.worker.component.resource.ds;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.worker.component.resource.ExecutesManager;
import com.clougence.clouddm.worker.component.resource.OnlineDsResourceManager;
import com.clougence.clouddm.worker.global.config.DmSidecarConfig;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.utils.timer.HashedWheelTimer;
import com.clougence.utils.timer.Timeout;
import com.clougence.utils.timer.Timer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OnlineDsResourceManagerImpl extends AbstractDsResourceManager implements OnlineDsResourceManager, UnifiedPostConstruct {

    @Resource
    private ApplicationContext  applicationContext;
    @Resource
    private DmSidecarConfig     dmConfig;

    private final AtomicBoolean inited   = new AtomicBoolean(false);
    private Timer               timer    = null;
    private ExecutesManager     executor = null;

    @Override
    public void init() throws Exception {
        if (this.inited.compareAndSet(false, true)) {
            int maxExecutor = this.dmConfig.getOnlineMaxExecutor();
            int maxQueue = this.dmConfig.getOnlineMaxQueue();

            this.timer = new HashedWheelTimer();
            this.executor = new ExecutesManager(Math.max(1, maxExecutor / 10), maxExecutor, maxQueue, 10, applicationContext.getClassLoader());
        }
    }

    @Override
    public void stop() {
        if (this.inited.compareAndSet(true, false)) {
            Set<Timeout> unprocessed = this.timer.stop();
            this.executor.shutdown();
        }
    }

    @Override
    public <C extends DataSourceConfig> Timer getTimer(C dbConfig) {
        return this.timer;
    }

    @Override
    public <C extends DataSourceConfig> Executor getExecutor(C dbConfig) {
        return this.executor.getExecute(dbConfig.getInstanceId());
    }

    @Override
    public boolean isTask() { return false; }

    @Override
    public boolean isReady() { return this.inited.get(); }
}
