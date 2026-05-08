/*
 * Copyright 2008-2009 the original author or authors. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.clougence.clouddm.init;

import java.util.concurrent.ExecutionException;

import com.clougence.utils.future.BasicFuture;

import lombok.extern.slf4j.Slf4j;

/**
 * @version : 2015年11月4日
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class ShutdownHook extends Thread implements Runnable {

    private final BasicFuture<Object> future = new BasicFuture<>();

    public void run() {
        log.info("shutdown form hook.");
        if (!future.isDone()) {
            future.completed(new Object());
        }
    }

    public static void joinShutdown() {
        try {
            ShutdownHook hook = new ShutdownHook();
            Runtime.getRuntime().addShutdownHook(hook);
            hook.future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
