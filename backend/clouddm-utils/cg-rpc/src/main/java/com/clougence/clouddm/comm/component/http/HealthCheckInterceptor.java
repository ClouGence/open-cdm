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
package com.clougence.clouddm.comm.component.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author wanshao create time is 2021/1/21
 **/
public class HealthCheckInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request reequest = chain.request();
        String questUrl = reequest.url().toString();
        boolean isUploadVideoApi = questUrl.endsWith("/healthcheck");
        if (isUploadVideoApi) {
            return chain.withConnectTimeout(1000, TimeUnit.MILLISECONDS)
                .withReadTimeout(1000, TimeUnit.MILLISECONDS)
                .withWriteTimeout(1000, TimeUnit.MILLISECONDS)
                .proceed(reequest);
        }
        return chain.proceed(reequest);
    }
}
