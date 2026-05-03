package com.clougence.clouddm.console.web.service.cluster.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.console.web.constants.CloudOrIdcName;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.DmClusterMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.model.DmClusterDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.model.fo.cluster.ClusterWithWorkerNetVO;
import com.clougence.clouddm.console.web.model.fo.cluster.CreateClusterFO;
import com.clougence.clouddm.console.web.model.fo.cluster.WorkerNetVO;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;
import com.clougence.clouddm.console.web.service.cluster.ClusterService;
import com.clougence.clouddm.console.web.service.system.NamingService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-20 18:27
 * @since 1.1.3
 */
@Service
@Slf4j
public class ClusterServiceImpl implements ClusterService {

    @Resource
    private DmClusterMapper clusterMapper;
    @Resource
    private DmWorkerMapper  workerMapper;
    @Resource
    private WorkerDetector  workerDetector;
    @Resource
    private NamingService   namingService;
    @Resource
    private RdpUserService  rdpUserService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public long addCluster(String puid, String uid, CreateClusterFO fo) {
        String clusterName = this.namingService.genClusterName();
        DmClusterDO clusterDO = new DmClusterDO();
        clusterDO.setCloudOrIdcName(fo.getCloudOrIdcName());
        clusterDO.setClusterName(clusterName);
        clusterDO.setClusterDesc(fo.getClusterDesc());
        clusterDO.setRegion(fo.getRegion());
        clusterDO.setUid(puid);

        if (StringUtils.isBlank(clusterDO.getClusterDesc())) {
            clusterDO.setClusterDesc(clusterName);
        }

        this.clusterMapper.insert(clusterDO);
        return clusterDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteCluster(long clusterId) {
        List<DmWorkerDO> dos = this.workerMapper.queryByCluster(clusterId);
        if (CollectionUtils.isNotEmpty(dos)) {
            throw new IllegalStateException(DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_DEL_HAVE_WORKS_ERROR.name()));
        }

        this.clusterMapper.deleteById(clusterId);
    }

    @Override
    public void updateClusterDesc(long clusterId, String desc) {
        DmClusterDO clusterDO = new DmClusterDO();
        clusterDO.setId(clusterId);
        clusterDO.setClusterDesc(desc);
        this.clusterMapper.updateClusterDesc(clusterDO);
    }

    @Override
    public List<ClusterVO> listByCondition(String ownerUid, String clusterName, String clusterDesc, String region, CloudOrIdcName cloudOrIdcName) {
        String clusterNameLike = StringUtils.isBlank(clusterName) ? null : clusterName;
        String clusterDescLike = StringUtils.isBlank(clusterDesc) ? null : clusterDesc;

        List<DmClusterDO> clusterDOList = this.clusterMapper.listByCondition(ownerUid, clusterNameLike, cloudOrIdcName, region, clusterDescLike);
        return clusterDOList.stream().map(this::genClusterVoWithOwnerNameAndSummary).collect(Collectors.toList());
    }

    @Override
    public List<ClusterVO> listByOwnerUid(String ownerUid) {
        List<DmClusterDO> clusterDOList = this.clusterMapper.listByCondition(ownerUid, null, null, null, null);
        return clusterDOList.stream().map(this::genClusterVoWithOwnerNameAndSummary).collect(Collectors.toList());
    }

    @Override
    public List<ClusterWithWorkerNetVO> listWithWorkersNet(List<Long> clusterIds) {
        List<DmClusterDO> clusterDOs = this.clusterMapper.listByIds(clusterIds);

        if (CollectionUtils.isEmpty(clusterDOs)) {
            return new ArrayList<>();
        }

        return genClusterWithWorkerNets(clusterDOs);
    }

    @Override
    public ClusterVO queryByClusterId(long clusterId) {
        DmClusterDO clusterDO = this.clusterMapper.selectById(clusterId);
        return this.genClusterVoWithOwnerNameAndSummary(clusterDO);
    }

    protected List<ClusterWithWorkerNetVO> genClusterWithWorkerNets(List<DmClusterDO> clusterDOs) {
        Map<Long, DmClusterDO> clusterIndex = clusterDOs.stream().collect(Collectors.toMap(DmClusterDO::getId, c -> c));
        List<Long> clusterIds = clusterDOs.stream().map(DmClusterDO::getId).collect(Collectors.toList());
        List<DmWorkerDO> workerDOs = this.workerMapper.queryByClusters(clusterIds);
        Map<Long, ClusterWithWorkerNetVO> re = new HashMap<>();

        for (DmWorkerDO workerDO : workerDOs) {
            ClusterWithWorkerNetVO vo = re.get(workerDO.getClusterId());
            if (vo == null) {
                vo = new ClusterWithWorkerNetVO();
                DmClusterDO cd = clusterIndex.get(workerDO.getClusterId());
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

    protected ClusterVO genClusterVoWithSummary(DmClusterDO clusterDO) {
        ClusterVO clusterVO = DmConvertUtils.convertToClusterVO(clusterDO);
        fillWorkerSummary(clusterVO, clusterDO.getId());
        return clusterVO;
    }

    protected ClusterVO genClusterVoWithOwnerNameAndSummary(DmClusterDO clusterDO) {
        ClusterVO clusterVO = genClusterVoWithSummary(clusterDO);
        fillClusterOwner(clusterVO, clusterDO.getUid());
        return clusterVO;
    }

    protected void fillClusterOwner(ClusterVO clusterVO, String ownerUid) {
        if (StringUtils.isNotBlank(ownerUid)) {
            clusterVO.setOwnerName(this.rdpUserService.getUserByUid(ownerUid).getUsername());
        }
    }

    protected void fillWorkerSummary(ClusterVO clusterVO, Long clusterId) {
        List<DmWorkerDO> workerDOs = this.workerMapper.queryByCluster(clusterId);
        if (CollectionUtils.isEmpty(workerDOs)) {
            return;
        }

        int runnintCount = 0;
        int abnormalCount = 0;
        for (DmWorkerDO workerDO : workerDOs) {
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
