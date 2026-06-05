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
package com.clougence.clouddm.api.sidecar.autoexec;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.service.secrules.Requester;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoExecJobDTO {

    private Long                  jobId;
    private Requester             requester;
    private String                uid;
    private List<String>          levels;

    private boolean               jobIsExecByAnother;
    private boolean               jobNotExists;

    private boolean               enableTransactional;
    private ErrorStrategy         errorStrategy;

    private Long                  retryWaitTime;
    private Long                  retryCount;

    private List<AutoExecTaskDTO> taskList = new ArrayList<>();

    private SessionContextDTO     contextDTO;

    private Long                  dsId;
    private DataSourceType        dsType;

}
