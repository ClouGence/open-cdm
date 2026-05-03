package com.clougence.clouddm.console.web.provider;

import javax.annotation.Resource;

import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.UserCacheEntry;
import com.clougence.clouddm.console.web.component.auth.model.WorkerCacheEntry;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/16 11:54
 */
@Slf4j
public abstract class AbstractBasicProvider {

    @Resource
    protected BizResOwnerCacheService ownerCacheService;

    protected boolean checkAccessKey(WorkerIdentity identity) {
        if (identity == null) {
            return false;
        }

        WorkerCacheEntry workerDO = this.ownerCacheService.queryByWsn(identity.getWorkerSeqNumber());
        if (workerDO == null) {
            return false;
        }

        UserCacheEntry userDO = this.ownerCacheService.queryByAk(identity.getAccessKey());
        if (!workerDO.getOwnerUid().equals(userDO.getUid())) {
            log.error("worker (" + identity.getWorkerSeqNumber() + ") not belone user (" + identity.getAccessKey() + ")");
            return false;
        }

        return true;
    }
}
