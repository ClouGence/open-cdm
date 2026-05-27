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
package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.access.entry.*;

/**
 * @author bucketli 2024/2/27 13:19:28
 */
public interface ObjectCacheDao {

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
