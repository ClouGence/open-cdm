package com.clougence.clouddm.comm.component.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.model.TestWorkerStatusDO;
import lombok.Getter;

/**
 * @author wanshao create time is 2021/1/12
 **/

public class TestWorkerStatusMapper {

    // Mock store to persist worker status. Key is wsn
    @Getter
    private Map<String, TestWorkerStatusDO> testWorkerStatusDOMap = new ConcurrentHashMap<>();

    public TestWorkerStatusDO queryByWsn(String workerSeqNumber) {
        TestWorkerStatusDO testWorkerStatusDO = testWorkerStatusDOMap.get(workerSeqNumber);

        return testWorkerStatusDO;
    }

    public List<TestWorkerStatusDO> queryByUidClusterIdAndStatus(String uid, WorkerConnStatus workerConnStatus, Long clusterId) {
        if (testWorkerStatusDOMap.size() == 0) {
            throw new IllegalStateException("There is no registered test workers....");
        } else {
            return testWorkerStatusDOMap.values().stream().collect(Collectors.toList());
        }
    }

    public List<TestWorkerStatusDO> queryByClusterIdAndStatus(Long clusterId, WorkerConnStatus workerConnStatus) {
        if (testWorkerStatusDOMap.size() == 0) {
            throw new IllegalStateException("There is no registered test workers....");
        } else {
            return testWorkerStatusDOMap.values().stream().collect(Collectors.toList());
        }
    }

    public int updateConnInfoByWsn(TestWorkerStatusDO workerStatusDO) {
        return 1;
    }

}
