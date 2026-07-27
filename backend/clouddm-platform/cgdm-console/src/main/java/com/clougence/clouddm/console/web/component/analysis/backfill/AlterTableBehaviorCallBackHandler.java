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
package com.clougence.clouddm.console.web.component.analysis.backfill;

import java.util.List;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.platform.dal.model.datasource.MetaInformationType;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlAuditDO;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

@Component
public class AlterTableBehaviorCallBackHandler extends AbstractBehaviorCallBackHandler {

    @Override
    public void backfill(DmExecSqlAuditDO audit, List<BehaviorRelation> behaviors) {
        for (String tablePath : affectedPaths(behaviors, TargetType.Table, BehaviorAction.ALTER)) {
            deleteCache(audit, tablePath, MetaInformationType.TableDetail);
            deleteCache(audit, tablePath, MetaInformationType.ETable);
        }
    }
}
