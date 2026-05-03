package com.clougence.rdp.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2019-12-31 11:40
 * @since 1.1.3
 */
@Slf4j
public class RdpThreadPoolFactory {

    public static final int             DEFAULT_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    private static final RdpThreadPoolFactory threadPoolFactory = new RdpThreadPoolFactory();

    public static RdpThreadPoolFactory instance() {
        return threadPoolFactory;
    }

    /**
     * datatask engine thread pool
     * 
     * @return
     */
    public ExecutorService newThreadPool(int threadCount, String name) {
        if (threadCount < 0) {
            threadCount = DEFAULT_THREAD_COUNT;
        }

        return new ThreadPoolExecutor(1,
            threadCount,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new NamedThreadFactory(name, false),
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void shutdownThreadPool(ExecutorService executorService) {
        if (executorService != null) {
            try {
                executorService.shutdown();
                while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    if (executorService.isShutdown() || executorService.isTerminated()) {
                        break;
                    }

                    executorService.shutdown();
                }
            } catch (Exception e) {
                log.error("executor service shutdown error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }
    }

    public void shutdownNowThreadPool(ExecutorService executorService) {
        if (executorService != null) {
            try {
                executorService.shutdownNow();
                while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    if (executorService.isShutdown() || executorService.isTerminated()) {
                        break;
                    }

                    executorService.shutdownNow();
                }
            } catch (Exception e) {
                log.error("executor service shutdown now error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }
    }
}
