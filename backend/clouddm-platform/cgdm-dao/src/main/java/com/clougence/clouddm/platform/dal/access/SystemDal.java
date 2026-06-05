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
}
