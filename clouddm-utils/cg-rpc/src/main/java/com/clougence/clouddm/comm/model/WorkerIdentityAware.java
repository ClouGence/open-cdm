package com.clougence.clouddm.comm.model;

import com.clougence.clouddm.comm.model.auth.WorkerIdentity;

/**
 * @author wanshao create time is 2021/1/9
 **/
public interface WorkerIdentityAware {

    void injectWorkerIdentity(WorkerIdentity workerIdentity);

    WorkerIdentity getWorkerIdentity();
}
