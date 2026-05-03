package com.clougence.clouddm.comm.model.rsocket;

import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.utils.future.BasicFuture;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/9
 **/
@Getter
@Setter
public class AsyncRequestFuture {

    private String                         requestId;

    private BasicFuture<RSocketRespDTO<?>> settableFuture;

    private String                         routeName;
}
