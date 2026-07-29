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
package com.clougence.clouddm.console.web.component.analysis.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.analysis.BehaviorCallBackService;
import com.clougence.clouddm.console.web.component.analysis.backfill.BehaviorCallBackHandler;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlAuditDO;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.utils.CollectionUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BehaviorCallBackServiceImpl implements BehaviorCallBackService {

    private static final Set<BehaviorAction>    BACKFILL_ACTIONS = EnumSet.of( //
            BehaviorAction.CREATE, BehaviorAction.ALTER, BehaviorAction.DROP);

    @Resource
    private ExecutionDal                        execDal;
    private final List<BehaviorCallBackHandler> handlers;

    public BehaviorCallBackServiceImpl(List<BehaviorCallBackHandler> handlers){
        this.handlers = List.copyOf(handlers);
    }

    @Override
    public void onSuccess(String queryId) {
        DmExecSqlAuditDO audit = this.execDal.sqlAuditMapper().queryByQueryId(queryId);
        if (audit == null) {
            return;
        }

        List<BehaviorRelation> behaviors = audit.getBehaviors();
        if (CollectionUtils.isEmpty(behaviors)) {
            return;
        }

        behaviors = behaviors.stream() //
            .filter(b -> b != null && BACKFILL_ACTIONS.contains(b.getAction()))
            .toList();
        if (behaviors.isEmpty()) {
            return;
        }

        try {
            for (BehaviorCallBackHandler handler : handlers) {
                handler.backfill(audit, behaviors);
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }
}
