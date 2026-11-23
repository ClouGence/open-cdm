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

import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ObjectQueryDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.utils.SystemUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/16 11:54
 */
@Slf4j
public abstract class AbstractBasicProvider {

    @Resource
    protected ObjectQueryDao objectQueryDao;
    @Resource
    private AuthDal          authDal;
    @Resource
    private SystemDal        systemDal;

    protected boolean checkAccessKey(WorkerIdentity identity) {
        if (identity == null) {
            return false;
        }

        DmSysWorkerDO workerDO = this.systemDal.workerMapper().getByWsn(identity.getWorkerSeqNumber());
        if (workerDO == null) {
            return false;
        }

        // Network ownership is checked by checkIncoming; embedded calls derive the routing identity here.
        if ("embedded".equals(SystemUtils.getSystemProperty("app.mode", "distributed"))) {
            identity.setOwnerConsoleId(workerDO.getOwnerInstanceId());
            identity.setConnectionGeneration(workerDO.getConnectionGeneration());
        }

        DmAuthUserDO userDO = this.authDal.userMapper().queryByAccessKey(identity.getAccessKey());
        if (userDO == null || userDO.isDisable()) {
            log.error("worker ({}) access denied, access key not found", identity.getWorkerSeqNumber());
            return false;
        }

        if (!workerDO.getUid().equals(userDO.getUid())) {
            log.error("worker ({}) does not belong to the authenticated user", identity.getWorkerSeqNumber());
            return false;
        }

        return true;
    }
}
