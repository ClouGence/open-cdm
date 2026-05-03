package com.clougence.clouddm.worker.component.rsocket;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.comm.RSocketSerialization;
import com.clougence.clouddm.comm.component.RSocketApiProxy;
import com.clougence.clouddm.comm.component.client.RSocketClientSender;
import com.clougence.clouddm.comm.model.RSocketParam;
import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.clouddm.comm.util.RSocketRespUtil;
import com.clougence.utils.JsonUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/16
 **/
@Slf4j
public class DmClientRSocketApiProxy implements RSocketApiProxy {

    private final ApplicationContext   applicationContext;
    private final RSocketSerialization serialization;

    public DmClientRSocketApiProxy(Object api, ApplicationContext applicationContext, RSocketSerialization serialization){
        this.applicationContext = applicationContext;
        this.serialization = serialization;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return sendNonBlockRequest(method, args);
    }

    @SneakyThrows
    private Object sendNonBlockRequest(Method method, Object[] args) {
        RSocketClientSender rSocketClientSender = applicationContext.getBean(RSocketClientSender.class);
        String fullMethodName = getMethodFullNameForSend(method);
        RSocketRespDTO<?> rSocketRespDTO = rSocketClientSender.requestNonBlock(fullMethodName, method.getParameterTypes(), args);

        if (RSocketRespUtil.isFailed(rSocketRespDTO)) {
            throw new RuntimeException(
                "Receive failed response from remote server. MsgContent is :\n" + rSocketRespDTO.getMsg() + "\n Request id is " + rSocketRespDTO.getRequestId() + "\n");
        }
        Type paramJavaType = method.getGenericReturnType();
        String paramJson = String.valueOf(rSocketRespDTO.getData());
        RSocketParam obj = JsonUtils.toObj(paramJson, RSocketParam.class);
        return this.serialization.decode(obj.getSerializer(), obj.getData(), paramJavaType);
    }

    private static String getMethodFullNameForSend(Method method) {
        String interfaceClzName = method.getDeclaringClass().getCanonicalName();
        String methodName = method.getName();
        return interfaceClzName + "." + methodName;
    }
}
