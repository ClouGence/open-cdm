/*
 * Copyright 2015-2022 the original author or authors.
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

    /** 就绪状，表示随时可以开始新的查询。如果连接上发起多次查询，当前一个查询的所有结果都被读取后也会进入就绪状态。 */
    Ready(true),
    /** 等待中，表示正在或者已经将查询请求发送给远程数据库服务器，数据库服务器尚未响应，通常是指在第一条数据到达之前。*/
    Pending(false),
    /** 接收中，表示查询已经完毕，服务器开始向客户端传送查询结果数据。 */
    Receive(false),

    ;

    private final boolean finish;

    public boolean isFinish() { return this.finish; }

    AdapterReceiveState(boolean finish){
        this.finish = finish;
    }
}
