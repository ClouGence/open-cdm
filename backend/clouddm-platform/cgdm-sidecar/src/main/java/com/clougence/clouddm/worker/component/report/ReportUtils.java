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
package com.clougence.clouddm.worker.component.report;

import java.io.IOException;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.utils.HostUtil;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Execute shell to get local system's info or using oshi library to collect system stat info
 *
 * @author wanshao create time is 2020/1/14
 **/
@Slf4j
public class ReportUtils {

    public static WorkerIdentity getIdentity() throws IOException {
        ConnAuthDTO authDto = GlobalConfUtils.loadGlobalConf();
        if (StringUtils.isBlank(authDto.getWsn()) || StringUtils.isBlank(authDto.getAk()) || StringUtils.isBlank(authDto.getSk())) {
            throw new IllegalArgumentException("properties in global config (" + authDto + ") are empty.");
        }

        WorkerIdentity identity = new WorkerIdentity();

        identity.setAccessKey(authDto.getAk());
        identity.setWorkerSeqNumber(authDto.getWsn());
        identity.setLocalIp(tryFetchLocalIp());
        return identity;
    }

    public static String tryFetchLocalIp() {
        return HostUtil.getHostIp();
    }

    public static void existSystem() {
        Thread thread = new Thread(() -> {
            log.error("try to exist.");
            System.exit(1);
        });
        thread.start();
    }
}
