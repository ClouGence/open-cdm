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
package com.clougence.clouddm.console.web.global.rsocket;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.comm.ConsoleRSocketServer;
import com.clougence.clouddm.comm.component.impl.RSocketApiManager;
import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/14
 **/
@Slf4j
@Service
public class RSocketServerServiceImpl {

    @Resource
    private ConsoleRSocketServer consoleRSocketServer;
    @Resource
    private ApplicationContext   applicationContext;

    public void init() {
        String appMode = SystemUtils.getSystemProperty("app.mode", "distributed");
        if (StringUtils.equalsIgnoreCase(appMode, "embedded")) {
            log.info("-Dapp.mode=embedded");
        }

        log.info("load RSocket...");

        ClassLoader appClassLoader = this.applicationContext.getClassLoader();
        RSocketApiManager.scanAllApiAndRegister(appClassLoader, "com.clougence.clouddm", applicationContext::getBean);
        consoleRSocketServer.start();

        log.info("Console rsocket server started.");
    }

    @PreDestroy
    public void stopRSocketServer() {
        consoleRSocketServer.stop();
    }
}
