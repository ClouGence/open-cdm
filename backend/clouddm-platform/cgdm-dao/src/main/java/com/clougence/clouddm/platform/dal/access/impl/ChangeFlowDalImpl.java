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

import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.mapper.cicd.*;
import com.clougence.clouddm.platform.dal.mapper.gitops.DmGitOpsScmMapper;

import jakarta.annotation.Resource;

@Service
public class ChangeFlowDalImpl implements ChangeFlowDal {
    @Resource
    private DmChangeFlowMapper           flowMapper;
    @Resource
    private DmChangeFlowItemMapper       flowItemMapper;
    @Resource
    private DmChangeMapper               changeMapper;
    @Resource
    private DmChangeItemMapper           changeItemMapper;
    @Resource
    private DmChangeVersionMapper        versionMapper;
    @Resource
    private DmChangeTriggerReceiptMapper triggerReceiptMapper;
    @Resource
    private DmGitOpsScmMapper            scmMapper;

    @Override
    public DmChangeFlowMapper flowMapper() {
        return flowMapper;
    }

    @Override
    public DmChangeFlowItemMapper flowItemMapper() {
        return flowItemMapper;
    }

    @Override
    public DmChangeMapper changeMapper() {
        return changeMapper;
    }

    @Override
    public DmChangeItemMapper changeItemMapper() {
        return changeItemMapper;
    }

    @Override
    public DmChangeVersionMapper versionMapper() {
        return versionMapper;
    }

    @Override
    public DmChangeTriggerReceiptMapper triggerReceiptMapper() {
        return triggerReceiptMapper;
    }

    @Override
    public DmGitOpsScmMapper scmMapper() {
        return scmMapper;
    }
}
