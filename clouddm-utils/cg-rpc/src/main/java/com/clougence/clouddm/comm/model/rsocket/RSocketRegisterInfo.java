package com.clougence.clouddm.comm.model.rsocket;

import org.springframework.messaging.rsocket.RSocketRequester;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Getter
@Setter
public class RSocketRegisterInfo {

    private RSocketRequester requester;

    private String           workerSeqNumber;

    private String           workerIp;
}
