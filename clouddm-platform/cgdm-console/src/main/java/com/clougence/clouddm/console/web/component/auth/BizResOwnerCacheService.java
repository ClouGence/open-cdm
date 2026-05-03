package com.clougence.clouddm.console.web.component.auth;

import com.clougence.clouddm.console.web.component.auth.model.*;

/**
 * @author bucketli 2024/2/27 13:19:28
 */
public interface BizResOwnerCacheService {

    UserCacheEntry queryByUid(String uid);

    UserCacheEntry queryByUserNumberId(Long id);

    UserCacheEntry queryByAk(String ak);

    DsCacheEntry queryByDsId(Long dsId);

    WorkerCacheEntry queryByWorkerId(Long workerId);

    WorkerCacheEntry queryByWsn(String wsn);

    ClusterCacheEntry queryByClusterId(Long clusterId);

    EnvCacheEntry queryByEnvId(Long envId);

    void ownCluster(String uid, Long clusterId);

    void ownEnv(String uid, Long envId);

    void ownWorker(String uid, Long workerId);

    void ownDataSource(String uid, Long dsId);

    void removeWorkerFromCache(Long workerId);

    void removeClusterFromCache(Long clusterId);

    void removeDataSourceCache(Long dsId);
}
