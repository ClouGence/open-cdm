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

import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.mapper.execution.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExecutionDalImpl implements ExecutionDal {

    @Resource
    private DmExecAsyncTaskMapper asyncTaskMapper;
    @Resource
    private DmExecAutoJobMapper   autoJobMapper;
    @Resource
    private DmExecAutoTaskMapper  autoTaskMapper;
    @Resource
    private DmExecFileMapper      fileMapper;
    @Resource
    private DmExecSessionMapper   sessionMapper;
    @Resource
    private DmExecSqlAuditMapper  sqlAuditMapper;
    @Resource
    private DmExecSqlScriptMapper sqlScriptMapper;

    @Override
    public DmExecAsyncTaskMapper asyncTaskMapper() {
        return asyncTaskMapper;
    }

    @Override
    public DmExecAutoJobMapper autoJobMapper() {
        return autoJobMapper;
    }

    @Override
    public DmExecAutoTaskMapper autoTaskMapper() {
        return autoTaskMapper;
    }

    @Override
    public DmExecFileMapper fileMapper() {
        return fileMapper;
    }

    @Override
    public DmExecSessionMapper sessionMapper() {
        return sessionMapper;
    }

    @Override
    public DmExecSqlAuditMapper sqlAuditMapper() {
        return sqlAuditMapper;
    }

    @Override
    public DmExecSqlScriptMapper sqlScriptMapper() {
        return sqlScriptMapper;
    }

    // ---------- dal service methods ----------
}
