package com.clougence.clouddm.worker.component.report;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2019-12-31 11:48
 * @since 1.1.3
 */
@Slf4j
class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger            PoolNumber   = new AtomicInteger();
    private final AtomicInteger                   threadNumber = new AtomicInteger();
    private final ThreadGroup                     group;
    private final String                          namePrefix;
    private final boolean                         isDaemon;
    private final ClassLoader                     classLoader;
    private final Thread.UncaughtExceptionHandler handler      = (t, e) -> log.error(ExceptionUtils.getStackTrace(e));

    public NamedThreadFactory(String prefix, boolean daemon){
        this(prefix, daemon, Thread.currentThread().getContextClassLoader());
    }

    public NamedThreadFactory(String prefix, boolean daemon, ClassLoader classLoader){
        this.group = Thread.currentThread().getThreadGroup();
        this.namePrefix = prefix + "-" + PoolNumber.getAndIncrement() + "-thread-";
        this.isDaemon = daemon;
        this.classLoader = classLoader;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0);
        t.setDaemon(this.isDaemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }

        if (this.classLoader != null) {
            t.setContextClassLoader(this.classLoader);
        }
        t.setUncaughtExceptionHandler(this.handler);
        return t;
    }
}
