package com.clougence.clouddm.console.web.controller.cluster;

import static com.clougence.rdp.constant.auth.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_WORKER_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_WORKER_READ;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.model.fo.cluster.*;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;
import com.clougence.clouddm.console.web.service.cluster.ClusterService;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-20 12:04
 * @since 1.1.3
 */
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/cluster")
@Slf4j
public class ClusterController {

    @Resource
    private ClusterService          clusterService;
    @Resource
    private BizResOwnerCacheService ownerCacheService;

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResWebData<?> createCluster(@RequestBody @Valid CreateClusterFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        long clusterId = clusterService.addCluster(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(clusterId);
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> deleteCluster(@RequestBody @Valid DeleteClusterFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownCluster(puid, fo.getClusterId());

        this.clusterService.deleteCluster(fo.getClusterId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_WORKER_MANAGE)
    @RequestMapping(value = "/updatedesc", method = RequestMethod.POST)
    public ResWebData<?> updateClusterDesc(@RequestBody @Valid UpdateClusterDescFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownCluster(puid, fo.getClusterId());

        this.clusterService.updateClusterDesc(fo.getClusterId(), fo.getClusterDesc());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_WORKER_READ)
    @RequestMapping(value = "/listbycondition", method = RequestMethod.POST)
    public ResWebData<?> listByCondition(@RequestBody @Valid ListClustersFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<ClusterVO> vos = this.clusterService.listByCondition(puid, fo.getClusterNameLike(), fo.getClusterDescLike(), fo.getRegion(), fo.getCloudOrIdcName());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_WORKER_READ)
    @RequestMapping(value = "/listwithworkernets", method = RequestMethod.POST)
    public ResWebData<?> listWithWorkerNets(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<ClusterVO> clusterVOs = this.clusterService.listByOwnerUid(puid);
        if (CollectionUtils.isEmpty(clusterVOs)) {
            return ResWebDataUtils.buildSuccess(new ArrayList<>());
        }

        List<Long> clusterIds = clusterVOs.stream().map(ClusterVO::getId).collect(Collectors.toList());
        List<ClusterWithWorkerNetVO> vos = this.clusterService.listWithWorkersNet(clusterIds);
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_WORKER_READ)
    @RequestMapping(value = "/querybyid", method = RequestMethod.POST)
    public ResWebData<?> queryById(@RequestBody @Valid QueryClusterFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownCluster(puid, fo.getClusterId());

        ClusterVO vo = this.clusterService.queryByClusterId(fo.getClusterId());
        return ResWebDataUtils.buildSuccess(vo);
    }
}
