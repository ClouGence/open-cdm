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

/**
 * D
 * 
 * @author wanshao create time is 2021/1/7
 **/
public interface RSocketConnManager {

    /**
     * when console startup,no worker will bind with this console,so SidecarStatusDO bind with this console in DB is illegal,need to be reset.or these workers will not be allow register again.this
     * case will only happen in console process force killed or console machine sudden crashed without unregister workers
     */
    void resetWorkerStatusInDB();

}
