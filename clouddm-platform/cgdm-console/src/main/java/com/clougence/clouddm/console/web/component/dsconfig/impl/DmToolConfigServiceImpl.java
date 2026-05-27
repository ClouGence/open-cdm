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
package com.clougence.clouddm.console.web.component.dsconfig.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.base.metadata.ds.tools.FakerPluginConfig;
import com.clougence.clouddm.console.web.component.dsconfig.DmToolConfigService;
import com.clougence.clouddm.platform.dal.model.system.RsSysToolConfObj;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020/11/7 14:27
 */
@Slf4j
@Service
public class DmToolConfigServiceImpl implements DmToolConfigService {

    @Override
    public ToolConfig fetchToolConfig(String toolName) {
        if (StringUtils.equals(toolName, FakerPluginConfig.TOOL_NAME)) {
            FakerPluginConfig config = new FakerPluginConfig();
            config.deserialize();

            config.setExportMaxConcurrent(50);
            config.setOnlineMaxConcurrent(50);
            return config;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<RsSysToolConfObj> fetchDefaultConfig(String toolName) {
        return Collections.emptyList();
    }

    @Override
    public void cleanToolConfig(String toolName) {

    }
}
