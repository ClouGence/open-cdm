package com.clougence.clouddm.sdk.service.cache;

import com.clougence.clouddm.sdk.service.Service;
import com.clougence.utils.function.EFunction;

public interface CacheService extends Service {

    Object getObject(String key);

    Object getObjectIfAbsent(String key, EFunction<String, Object, Exception> absent) throws Exception;

    Object cacheAndReturn(String key, Object obj);

    //Object cacheAndReturn(String key, Object obj, int timeout, TimeUnit timeUnit);
}
