package com.clougence.clouddm.comm.model;

import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/9
 **/
@Getter
@Setter
public class RequestForwardDTO implements WorkerIdentityAware {

    private String         uid;

    private String         apiMethodName;

    private String[]       jsonParams;

    private String         targetWsn;

    private WorkerIdentity workerIdentity;

    @Override
    public void injectWorkerIdentity(WorkerIdentity workerIdentity) {
        this.workerIdentity = workerIdentity;
    }

    @Override
    public WorkerIdentity getWorkerIdentity() { return workerIdentity; }
}
