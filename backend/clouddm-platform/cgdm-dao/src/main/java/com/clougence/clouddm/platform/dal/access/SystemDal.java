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
