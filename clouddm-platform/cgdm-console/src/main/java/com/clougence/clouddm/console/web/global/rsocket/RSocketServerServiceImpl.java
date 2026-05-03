package com.clougence.clouddm.console.web.global.rsocket;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.comm.ConsoleRSocketServer;
import com.clougence.clouddm.comm.component.impl.RSocketApiManager;
import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;

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
