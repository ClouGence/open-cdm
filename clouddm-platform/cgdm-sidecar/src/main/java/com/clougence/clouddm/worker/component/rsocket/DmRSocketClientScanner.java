package com.clougence.clouddm.worker.component.rsocket;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.clougence.clouddm.comm.component.impl.RSocketApiManager;
import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/14
 **/
@Slf4j
@Component()
@DependsOn("dmClientApiScanner")
public class DmRSocketClientScanner {

    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        String appMode = SystemUtils.getSystemProperty("app.mode", "distributed");
        if (StringUtils.equalsIgnoreCase(appMode, "embedded")) {
            log.info("-Dapp.mode=embedded");
            return;
        } else {
            log.info("load RSocket...");
        }

        ClassLoader appClassLoader = this.applicationContext.getClassLoader();
        RSocketApiManager.scanAllApiAndRegister(appClassLoader, "com.clougence.clouddm", applicationContext::getBean);
    }
}
