/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.utils.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class I18nUtilsTest {

    @Test
    public void loadResourcesWhileReadingMessages() throws Exception {
        CountDownLatch readingResource = new CountDownLatch(1);
        CountDownLatch resourceRegistered = new CountDownLatch(1);
        ClassLoader resourceLoader = new ClassLoader(null) {
            @Override
            public URL getResource(String name) {
                if ("first_en_US.properties".equals(name)) {
                    readingResource.countDown();
                    try {
                        if (!resourceRegistered.await(5, TimeUnit.SECONDS)) {
                            throw new IllegalStateException("Timed out waiting for resource registration.");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException(e);
                    }
                }
                return null;
            }
        };

        I18nUtils i18nUtils = I18nUtils.initI18n();
        i18nUtils.loadResources(resourceLoader, "first", "second");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<String> message = executor.submit(() -> i18nUtils.getMessage("message.key", null, Locale.US));
            if (!readingResource.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Timed out waiting for resource reading.");
            }

            i18nUtils.loadResources(resourceLoader, "registered-concurrently");
            resourceRegistered.countDown();

            assertEquals("message.key", get(message));
        } finally {
            resourceRegistered.countDown();
            executor.shutdownNow();
        }
    }

    private static String get(Future<String> future) throws Exception {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            }
            throw e;
        }
    }
}
