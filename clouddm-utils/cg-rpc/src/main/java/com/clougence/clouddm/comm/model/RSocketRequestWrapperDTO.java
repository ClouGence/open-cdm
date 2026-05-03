package com.clougence.clouddm.comm.model;

import java.util.List;

import com.clougence.clouddm.comm.model.auth.WorkerIdentity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/9
 **/
@Getter
@Setter
public class RSocketRequestWrapperDTO {

    /**
     * Related request's api method name
     */
    protected String               apiFullMethodName;

    protected List<String>         paramJsonValues;

    protected String               requestId;

    protected RSocketDirectionType rSocketDirectionType;

    protected WorkerIdentity       workerIdentity;
}
