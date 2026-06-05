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
package com.clougence.clouddm.console.web.model.vo.cluster;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/9/17 10:17
 */
@Getter
@Setter
public class WorkerDeployConfigVO {

    private String userAkLabel      = "clouddm.auth.ak";
    private String userAkValue;

    private String userSkLabel      = "clouddm.auth.sk";
    private String userSkValue;

    private String wsnLabel         = "clouddm.worker.wsn";
    private String wsnValue;

    private String consoleHostLabel = "clouddm.console.host";
    private String consoleHostValue;

    private String consolePortLabel = "clouddm.console.port";
    private String consolePortValue;
}
