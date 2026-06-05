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
package com.clougence.clouddm.console.web.component.autoexec.handler;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelper;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecHelperForTicket implements AutoExecHelper {

    @Override
    public SQLJobBizType getHandleType() { return SQLJobBizType.TICKET; }

    @Override
    public void execStart(SQLJobBizType bizType, String bizId) {

    }

    @Override
    public void execCompleted(SQLJobBizType bizType, String bizId) {

    }

    @Override
    public void execAbort(SQLJobBizType bizType, String bizId) {

    }

    @Override
    public void execFailed(SQLJobBizType bizType, String bizId) {

    }
}
