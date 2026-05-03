package com.clougence.clouddm.comm.component;

import com.clougence.clouddm.comm.model.RSocketRequestWrapperDTO;
import com.clougence.clouddm.comm.model.RSocketRespDTO;

/**
 * Dispatcher rsocket requests to rsocket api
 *
 * @author wanshao create time is 2021/1/9
 **/
public interface RSocketRequestDispatcher {

    RSocketRespDTO<?> dispatchByRouteName(RSocketRequestWrapperDTO paramWrapper);
}
