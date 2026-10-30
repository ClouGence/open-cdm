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
package com.clougence.clouddm.platform.dal.access;

import java.io.InputStream;
import java.io.OutputStream;

import com.clougence.clouddm.platform.dal.mapper.system.*;

public interface SystemDal {

    DmSysAttachmentMapper attachmentMapper();

    DmSysClusterMapper clusterMapper();

    DmSysEnvMapper envMapper();

    DmSysEnvParamMapper envParamMapper();

    DmSysMessengerMapper messengerMapper();

    DmSysUserConfMapper userConfMapper();

    DmSysWorkerMapper workerMapper();

    DmSshConfigMapper sshConfigMapper();

    // ---------- dal service methods ----------

    void writeAttachment(long attachmentId, InputStream input, long contentLength);

    boolean readAttachment(long attachmentId, OutputStream output);

    String fetchSystemConf(String configName);

    <T> T fetchSystemConf(String configName, Class<T> type);

    <T> T fetchSystemConf(String configName, Class<T> type, T defaultValue);

    String fetchUserConf(String uid, String configName);

    <T> T fetchUserConf(String uid, String configName, Class<T> type);

    <T> T fetchUserConf(String uid, String configName, Class<T> type, T defaultValue);
}
