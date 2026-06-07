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
package com.clougence.clouddm.console.web.controller.cluster;

import static com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_WORKER_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_WORKER_READ;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.cluster.*;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerDeployConfigVO;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.cluster.WorkerDetector;
import com.clougence.clouddm.console.web.service.cluster.WorkerService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    private WorkerService  workerService;
    @Resource
    private ObjectCacheDao objectCacheDao;
    @Resource
    private WorkerDetector workerDetector;

    @RequestAuth(DM_WORKER_READ)
    @RequestMapping(value = "/listworkers", method = RequestMethod.POST)
    public ResWebData<?> listWorkers(@Valid @RequestBody ListWorkersFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownCluster(puid, fo.getClusterId());

        List<DmSysWorkerDO> workerDOs = this.workerService.listWorkers(fo.getClusterId());
        List<WorkerVO> workerVOs = workerDOs.stream().map(w -> DmConvertUtils.convertToWorkerVO(w, this.workerDetector)).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(workerVOs);
    }

    @RequestAuth(DM_WORKER_MANAGE)
    @RequestMapping(value = "/updateworkerdesc", method = RequestMethod.POST)
    public ResWebData<?> updateWorkerDesc(@Valid @RequestBody UpdateWorkerDescFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownWorker(puid, fo.getWorkerId());

        this.workerService.updateWorkerDesc(fo.getWorkerId(), fo.getDesc());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_WORKER_READ)
    @RequestMapping(value = "/queryworkerbyid", method = RequestMethod.POST)
    public ResWebData<?> queryWorkerById(@Valid @RequestBody QueryWorkerFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownWorker(puid, fo.getWorkerId());

        DmSysWorkerDO workerDO = this.workerService.getWorkerById(fo.getWorkerId());
        WorkerVO workerVO = DmConvertUtils.convertToWorkerVO(workerDO, this.workerDetector);
        return ResWebDataUtils.buildSuccess(workerVO);
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/createinitialworker", method = RequestMethod.POST)
    public ResWebData<?> createInitialWorker(@Valid @RequestBody CreateInitialWorkerFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownCluster(puid, fo.getClusterId());

        this.workerService.createInitialWorker(puid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/deleteworker", method = RequestMethod.POST)
    public ResWebData<?> deleteWorker(@Valid @RequestBody DeleteWorkerFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownWorker(puid, fo.getWorkerId());

        this.workerService.deleteWorker(fo.getWorkerId(), false);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/waittooffline", method = RequestMethod.POST)
    public ResWebData<?> waitToOffline(@Valid @RequestBody WaitToOfflineFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownWorker(puid, fo.getWorkerId());

        this.workerService.updateToWaitToOffline(fo.getWorkerId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/waittoonline", method = RequestMethod.POST)
    public ResWebData<?> waitToOnline(@Valid @RequestBody WaitToOnlineFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownWorker(puid, fo.getWorkerId());

        this.workerService.updateToWaitToOnline(fo.getWorkerId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_WORKER_READ)
    @RequestMapping(value = "/downloadclienturl", method = RequestMethod.POST)
    public ResWebData<?> downLoadClientUrl(@Valid @RequestBody ClientUrlFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownWorker(puid, fo.getWorkerId());

        String downloadUrl = this.workerService.getClientDownloadUrl(fo.getWorkerId());
        return ResWebDataUtils.buildSuccess(downloadUrl);
    }

    @RequestAuth(level = HIGH, value = DM_WORKER_READ)
    @RequestMapping(value = "/clientcoreconfig", method = RequestMethod.POST)
    public ResWebData<?> clientCoreConfig(@Valid @RequestBody ClientCoreConfFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        this.objectCacheDao.ownWorker(puid, fo.getWorkerId());

        WorkerDeployConfigVO coreConfig = this.workerService.getClientDeployCoreConfig(fo.getWorkerId(), puid);
        return ResWebDataUtils.buildSuccess(coreConfig);
    }
}
