package com.clougence.clouddm.worker.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.utils.ThreadUtils;
import com.clougence.utils.function.EFunction;

@Service
public class SidecarCacheServiceImpl implements CacheService, UnifiedPostConstruct {

    private final AtomicBoolean inited = new AtomicBoolean(false);
    private final Object        lock   = new Object();
    private Map<String, Object> theCache;

    @Override
    public void init() throws Exception {
        if (inited.compareAndSet(false, true)) {
            if (this.theCache != null) {
                return;
            }

            this.theCache = new LinkedHashMap<>(500);

            ThreadUtils.runDaemonThread(() -> {
                Thread.currentThread().setName("SidecarCacheService-cache-cleanup");
                while (true) {
                    synchronized (this.lock) {
                        theCache.clear();
                    }
                    ThreadUtils.sleep(60 * 1000);
                }
            });
        }
    }

    @Override
    public void stop() {
        if (this.inited.compareAndSet(true, false)) {
            synchronized (this.lock) {
                this.theCache.clear();
            }
        }
    }

    @Override
    public synchronized Object getObject(String key) {
        synchronized (this.lock) {
            return this.theCache.get(key);
        }
    }

    @Override
    public synchronized Object getObjectIfAbsent(String key, EFunction<String, Object, Exception> absent) throws Exception {
        synchronized (this.lock) {
            Object v = this.theCache.get(key);
            if (v == null) {
                v = absent.eApply(key);
                this.theCache.put(key, v);
            }
            return v;
        }
    }

    @Override
    public synchronized Object cacheAndReturn(String key, Object obj) {
        synchronized (this.lock) {
            this.theCache.put(key, obj);
            return obj;
        }
    }

    //    @Override
    //    public Object cacheAndReturn(String key, Object obj, int timeout, TimeUnit timeUnit) {
    //        return obj;
    //    }
}
