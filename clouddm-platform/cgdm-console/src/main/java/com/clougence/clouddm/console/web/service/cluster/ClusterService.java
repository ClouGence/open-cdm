package com.clougence.clouddm.console.web.service.cluster;

import java.util.List;

import com.clougence.clouddm.console.web.constants.CloudOrIdcName;
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
