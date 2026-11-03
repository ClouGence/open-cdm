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
package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.AiDal;
import com.clougence.clouddm.platform.dal.mapper.ai.*;

import jakarta.annotation.Resource;

@Service
public class AiDalImpl implements AiDal {

    @Resource
    private DmAiSessionMapper        sessionMapper;
    @Resource
    private DmAiHistoryMapper        historyMapper;
    @Resource
    private DmAiHistoryEventMapper   historyEventMapper;
    @Resource
    private DmAiTurnRunMapper        turnRunMapper;
    @Resource
    private DmAiTurnEventMapper      turnEventMapper;
    @Resource
    private DmAiProviderConfigMapper providerConfigMapper;
    @Resource
    private DmAiModelConfigMapper    modelConfigMapper;

    @Override
    public DmAiSessionMapper sessionMapper() {
        return sessionMapper;
    }

    @Override
    public DmAiHistoryMapper historyMapper() {
        return historyMapper;
    }

    @Override
    public DmAiHistoryEventMapper historyEventMapper() {
        return historyEventMapper;
    }

    @Override
    public DmAiTurnRunMapper turnRunMapper() {
        return turnRunMapper;
    }

    @Override
    public DmAiTurnEventMapper turnEventMapper() {
        return turnEventMapper;
    }

    @Override
    public DmAiProviderConfigMapper providerConfigMapper() {
        return providerConfigMapper;
    }

    @Override
    public DmAiModelConfigMapper modelConfigMapper() {
        return modelConfigMapper;
    }
}
