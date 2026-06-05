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
package com.clougence.clouddm.console.web.model.vo.faker;

import java.util.Date;

import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskProcessType;
import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2020/4/13
 **/
@Getter
@Setter
public class DmAsyncTaskVO {

    private Long                   id;
    private String                 title;
    private String                 description;
    private String                 bizId;
    private String                 bizType;
    private AsyncTaskProcessType processType;
    private String                 processValue;
    private AsyncTaskStatus      status;
    private String                 statusMsg;
    private Date                   timeOfStart;
    private Date                   timeOfLast;
    private Date                   timeOfFinish;
}
