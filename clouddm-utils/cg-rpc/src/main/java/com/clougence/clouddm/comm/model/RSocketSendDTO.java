package com.clougence.clouddm.comm.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/15
 **/
@Getter
@Setter
public class RSocketSendDTO {

    private Long            clusterId;

    private String          uid;

    private String          workerSeqNumber;
    private String          workerIP;

    private RSocketSendType rSocketSendType;
}
