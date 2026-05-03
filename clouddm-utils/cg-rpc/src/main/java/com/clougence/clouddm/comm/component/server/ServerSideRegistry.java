package com.clougence.clouddm.comm.component.server;

import java.util.Map;

import org.springframework.messaging.rsocket.RSocketRequester;

import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.rsocket.RSocketRegisterInfo;

/**
 * Server side will use this class to finish register and unregister
 *
 * @author wanshao create time is 2021/1/7
 **/
public interface ServerSideRegistry {

    void checkAuth(ConnAuthDTO authInfo);

    /**
     * Register worker's info to db
     */
    void register(RSocketRegisterInfo registerInfo);

    void unRegister(String workerSeqNumber);

    /**
     * Get worker requester object to send data by wsn. Usually be used in server side
     */
    RSocketRequester getRSocketRequester(String workerSeqNumber);

    Map<String, RSocketRequester> getRequesterMap();
}
