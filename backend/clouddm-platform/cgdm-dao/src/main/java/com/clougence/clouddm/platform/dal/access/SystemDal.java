package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.system.*;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;

public interface SystemDal {

    DmSysClusterMapper clusterMapper();

    DmSysConfMapper confMapper();

    DmSysEnvMapper envMapper();

    DmSysEnvParamMapper envParamMapper();

    DmSysMessengerMapper messengerMapper();

    DmSysUserConfMapper userConfMapper();

    DmSysWorkerMapper workerMapper();

    // ---------- dal service methods ----------

    DmSysUserConfDO getSpecifiedConfig(String uid, String configName);
}
