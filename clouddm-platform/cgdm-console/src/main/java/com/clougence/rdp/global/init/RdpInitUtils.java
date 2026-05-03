package com.clougence.rdp.global.init;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstructOrder;
import com.clougence.rdp.component.jwtsession.RdpWebUtils;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.util.RdpAuthUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@UnifiedPostConstructOrder(-1)
public class RdpInitUtils implements UnifiedPostConstruct {

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private RdpConsoleConfig   rdpConfig;

    @Override
    public void init() throws Exception {
        RdpAuthUtils.initUtils(this.applicationContext);
        RdpWebUtils.initUtils(this.rdpConfig);
    }

    @Override
    public void stop() {

    }
}
