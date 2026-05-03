package com.clougence.clouddm.console.web.global.rsocket;

import java.util.List;

import com.clougence.clouddm.comm.component.server.RSocketConnManager;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerStatusMapper;
import com.clougence.clouddm.console.web.dal.model.DmWorkerStatusDO;
import com.clougence.utils.HostUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/8 21:16
 */
@Slf4j
public class DmConsoleConnManager implements RSocketConnManager {

    private final DmWorkerStatusMapper workerStatusMapper;

    public DmConsoleConnManager(DmWorkerStatusMapper workerStatusMapper){
        this.workerStatusMapper = workerStatusMapper;
    }

    @Override
    public void resetWorkerStatusInDB() {
        String localIp = HostUtil.getHostIp();
        List<DmWorkerStatusDO> statusDOs = workerStatusMapper.queryByConsoleIpAndStatus(localIp, WorkerConnStatus.CONNECTED);
        for (DmWorkerStatusDO statusDO : statusDOs) {
            log.info("Console started or restarted, try to reset sidecar " + statusDO.getWorkerIp() + "'s connection status to DISCONNECTED...");
            workerStatusMapper.updateStatusById(statusDO.getId(), WorkerConnStatus.DISCONNECTED);
        }
    }
}
