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
