package com.clougence.clouddm.model;
import java.util.Date;

import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.Tolerate;

/**
 * @author wanshao create time is 2021/1/12
 **/
@Data
@Builder
@FieldNameConstants
public class TestWorkerStatusDO {

    @Tolerate
    public TestWorkerStatusDO(){
    }

    private Long             id;

    private Date             gmtCreate;

    private Date             gmtModified;

    private WorkerConnStatus workerConnStatus;

    private String           uid;

    private String           workerSeqNumber;

    private String           consoleIp;

    private String           sidecarIp;

    private Long             clusterId;
}
