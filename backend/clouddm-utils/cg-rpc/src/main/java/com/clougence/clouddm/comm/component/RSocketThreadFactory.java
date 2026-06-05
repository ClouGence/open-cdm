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
