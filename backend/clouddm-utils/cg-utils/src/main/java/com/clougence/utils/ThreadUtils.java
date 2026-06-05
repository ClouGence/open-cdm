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
package com.clougence.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @version : 2014年7月8日
 * @author 赵永春 (zyc@hasor.net)
 */
public class ThreadUtils {

    private static final AtomicInteger globalCnt = new AtomicInteger(0);

    /**
     * 挂起当前线程
     * @param timeout  时长
     * @param timeUnit 时长单位
     * @return 中断返回 false，否则true
     */
    public static boolean sleep(Number timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout.longValue());
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    public static void sleep(long interval, TimeUnit timeUnit) {
        if (interval <= 0) {
            return;
        }
        try {
            Thread.sleep(timeUnit.toMillis(interval));
        } catch (Exception ignored) {
        }
    }

    /**
     * 挂起当前线程
     * @param millis  时长
     * @return 中断返回 false，否则true
     */
    public static boolean sleep(Number millis) {
        if (millis == null || millis.longValue() == 0) {
            return true;
        }

        try {
            Thread.sleep(millis.longValue());
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * 考虑{@link Thread#sleep(long)}方法有可能时间不足给定毫秒数，此方法保证sleep时间不小于给定的毫秒数
     * @param millis  时长
     * @return 中断返回 false，否则true
     */
    public static boolean safeSleep(Number millis) {
        long millisLong = millis.longValue();
        long done = 0;
        while (done < millisLong) {
            long before = System.currentTimeMillis();
            if (!sleep(millisLong - done)) {
                return false;
            }
            long after = System.currentTimeMillis();
            done += (after - before);
        }
        return true;
    }

    public static void safeInterrupt(Thread thread) {
        try {
            thread.interrupt();
        } catch (Exception ignored) {
        }
    }

    /** create ExecutorService use nThreads daemon Thread*/
    public static ExecutorService daemonFixedThreadPool(final ClassLoader loader, int nThreads) {
        ThreadFactory threadFactory = threadFactory(loader, "Thread-%s", true);
        return Executors.newFixedThreadPool(nThreads, threadFactory);
    }

    /** create ExecutorService use nThreads front Thread*/
    public static ExecutorService frontFixedThreadPool(final ClassLoader loader, int nThreads) {
        ThreadFactory threadFactory = threadFactory(loader, "Thread-%s", false);
        return Executors.newFixedThreadPool(nThreads, threadFactory);
    }

    public static ThreadFactory daemonThreadFactory(final ClassLoader loader) {
        return threadFactory(loader, "Thread-%s", true);
    }

    public static ThreadFactory daemonThreadFactory(final ClassLoader loader, final String nameTemplate) {
        return threadFactory(loader, nameTemplate, true);
    }

    public static ThreadFactory threadFactory(final ClassLoader loader, final String nameTemplate, final boolean isDaemon) {
        final String template = StringUtils.isBlank(nameTemplate) ? "Thread-%s" : nameTemplate;
        final AtomicInteger cnt = new AtomicInteger(0);
        return run -> {
            Thread t = new Thread(run);
            if (loader != null) {
                t.setContextClassLoader(loader);
            }
            t.setName(String.format(template, cnt.incrementAndGet()));
            t.setDaemon(isDaemon);
            return t;
        };
    }

    /** create front Thread, but not start */
    public static Thread frontThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(String.format("Thread-%s", globalCnt.incrementAndGet()));
        t.setDaemon(false);
        return t;
    }

    /** create front Thread, but not start */
    public static Thread frontThread(ClassLoader loader, Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setContextClassLoader(loader);
        t.setName(String.format("Thread-%s", globalCnt.incrementAndGet()));
        t.setDaemon(false);
        return t;
    }

    /** create daemon Thread, but not start */
    public static Thread daemonThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(String.format("Thread-%s", globalCnt.incrementAndGet()));
        t.setDaemon(true);
        return t;
    }

    /** create daemon Thread, but not start */
    public static Thread daemonThread(ClassLoader loader, Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setContextClassLoader(loader);
        t.setName(String.format("Thread-%s", globalCnt.incrementAndGet()));
        t.setDaemon(true);
        return t;
    }

    /** create front Thread, and start it*/
    public static Thread runFrontThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(String.format("Thread-%s", globalCnt.incrementAndGet()));
        t.setDaemon(false);
        t.start();
        return t;
    }

    /** create front Thread, and start it*/
    public static Thread runFrontThread(ClassLoader loader, Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setContextClassLoader(loader);
        t.setName(String.format("Thread-%s", globalCnt.incrementAndGet()));
        t.setDaemon(false);
        t.start();
        return t;
    }

    /** create daemon Thread, and start it*/
    public static Thread runDaemonThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(String.format("Thread-%s", globalCnt.incrementAndGet()));
        t.setDaemon(true);
        t.start();
        return t;
    }

    /** create daemon Thread, and start it*/
    public static Thread runDaemonThread(ClassLoader loader, Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setContextClassLoader(loader);
        t.setName(String.format("Thread-%s", globalCnt.incrementAndGet()));
        t.setDaemon(true);
        t.start();
        return t;
    }

    public static void shutdownNowExecuteService(ExecutorService executeService, int retryTimes) throws InterruptedException {
        if (executeService != null) {
            executeService.shutdownNow();

            int times = 0;
            while (times < retryTimes) {
                if (executeService.isShutdown() || executeService.isTerminated()) {
                    break;
                }

                if (executeService.awaitTermination(1, TimeUnit.SECONDS)) {
                    executeService.shutdownNow();
                    times++;
                }
            }
        }
    }
}
