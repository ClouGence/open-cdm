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
package com.clougence.clouddm.console.web.provider;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.sqlaudit.SqlAuditRService;
import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.console.web.global.notify.ConsoleSqlNotifyService;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/16 11:54
 */
@Slf4j
@Service
@RSocketApiClass
public class SqlAuditRServiceProvider extends AbstractBasicProvider implements SqlAuditRService {

    @Resource
    private ConsoleSqlNotifyService sqlNotifyService;

    @Override
    public void reportSqlAudit(WorkerIdentity identity, Date sendTime, List<SqlExecNotifyDTO> audits) {
        if (!this.checkAccessKey(identity) || CollectionUtils.isEmpty(audits)) {
            return;
        }
        log.info("receive worker auditLog request,date:" + sendTime);
        this.sqlNotifyService.sqlExecNotify(audits, identity.getWorkerSeqNumber());
    }
}
