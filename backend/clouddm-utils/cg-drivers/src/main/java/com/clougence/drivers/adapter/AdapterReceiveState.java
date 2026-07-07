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
package com.clougence.drivers.adapter;

public enum AdapterReceiveState {

    /** Ready to start new inquiries at any time. If multiple queries are launched on the connection, all results of the current query will be read and ready. */
    Ready(true),
    /** Waiting to indicate that a request for queries is being sent or has been sent to a remote database server, which has not yet responded, usually before the first data arrival. */
    Pending(false),
    /** In receipt, indicating that the query has been completed and the server has started to transmit the query data to the client. */
    Receive(false),

    ;

    private final boolean finish;

    public boolean isFinish() { return this.finish; }

    AdapterReceiveState(boolean finish){
        this.finish = finish;
    }
}
