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
