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
package com.clougence.clouddm.console.web.component.asyntask;

import java.io.Serializable;
import java.util.List;

import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskProcessType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2019-12-30 15:53
 * @since 1.1.3
 */
@Getter
@Setter
public class AsyncTaskConfig implements Serializable {

    private static final long     serialVersionUID = -7166362823815428900L;

    private String                title;
    private String                description;

    private String                bizId;
    private String                bizType;

    private Class<?>              handlerType;
    private AsyncTaskProcessType  processType;
    private String                configData;

    private int                   delayActivate;
    private Boolean               parallel;
    private boolean               showInDock;
    private boolean               fastFail;
    private List<AsyncTaskConfig> subTask;
}
