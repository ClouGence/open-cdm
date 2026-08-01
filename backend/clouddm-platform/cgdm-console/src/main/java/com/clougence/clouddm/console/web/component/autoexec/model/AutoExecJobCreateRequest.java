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
package com.clougence.clouddm.console.web.component.autoexec.model;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecType;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AutoExecJobCreateRequest {

    private final DsLevels      dsLevels;
    private final SQLJobBizType bizType;
    private final String        bizId;
    private final AutoExecType  execType;
    private final boolean       transactional;
    private final ErrorStrategy errorStrategy;
    private final Long          retryWaitTime;
    private final Long          retryCount;
    private final Long          execTime;
}
