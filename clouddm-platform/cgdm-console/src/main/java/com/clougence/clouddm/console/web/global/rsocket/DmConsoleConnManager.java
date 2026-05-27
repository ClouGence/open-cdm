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
package com.clougence.clouddm.console.web.global.rsocket;

import java.util.List;

import com.clougence.clouddm.comm.component.server.RSocketConnManager;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/8 21:16
 */
@Slf4j
public class DmConsoleConnManager implements RSocketConnManager {

    private final SystemDal systemDal;

    public DmConsoleConnManager(SystemDal systemDal){
        this.systemDal = systemDal;
    }

    @Override
    public void resetWorkerStatusInDB() {
        List<DmSysWorkerDO> statusDOs = systemDal.workerMapper().queryByConnStatus(WorkerConnStatus.CONNECTED);
        for (DmSysWorkerDO statusDO : statusDOs) {
            log.info("Console started or restarted, try to reset sidecar " + statusDO.getWorkerIp() + "'s connection status to DISCONNECTED...");
            systemDal.workerMapper().updateWorkerConnStatusById(statusDO.getId(), WorkerConnStatus.DISCONNECTED);
        }
    }
}
