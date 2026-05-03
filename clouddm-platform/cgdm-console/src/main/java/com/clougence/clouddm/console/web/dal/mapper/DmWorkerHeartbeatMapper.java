package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.WorkerHeartbeatType;
import com.clougence.clouddm.console.web.dal.model.DmWorkerHeartbeatDO;

/**
 * @author bucketli 2020-01-22 15:20
 * @since 1.1.3
 */
public interface DmWorkerHeartbeatMapper extends BaseMapper<DmWorkerHeartbeatDO> {

    /**
     * query by workerId,one worker on heartbeat record
     */
    DmWorkerHeartbeatDO queryHeartbeatByWsn(String workerSeqNumber);

    /**
     * update gmt_modified,sidecar_send_time by worker_id
     */
    int updateHeartbeatByWsn(Date workerSendTime, WorkerHeartbeatType heartbeatType, String workerSeqNumber);
}
