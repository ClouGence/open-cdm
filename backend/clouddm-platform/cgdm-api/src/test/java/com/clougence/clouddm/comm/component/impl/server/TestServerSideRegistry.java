package com.clougence.clouddm.comm.component.impl.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;

import com.clougence.clouddm.comm.component.impl.TestWorkerStatusMapper;
import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.rsocket.RSocketRegisterInfo;
import com.clougence.clouddm.model.TestWorkerStatusDO;
import com.clougence.utils.HostUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Server use this to register client's info
 *
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class TestServerSideRegistry implements ServerSideRegistry {

    private TestWorkerStatusMapper testWorkerStatusMapper;
    private ObjectMapper           objectMapper = new ObjectMapper();

    @Autowired
    public TestServerSideRegistry(TestWorkerStatusMapper testWorkerStatusMapper){
        this.testWorkerStatusMapper = testWorkerStatusMapper;
    }

    /**
     * key is wsn. Every wsn related to a unique user sidecar machine. todo need add metric here later
     */
    private final Map<String, RSocketRequester> workerRequesterMap = new ConcurrentHashMap<>();

    @Override
    public void unRegister(String workerSeqNumber) {
        log.warn("[TEST] begin to unregister worker with wsn: " + workerSeqNumber);
        // unregister
        testWorkerStatusMapper.getTestWorkerStatusDOMap().remove(workerSeqNumber);
    }

    @SneakyThrows
    @Override
    public void register(RSocketRegisterInfo registerInfo) {
        log.warn("[TEST] Begin to register worker. Received register info is :" + registerInfo);
        workerRequesterMap.put(registerInfo.getWorkerSeqNumber(), registerInfo.getRequester());

        // persist worker status
        TestWorkerStatusDO statusDO = new TestWorkerStatusDO();
        statusDO.setConsoleIp(HostUtil.getHostIp());
        statusDO.setWorkerConnStatus(WorkerConnStatus.CONNECTED);
        statusDO.setSidecarIp(registerInfo.getWorkerIp());
        statusDO.setWorkerSeqNumber(registerInfo.getWorkerSeqNumber());
        testWorkerStatusMapper.getTestWorkerStatusDOMap().put(registerInfo.getWorkerSeqNumber(), statusDO);
        log.info("register successfully for worker (" + registerInfo.getWorkerSeqNumber() + "," + registerInfo.getWorkerIp() + ")");
        log.info("Persisted worker status info is " + objectMapper.writeValueAsString(statusDO));
    }

    @Override
    public void checkAuth(ConnAuthDTO authInfo) {

    }

    @Override
    public RSocketRequester getRSocketRequester(String workerSeqNumber) {
        return workerRequesterMap.get(workerSeqNumber);
    }

    @Override
    public Map<String, RSocketRequester> getRequesterMap() { return workerRequesterMap; }
}
