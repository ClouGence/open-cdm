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

import com.clougence.clouddm.platform.dal.access.SecRuleDal;
import com.clougence.clouddm.platform.dal.mapper.secrule.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecRuleDalImpl implements SecRuleDal {

    @Resource
    private DmSecRangeMapper     rangeMapper;
    @Resource
    private DmSecRefererMapper   refererMapper;
    @Resource
    private DmSecRulesMapper     rulesMapper;
    @Resource
    private DmSecSensitiveMapper sensitiveMapper;
    @Resource
    private DmSecSpecMapper      specMapper;

    @Override
    public DmSecRangeMapper rangeMapper() {
        return rangeMapper;
    }

    @Override
    public DmSecRefererMapper refererMapper() {
        return refererMapper;
    }

    @Override
    public DmSecRulesMapper rulesMapper() {
        return rulesMapper;
    }

    @Override
    public DmSecSensitiveMapper sensitiveMapper() {
        return sensitiveMapper;
    }

    @Override
    public DmSecSpecMapper specMapper() {
        return specMapper;
    }

    // ---------- dal service methods ----------
}
