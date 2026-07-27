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

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import com.clougence.clouddm.console.web.component.analysis.BehaviorRelations;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.MetaInformationType;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlAuditDO;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

import jakarta.annotation.Resource;

public abstract class AbstractBehaviorCallBackHandler implements BehaviorCallBackHandler {

    @Resource
    private DataSourceDal dsDal;

    @Override
    public abstract void backfill(DmExecSqlAuditDO audit, List<BehaviorRelation> behaviors);

    protected final List<String> affectedPaths(List<BehaviorRelation> behaviors, TargetType targetType, BehaviorAction... actions) {
        if (behaviors == null || behaviors.isEmpty()) {
            return Collections.emptyList();
        }

        EnumSet<BehaviorAction> actionSet = EnumSet.noneOf(BehaviorAction.class);
        Collections.addAll(actionSet, actions);
        List<String> paths = new ArrayList<>();
        BehaviorRelations.forEach(behaviors, (action, resource) -> {
            if (resource != null && resource.getTargetType() == targetType && actionSet.contains(action)) {
                paths.add(normalizePath(resource.getResourcePath()));
            }
        });
        return paths.stream().distinct().toList();
    }

    protected final void deleteCache(DmExecSqlAuditDO audit, String path, MetaInformationType type) {
        this.dsDal.metaDataMapper().deleteByPath(audit.getDsId(), path, type, audit.getEndTime());
    }

    protected final void deleteCacheTree(DmExecSqlAuditDO audit, String path) {
        this.dsDal.metaDataMapper().deleteByPathLike(audit.getDsId(), path, audit.getEndTime());
    }

    //
    //
    //

    protected final String normalizePath(String path) {
        if (path == null || path.isBlank() || "/".equals(path)) {
            return "/";
        }
        String normalized = path.startsWith("/") ? path : "/" + path;
        while (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    protected final String parentPath(String path) {
        String normalized = normalizePath(path);
        int index = normalized.lastIndexOf('/');
        return index <= 0 ? "/" : normalized.substring(0, index + 1);
    }
}
