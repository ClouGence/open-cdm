package com.clougence.clouddm.console.web.controller.cluster;

import static com.clougence.rdp.constant.auth.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_WORKER_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_WORKER_READ;

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
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.model.fo.cluster.*;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerDeployConfigVO;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerVO;
import com.clougence.clouddm.console.web.service.cluster.WorkerService;
import com.clougence.clouddm.console.web.service.cluster.impl.WorkerDetector;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.service.RdpVerifyService;
import com.clougence.rdp.service.model.CheckVerifyMO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-20 12:05
 * @since 1.1.3
 */
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/worker")
@Slf4j
public class WorkerController {

    @Resource
    private WorkerService           workerService;
    @Resource
    private BizResOwnerCacheService ownerCacheService;
    @Resource
    private RdpVerifyService        rdpVerifyService;
    @Resource
    private WorkerDetector          workerDetector;
    @Resource
    private RdpUserService          rdpUserService;

    @RequestAuth(DM_WORKER_READ)
    @RequestMapping(value = "/listworkers", method = RequestMethod.POST)
    public ResWebData<?> listWorkers(@Valid @RequestBody ListWorkersFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownCluster(puid, fo.getClusterId());

        List<DmWorkerDO> workerDOs = this.workerService.listWorkers(fo.getClusterId());
        List<WorkerVO> workerVOs = workerDOs.stream().map(w -> DmConvertUtils.convertToWorkerVO(w, this.workerDetector)).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(workerVOs);
    }

    @RequestAuth(DM_WORKER_MANAGE)
    @RequestMapping(value = "/updateworkerdesc", method = RequestMethod.POST)
    public ResWebData<?> updateWorkerDesc(@Valid @RequestBody UpdateWorkerDescFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownWorker(puid, fo.getWorkerId());

        this.workerService.updateWorkerDesc(fo.getWorkerId(), fo.getDesc());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_WORKER_READ)
    @RequestMapping(value = "/queryworkerbyid", method = RequestMethod.POST)
    public ResWebData<?> queryWorkerById(@Valid @RequestBody QueryWorkerFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownWorker(puid, fo.getWorkerId());

        DmWorkerDO workerDO = this.workerService.getWorkerById(fo.getWorkerId());
        WorkerVO workerVO = DmConvertUtils.convertToWorkerVO(workerDO, this.workerDetector);
        return ResWebDataUtils.buildSuccess(workerVO);
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/createinitialworker", method = RequestMethod.POST)
    public ResWebData<?> createInitialWorker(@Valid @RequestBody CreateInitialWorkerFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownCluster(puid, fo.getClusterId());

        this.workerService.createInitialWorker(puid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/deleteworker", method = RequestMethod.POST)
    public ResWebData<?> deleteWorker(@Valid @RequestBody DeleteWorkerFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownWorker(puid, fo.getWorkerId());

        this.workerService.deleteWorker(fo.getWorkerId(), false);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/waittooffline", method = RequestMethod.POST)
    public ResWebData<?> waitToOffline(@Valid @RequestBody WaitToOfflineFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownWorker(puid, fo.getWorkerId());

        this.workerService.updateToWaitToOffline(fo.getWorkerId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/waittoonline", method = RequestMethod.POST)
    public ResWebData<?> waitToOnline(@Valid @RequestBody WaitToOnlineFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownWorker(puid, fo.getWorkerId());

        this.workerService.updateToWaitToOnline(fo.getWorkerId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_WORKER_READ)
    @RequestMapping(value = "/downloadclienturl", method = RequestMethod.POST)
    public ResWebData<?> downLoadClientUrl(@Valid @RequestBody ClientUrlFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.ownerCacheService.ownWorker(puid, fo.getWorkerId());

        String downloadUrl = this.workerService.getClientDownloadUrl(fo.getWorkerId());
        return ResWebDataUtils.buildSuccess(downloadUrl);
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_READ)
    @RequestMapping(value = "/clientcoreconfig", method = RequestMethod.POST)
    public ResWebData<?> clientCoreConfig(@Valid @RequestBody ClientCoreConfFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.ownerCacheService.ownWorker(puid, fo.getWorkerId());
        RdpUserDO userDO = this.rdpUserService.getUserByUid(uid);

        CheckVerifyMO verifyData = new CheckVerifyMO();
        verifyData.setUid(uid);
        verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
        verifyData.setPhoneNumber(userDO.getPhone());
        verifyData.setVerifyCodeType(VerifyCodeType.FETCH_WORKER_DEPLOY_CORE_CONFIG);
        verifyData.setVerifyCode(fo.getVerifyCode());
        this.rdpVerifyService.checkVerifyCode(verifyData);

        WorkerDeployConfigVO coreConfig = this.workerService.getClientDeployCoreConfig(fo.getWorkerId(), puid);
        return ResWebDataUtils.buildSuccess(coreConfig);
    }
}
