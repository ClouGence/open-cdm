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
package com.clougence.clouddm.console.web.service.cluster;

import java.util.List;

import com.clougence.clouddm.platform.dal.model.system.CloudOrIdcName;
import com.clougence.clouddm.console.web.model.fo.cluster.ClusterWithWorkerNetVO;
import com.clougence.clouddm.console.web.model.fo.cluster.CreateClusterFO;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;

/**
 * @author wanshao create time is 2019/12/12 9:35 下午
 **/
public interface ClusterService {

    //long addLocalCluster(CreateClusterFO createClusterFO, String uid);

    long addCluster(String puid, String uid, CreateClusterFO fo);

    void deleteCluster(long clusterId);

    void updateClusterDesc(long clusterId, String desc);

    List<ClusterVO> listByCondition(String ownerUid, String clusterName, String clusterDesc, String region, CloudOrIdcName cloudOrIdcName);

    List<ClusterVO> listByOwnerUid(String ownerUid);

    List<ClusterWithWorkerNetVO> listWithWorkersNet(List<Long> clusterIds);

    ClusterVO queryByClusterId(long clusterId);
}
