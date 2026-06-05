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
