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
package com.clougence.clouddm.console.web.global.init;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstructOrder;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.service.security.FetchRangeUtils;
import com.clougence.clouddm.console.web.util.*;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@UnifiedPostConstructOrder(-1)
public class DmInitUtils implements UnifiedPostConstruct {

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private DmConsoleConfig    rdpConfig;

    @Override
    public void init() throws Exception {
        JacksonTypeHandler.setObjectMapper(JsonUtils.defaultObjectMapper());

        RdpAuthUtils.initUtils(this.applicationContext);
        RdpWebUtils.initUtils(this.rdpConfig);

        MessageUtils.initUtils(this.applicationContext);
        DmDsUtils.initUtils(this.applicationContext);
        DmTeamUtils.initUtils(this.applicationContext);
        FetchRangeUtils.initUtils(this.applicationContext);
    }

    @Override
    public void stop() {

    }
}
