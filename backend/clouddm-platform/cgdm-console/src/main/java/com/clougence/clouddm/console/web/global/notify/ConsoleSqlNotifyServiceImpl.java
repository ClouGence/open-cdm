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
package com.clougence.clouddm.console.web.global.notify;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.console.web.component.execute.AfterSqlExecuteService;
import com.clougence.clouddm.console.web.service.security.AuditService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsoleSqlNotifyServiceImpl implements ConsoleSqlNotifyService {

    @Resource
    private AfterSqlExecuteService afterSqlExecuteService;
    @Resource
    private AuditService           auditService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void sqlExecNotify(List<SqlExecNotifyDTO> audits, String wsn) {
        auditService.recordAudit(audits, wsn);
        afterSqlExecuteService.handleAfterSqlSuccess(audits);
    }
}
