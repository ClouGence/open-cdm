package com.clougence.clouddm.worker.component.resource.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resource.ToolObject;
import com.clougence.clouddm.sdk.execute.resource.ToolResourceManager;
import com.clougence.clouddm.sdk.execute.tools.ToolFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractToolsResourceManager implements ToolResourceManager {

    private final Map<String, AtomicInteger> resourceCounter = new ConcurrentHashMap<>();
    private final Map<String, Integer>       resourceLimited = new ConcurrentHashMap<>();
    private final Lock                       lock            = new ReentrantLock();

    @Override
    public <C extends ToolConfig> ToolObject requestResource(C dsConfig) {
        try {
            lock.lock();

            String toolName = dsConfig.getToolName();
            String toolVersion = dsConfig.getVersion();

            Integer limit = this.resourceLimited.get(toolName);
            if (limit == null) {
                synchronized (this) {
                    limit = this.resourceLimited.get(toolName);
                    if (limit == null) {
                        limit = getMaxConcurrent(dsConfig);
                        log.info("requestResource '" + toolName + "' limit is " + limit);
                        this.resourceLimited.put(toolName, limit);
                        this.resourceCounter.put(toolName, new AtomicInteger(0));
                    }
                }
            }

            final AtomicInteger counter = this.resourceCounter.get(toolName);
            synchronized (this) {
                if (limit > 0 && counter.get() >= limit) {
                    throw new IndexOutOfBoundsException("the tool '" + toolName + "' usage reached " + counter.get() + "/" + limit);
                } else {
                    counter.incrementAndGet();//++
                }
            }

            try {
                ToolFactory factory = PluginManager.findTools(dsConfig.getToolName());
                ToolObject toolsObj = new ToolObject(dsConfig, factory.create(dsConfig));
                toolsObj.addCloseListener(() -> doClose(toolName, counter));//--
                return toolsObj;
            } catch (Exception e) {
                counter.decrementAndGet();//--
                throw e;
            }
        } finally {
            lock.unlock();
        }
    }

    private void doClose(String dsId, AtomicInteger counter) {
        try {
            lock.lock();
            counter.decrementAndGet();

            if (counter.get() <= 0) {
                this.resourceLimited.remove(dsId);
                this.resourceCounter.remove(dsId);
            }
        } finally {
            lock.unlock();
        }
    }

    protected abstract int getMaxConcurrent(ToolConfig dsConfig);
}
