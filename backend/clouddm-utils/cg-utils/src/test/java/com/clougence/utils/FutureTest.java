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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import com.clougence.utils.future.CgFutureObj;
import org.junit.jupiter.api.Test;

import com.clougence.utils.future.CgFuture;
import com.clougence.utils.future.CgFutureObj;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2022-11-01
 */
public class FutureTest {

    @Test
    public void submitSoTask_Test() throws ExecutionException, InterruptedException {

        int round = 5000;
        boolean result = true;

        while (round > 0) {
            round--;
            AtomicInteger cnt = new AtomicInteger(0);

            CgFuture<Object> future = new CgFutureObj().onCompleted(f -> {
                cnt.incrementAndGet();
            }).onFailed(f -> {
                //
            });

            ThreadUtils.daemonThread(Thread.currentThread().getContextClassLoader(), () -> {
                future.completed(new Object());
            }).start();

            future.get();
            int i = cnt.get();
            if (i != 1) {
                result = false;
                break;
            }
        }

        assert result;
    }
}
