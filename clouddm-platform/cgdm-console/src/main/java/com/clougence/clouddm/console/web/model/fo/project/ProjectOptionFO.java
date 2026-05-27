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
package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.platform.dal.model.project.ChangeApproveStrategy;
import com.clougence.clouddm.platform.dal.model.project.ChangeCheckStrategy;
import com.clougence.clouddm.platform.dal.model.project.ChangeExecStrategy;
import com.clougence.clouddm.console.web.constants.DmInitScriptStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectOptionFO {

    private DmInitScriptStrategy    initScript;

    // for flow
    private ChangeCheckStrategy   checkStrategy;
    private ChangeApproveStrategy approveStrategy;
    private ChangeExecStrategy    executeStrategy;

    // exec default
    private boolean                 transactional;
    private ErrorStrategy           errorStrategy;
    private Long                    retryWaitTime;
    private Long                    retryCount;
}
