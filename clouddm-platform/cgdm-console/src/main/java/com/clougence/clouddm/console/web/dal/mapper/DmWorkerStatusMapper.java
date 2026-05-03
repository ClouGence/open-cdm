package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.console.web.dal.model.DmWorkerStatusDO;

/**
 * @author wanshao create time is 2020/7/11
 **/
public interface DmWorkerStatusMapper extends BaseMapper<DmWorkerStatusDO> {

    DmWorkerStatusDO queryByWsn(String workerSeqNumber);

    DmWorkerStatusDO queryOnlineByWsn(String workerSeqNumber);

    List<DmWorkerStatusDO> queryByClusterIdAndStatus(Long clusterId, WorkerConnStatus workerConnStatus);

    List<DmWorkerStatusDO> queryByConsoleIpAndStatus(String consoleIp, WorkerConnStatus workerConnStatus);

    List<DmWorkerStatusDO> queryInactivity(@Param("checkPoint") Date checkPoint);

    int updateConnInfoByWsn(DmWorkerStatusDO workerStatusDO);

    int updateStatusById(long id, WorkerConnStatus workerConnStatus);

    int deleteByWsn(String workerSeqNumber);
}
