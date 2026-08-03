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
<<<<<<<< HEAD:backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/sql/analysis/lineage/LineageContext.java
package com.clougence.clouddm.sdk.sql.analysis.lineage;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Builder;
import lombok.Getter;

/** @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Builder
@Getter
public class LineageContext {
    private long                  dsId;
    private DataSourceConfig      dsConfig;
    private String                userUID;
    private Map<UmiTypes, Object> levelsParam;

========
package com.clougence.clouddm.console.web.component.approval;

import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;

/**
 * Handles one kind of analysis during approval pre-initialization.
 *
 * @author clougence
 */
public interface PreInitHandler {

    boolean supports(PreInitContext context);

    void handle(QueryRequest request, PreInitContext context);
>>>>>>>> feat/sql_engine_optimization:backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/component/approval/PreInitHandler.java
}
