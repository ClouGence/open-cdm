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
