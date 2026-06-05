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
package com.clougence.clouddm.comm.constants.worker;

/**
 * @author wanshao create time is 2021/1/7
 **/
public enum WorkerConnStatus {
    /**
     * sidecar's rsocket connection is connected
     */
    CONNECTED,
    /**
     * sidecar's socket connection is disconnected
     */
    DISCONNECTED,
    /**
     * new status means the sidecar never connect to console, usually it is newly added and never start
     */
    NEW
}
