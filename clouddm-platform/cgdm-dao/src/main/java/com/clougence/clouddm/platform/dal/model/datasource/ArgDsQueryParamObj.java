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
package com.clougence.clouddm.platform.dal.model.datasource;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.datasource.DeployEnvType;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;

import lombok.Builder;
import lombok.Data;

/**
 * @author wanshao create time is 2021/12/13
 **/
@Data
@Builder
public class ArgDsQueryParamObj {

    private DataSourceType dataSourceType;

    private DeployEnvType  deployType;

    private LifeCycleState lifeCycleState;

    private String         dataSourceDescLike;

    private String         dsHostLike;

    private List<Long>     dataSourceIds;

    private String         instanceIdLike;

}
