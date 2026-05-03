package com.clougence.clouddm.comm.component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/11
 **/
@Slf4j
public class RSocketThreadFactory implements ThreadFactory {

    private static final AtomicInteger            poolNumber   = new AtomicInteger();
    private final AtomicInteger                   threadNumber = new AtomicInteger();
    private final ThreadGroup                     group;
    private final String                          namePrefix;
    private final boolean                         isDaemon;
    private final Thread.UncaughtExceptionHandler handler      = (t, e) -> log.error(ExceptionUtils.getStackTrace(e));

    public RSocketThreadFactory(){
        this("pool");
    }

    public RSocketThreadFactory(String prefix){
        this(prefix, false);
    }

    public RSocketThreadFactory(String prefix, boolean daemon){
        this.group = Thread.currentThread().getThreadGroup();
        namePrefix = prefix + "-" + poolNumber.getAndIncrement() + "-thread-";
        isDaemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        t.setDaemon(isDaemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }

        t.setUncaughtExceptionHandler(handler);
        return t;
    }
}
