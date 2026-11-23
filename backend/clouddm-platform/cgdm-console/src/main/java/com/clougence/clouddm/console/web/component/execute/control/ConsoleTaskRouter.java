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
package com.clougence.clouddm.console.web.component.execute.control;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.cluster.ConsoleInternalClient;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleInternalAction;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleInternalRequest;
import com.clougence.clouddm.console.web.component.execute.model.ConsoleTaskControlAction;
import com.clougence.clouddm.console.web.component.execute.model.ConsoleTaskControlRequest;
import com.clougence.clouddm.console.web.constants.ConsoleInternalPaths;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysConsoleDO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsoleTaskRouter {
    @Resource
    private ConsoleClusterService cluster;
    @Resource
    private ConsoleInternalClient internalClient;

    public boolean forwardAsyncControl(DmExecAsyncTaskDO task, ConsoleTaskControlAction action, String reasons) {
        if (task == null || this.cluster.getConsoleInstanceId().equals(task.getConsoleInstanceId())) {
            return false;
        }

        DmSysConsoleDO target = this.cluster.findAliveByConsoleInstanceId(task.getConsoleInstanceId());
        if (target == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_OWNER_UNAVAILABLE.name()));
        }

        ConsoleTaskControlRequest request = new ConsoleTaskControlRequest();
        request.setAction(action.name());
        request.setTaskId(task.getId());
        request.setReasons(reasons);
        request.setLocale(DmI18nUtils.getLocale().toLanguageTag());

        try {
            this.internalClient
                .post(target, ConsoleInternalPaths.DISPATCH, new ConsoleInternalRequest(ConsoleInternalAction.CONTROL_TASK, request), System.currentTimeMillis() + 60000);
        } catch (Exception e) {
            log.error("Console task control outcome unknown: action={}, taskId={}, target={}", request.getAction(), request.getTaskId(), target.getInstanceId(), e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_EXECUTION_UNKNOWN.name()));
        }
        return true;
    }
}
