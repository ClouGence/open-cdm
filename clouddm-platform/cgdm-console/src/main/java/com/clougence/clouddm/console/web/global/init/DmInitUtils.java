package com.clougence.clouddm.console.web.global.init;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.service.security.impl.FetchRangeUtils;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.console.web.util.DmTeamUtils;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DmInitUtils implements UnifiedPostConstruct {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        JacksonTypeHandler.setObjectMapper(JsonUtils.defaultObjectMapper());
        MessageUtils.initUtils(this.applicationContext);
        DmDsUtils.initUtils(this.applicationContext);
        DmTeamUtils.initUtils(this.applicationContext);
        FetchRangeUtils.initUtils(this.applicationContext);
    }

    @Override
    public void stop() {

    }
}
