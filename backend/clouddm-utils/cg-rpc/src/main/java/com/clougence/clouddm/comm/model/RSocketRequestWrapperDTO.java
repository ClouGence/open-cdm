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
package com.clougence.clouddm.comm.model;

import java.util.List;

import com.clougence.clouddm.comm.model.auth.WorkerIdentity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/9
 **/
@Getter
@Setter
public class RSocketRequestWrapperDTO {

    /**
     * Related request's api method name
     */
    protected String               apiFullMethodName;

    protected List<String>         paramJsonValues;

    protected String               requestId;

    protected RSocketDirectionType rSocketDirectionType;

    protected WorkerIdentity       workerIdentity;
}
