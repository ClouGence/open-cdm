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
            this.threadPoolExecutor.submit(() -> {
                try {
                    T result = task.call();
                    waitObj.completed(result);
                } catch (Exception e) {
                    waitObj.failed(e);
                } finally {
                    map.remove(key);
                }
            });
        } catch (Exception e) {
            waitObj.failed(e);
            map.remove(key);
        }
        return waitObj;
    }
}
