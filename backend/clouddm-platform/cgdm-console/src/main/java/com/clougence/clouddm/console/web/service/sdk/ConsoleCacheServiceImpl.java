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
package com.clougence.clouddm.console.web.service.sdk;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.component.cache.LocalCacheComponent;
import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.utils.function.EFunction;

@Primary
@Service("consoleCacheService")
public class ConsoleCacheServiceImpl implements CacheService {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(1);

    public void init() {
    }

    public void stop() {
        LocalCacheComponent.getInstance().clearMemory();
    }

    @Override
    public Object getObject(String key) {
        return LocalCacheComponent.getInstance().getObject(key);
    }

    @Override
    public Object getObjectIfAbsent(String key, EFunction<String, Object, Exception> absent) throws Exception {
        return LocalCacheComponent.getInstance().getObjectIfAbsent(key, DEFAULT_TTL, absent);
    }

    @Override
    public Object cacheAndReturn(String key, Object obj) {
        return LocalCacheComponent.getInstance().cacheAndReturn(key, obj, DEFAULT_TTL);
    }

    @Override
    public Object getObjectIfAbsent(String key, int timeout, TimeUnit timeUnit, EFunction<String, Object, Exception> absent) throws Exception {
        return LocalCacheComponent.getInstance().getObjectIfAbsent(key, Duration.ofMillis(timeUnit.toMillis(timeout)), absent);
    }

    @Override
    public Object cacheAndReturn(String key, Object obj, int timeout, TimeUnit timeUnit) {
        return LocalCacheComponent.getInstance().cacheAndReturn(key, obj, Duration.ofMillis(timeUnit.toMillis(timeout)));
    }
}
