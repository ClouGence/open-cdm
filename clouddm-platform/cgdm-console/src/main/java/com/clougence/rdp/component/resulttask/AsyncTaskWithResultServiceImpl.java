package com.clougence.rdp.component.resulttask;

import java.util.Map;
import java.util.concurrent.*;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.utils.ThreadUtils;
import com.clougence.utils.future.CgFuture;
import com.clougence.utils.future.CgFutureObj;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncTaskWithResultServiceImpl implements UnifiedPostConstruct, AsyncTaskWithResultService {

    ThreadPoolExecutor            threadPoolExecutor;

    private Map<String, CgFuture> map;

    @Override
    public void init() throws Exception {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(512);
        ThreadFactory threadFactory = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "Async-result-%s");
        this.threadPoolExecutor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, queue, threadFactory, new ThreadPoolExecutor.AbortPolicy());
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void stop() {

    }

    // if task is running  return the running task,
    public <T> CgFuture<T> submitTask(String key, Callable<T> task) {
        CgFuture<T> cgFuture = map.get(key);
        if (cgFuture != null) {
            return cgFuture;
        }

        CgFutureObj<T> waitObj = new CgFutureObj<>();
        map.put(key, waitObj);
        try {
            this.threadPoolExecutor.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        T result = task.call();
                        waitObj.completed(result);
                    } catch (Exception e) {
                        waitObj.failed(e);
                    } finally {
                        map.remove(key);
                    }
                }
            });
        } catch (Exception e) {
            waitObj.failed(e);
            map.remove(key);
        }
        return waitObj;
    }
}
