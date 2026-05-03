package com.clougence.clouddm.comm.component.client;

import org.springframework.messaging.rsocket.RSocketRequester;

/**
 * @author wanshao create time is 2021/1/13
 **/
public interface ClientSideRegistry {

    RSocketRequester getRSocketRequester();

}
