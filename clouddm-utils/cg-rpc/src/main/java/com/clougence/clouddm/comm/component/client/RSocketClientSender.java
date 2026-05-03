package com.clougence.clouddm.comm.component.client;

import java.lang.reflect.Type;

import com.clougence.clouddm.comm.model.RSocketRespDTO;

/**
 * @author wanshao create time is 2021/1/9
 **/
public interface RSocketClientSender {

    RSocketRespDTO<?> requestNonBlock(String apiFullMethodName, Type[] paramTypes, Object[] param);
}
