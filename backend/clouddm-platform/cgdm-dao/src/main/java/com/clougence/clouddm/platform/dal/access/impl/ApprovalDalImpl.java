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

import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.mapper.approval.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalDalImpl implements ApprovalDal {

    @Resource
    private DmApprovalMapper                approvalMapper;
    @Resource
    private DmApprovalPersonMapper          personMapper;
    @Resource
    private DmApprovalProcessActivityMapper activityMapper;
    @Resource
    private DmApprovalProcessMapper         processMapper;
    @Resource
    private DmApprovalTemplateMapper        templateMapper;

    @Override
    public DmApprovalMapper approvalMapper() {
        return approvalMapper;
    }

    @Override
    public DmApprovalPersonMapper personMapper() {
        return personMapper;
    }

    @Override
    public DmApprovalProcessActivityMapper activityMapper() {
        return activityMapper;
    }

    @Override
    public DmApprovalProcessMapper processMapper() {
        return processMapper;
    }

    @Override
    public DmApprovalTemplateMapper templateMapper() {
        return templateMapper;
    }

    // ---------- dal service methods ----------
}
