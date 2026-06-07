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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.cluster.ClusterWithWorkerNetVO;
import com.clougence.clouddm.console.web.model.fo.cluster.CreateClusterFO;
import com.clougence.clouddm.console.web.model.fo.cluster.WorkerNetVO;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.CloudOrIdcName;
import com.clougence.clouddm.platform.dal.model.system.DmSysClusterDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-20 18:27
 * @since 1.1.3
 */
@Service
@Slf4j
public class ClusterServiceImpl implements ClusterService {
    private static final String DEFAULT_CLUSTER_REGION = "customer";

    @Resource
    private SystemDal      systemDal;
    @Resource
    private WorkerDetector workerDetector;
    @Resource
    private NamingDao  namingDao;
    @Resource
    private RdpUserService userService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public long addCluster(String puid, String uid, CreateClusterFO fo) {
        String clusterName = this.namingDao.genClusterName();
        DmSysClusterDO clusterDO = new DmSysClusterDO();
        clusterDO.setCloudOrIdcName(fo.getCloudOrIdcName());
        clusterDO.setClusterName(clusterName);
        clusterDO.setClusterDesc(fo.getClusterDesc());
        clusterDO.setRegion(DEFAULT_CLUSTER_REGION);
        clusterDO.setUid(puid);

        if (StringUtils.isBlank(clusterDO.getClusterDesc())) {
            clusterDO.setClusterDesc(clusterName);
        }

        this.systemDal.clusterMapper().insert(clusterDO);
        return clusterDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteCluster(long clusterId) {
        List<DmSysWorkerDO> dos = this.systemDal.workerMapper().queryByCluster(clusterId);
        if (CollectionUtils.isNotEmpty(dos)) {
            throw new IllegalStateException(DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_DEL_HAVE_WORKS_ERROR.name()));
        }

        this.systemDal.clusterMapper().deleteById(clusterId);
    }

    @Override
    public void updateClusterDesc(long clusterId, String desc) {
        DmSysClusterDO clusterDO = new DmSysClusterDO();
        clusterDO.setId(clusterId);
        clusterDO.setClusterDesc(desc);
        this.systemDal.clusterMapper().updateClusterDesc(clusterDO);
    }

    @Override
    public List<ClusterVO> listByCondition(String ownerUid, String clusterName, String clusterDesc, String region, CloudOrIdcName cloudOrIdcName) {
        String clusterNameLike = StringUtils.isBlank(clusterName) ? null : clusterName;
        String clusterDescLike = StringUtils.isBlank(clusterDesc) ? null : clusterDesc;

        List<DmSysClusterDO> clusterDOList = this.systemDal.clusterMapper().listByCondition(ownerUid, clusterNameLike, cloudOrIdcName, region, clusterDescLike);
        return clusterDOList.stream().map(this::genClusterVoWithOwnerNameAndSummary).collect(Collectors.toList());
    }

    @Override
    public List<ClusterVO> listByOwnerUid(String ownerUid) {
        List<DmSysClusterDO> clusterDOList = this.systemDal.clusterMapper().listByCondition(ownerUid, null, null, null, null);
        return clusterDOList.stream().map(this::genClusterVoWithOwnerNameAndSummary).collect(Collectors.toList());
    }

    @Override
    public List<ClusterWithWorkerNetVO> listWithWorkersNet(List<Long> clusterIds) {
        List<DmSysClusterDO> clusterDOs = this.systemDal.clusterMapper().listByIds(clusterIds);

        if (CollectionUtils.isEmpty(clusterDOs)) {
            return new ArrayList<>();
        }

        return genClusterWithWorkerNets(clusterDOs);
    }

    @Override
    public ClusterVO queryByClusterId(long clusterId) {
        DmSysClusterDO clusterDO = this.systemDal.clusterMapper().selectById(clusterId);
        return this.genClusterVoWithOwnerNameAndSummary(clusterDO);
    }

    protected List<ClusterWithWorkerNetVO> genClusterWithWorkerNets(List<DmSysClusterDO> clusterDOs) {
        Map<Long, DmSysClusterDO> clusterIndex = clusterDOs.stream().collect(Collectors.toMap(DmSysClusterDO::getId, c -> c));
        List<Long> clusterIds = clusterDOs.stream().map(DmSysClusterDO::getId).collect(Collectors.toList());
        List<DmSysWorkerDO> workerDOs = this.systemDal.workerMapper().queryByClusters(clusterIds);
        Map<Long, ClusterWithWorkerNetVO> re = new HashMap<>();

        for (DmSysWorkerDO workerDO : workerDOs) {
            ClusterWithWorkerNetVO vo = re.get(workerDO.getClusterId());
            if (vo == null) {
                vo = new ClusterWithWorkerNetVO();
                DmSysClusterDO cd = clusterIndex.get(workerDO.getClusterId());
                vo.setCloudOrIdcName(cd.getCloudOrIdcName());
                vo.setClusterDesc(cd.getClusterDesc());
                vo.setClusterName(cd.getClusterName());
                vo.setGmtCreate(cd.getGmtCreate());
                vo.setGmtModified(cd.getGmtModified());
                vo.setId(cd.getId());
                vo.setRegion(cd.getRegion());
                List<WorkerNetVO> wos = new ArrayList<>();
                vo.setWorkersNet(wos);
                re.put(cd.getId(), vo);
            }

            WorkerNetVO netVO = new WorkerNetVO();
            netVO.setId(workerDO.getId());
            netVO.setPrivateIp(workerDO.getWorkerIp());
            netVO.setPublicIp(workerDO.getExternalIp());
            vo.getWorkersNet().add(netVO);
        }

        return new ArrayList<>(re.values());
    }

    protected ClusterVO genClusterVoWithSummary(DmSysClusterDO clusterDO) {
        ClusterVO clusterVO = DmConvertUtils.convertToClusterVO(clusterDO);
        fillWorkerSummary(clusterVO, clusterDO.getId());
        return clusterVO;
    }

    protected ClusterVO genClusterVoWithOwnerNameAndSummary(DmSysClusterDO clusterDO) {
        ClusterVO clusterVO = genClusterVoWithSummary(clusterDO);
        fillClusterOwner(clusterVO, clusterDO.getUid());
        return clusterVO;
    }

    protected void fillClusterOwner(ClusterVO clusterVO, String ownerUid) {
        if (StringUtils.isNotBlank(ownerUid)) {
            clusterVO.setOwnerName(this.userService.getUserByUid(ownerUid).getUsername());
        }
    }

    protected void fillWorkerSummary(ClusterVO clusterVO, Long clusterId) {
        List<DmSysWorkerDO> workerDOs = this.systemDal.workerMapper().queryByCluster(clusterId);
        if (CollectionUtils.isEmpty(workerDOs)) {
            return;
        }

        int runnintCount = 0;
        int abnormalCount = 0;
        for (DmSysWorkerDO workerDO : workerDOs) {
            if (workerDO.getWorkerState() == WorkerState.ONLINE) {
                if (this.workerDetector.isLooseAlive(workerDO)) {
                    runnintCount++;
                } else {
                    abnormalCount++;
                }
            } else {
                abnormalCount++;
            }
        }

        clusterVO.setWorkerCount(workerDOs.size());
        clusterVO.setRunningCount(runnintCount);
        clusterVO.setAbnormalCount(abnormalCount);
    }
}
