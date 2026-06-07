package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.system.*;

public interface SystemDal {

    DmSysClusterMapper clusterMapper();

    DmSysConfMapper confMapper();

    DmSysEnvMapper envMapper();

    DmSysEnvParamMapper envParamMapper();

    DmSysMessengerMapper messengerMapper();

    DmSysUserConfMapper userConfMapper();

    DmSysWorkerMapper workerMapper();

    // ---------- dal service methods ----------

    String fetchSystemConf(String configName);

    <T> T fetchSystemConf(String configName, Class<T> type);

    <T> T fetchSystemConf(String configName, Class<T> type, T defaultValue);

    String fetchUserConf(String uid, String configName);

    <T> T fetchUserConf(String uid, String configName, Class<T> type);

    <T> T fetchUserConf(String uid, String configName, Class<T> type, T defaultValue);
}
