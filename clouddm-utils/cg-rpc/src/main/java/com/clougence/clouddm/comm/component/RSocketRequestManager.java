package com.clougence.clouddm.comm.component;

import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.clouddm.comm.model.SendBackDataDTO;
import com.clougence.clouddm.comm.model.rsocket.AsyncRequestFuture;
import reactor.core.publisher.Mono;

/**
 * Manager to help send async request and receive async response
 *
 * @author wanshao create time is 2021/1/9
 **/
public interface RSocketRequestManager {

    /**
     * register rsocket request and get rsocket result async
     *
     * @return requestId. invoker can use the requestId to get the async result
     */
    AsyncRequestFuture registerRsocketRequest(String routeName);

    /**
     * fill async result
     */
    void fillAsyncResult(RSocketRespDTO<?> responseData);

    /**
     * remove timeout future result in map
     */
    void removeTimeoutFutureResult(AsyncRequestFuture asyncRequestFuture);

    /**
     * After process async will use this method to send back async results
     */
    void sendAsyncResultBack(RSocketRespDTO rSocketRespDTO);

    /**
     * Receive rsocket request's async response and update future result
     */
    Mono<Void> responseReceiver(SendBackDataDTO sendBackDataDTO);

}
