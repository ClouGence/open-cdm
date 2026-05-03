package com.clougence.clouddm.console.web.global.rsocket;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.comm.RSocketSerialization;
import com.clougence.clouddm.comm.component.RSocketApiProxy;
import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.comm.constants.rsocket.RSocketLogNames;
import com.clougence.clouddm.comm.model.RSocketParam;
import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.comm.util.RSocketRespUtil;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/15
 **/
@Slf4j
public class DmServerRSocketApiProxy implements RSocketApiProxy {

    private final Class<?>             rsocketApi;
    private final ApplicationContext   applicationContext;
    private final RSocketSerialization serialization;
    private final DmConsoleConfig      dmConfig;

    public DmServerRSocketApiProxy(Class<?> rsocketApi, ApplicationContext applicationContext, RSocketSerialization serialization){
        this.rsocketApi = rsocketApi;
        this.applicationContext = applicationContext;
        this.dmConfig = applicationContext.getBean(DmConsoleConfig.class);
        this.serialization = serialization;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return sendNonBlockRequest(method, args);
    }

    @SneakyThrows
    private Object sendNonBlockRequest(Method method, Object[] args) {
        String methodFullName = rsocketApi.getName() + "." + method.getName();
        try {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
            if (args == null || args.length == 0 || !(args[0] instanceof RSocketSendDTO)) {
                throw new UnsupportedOperationException("Api param size is 0 or first parameter type is not " + RSocketSendDTO.class.getSimpleName());
            }
            RSocketSendDTO sendDTO = (RSocketSendDTO) args[0];
            if (sendDTO.getRSocketSendType() == null) {
                throw new UnsupportedOperationException("RSocket server request's send type can't be null and muse be specified, please check.");
            }
            RSocketRespDTO<?> dto;
            RSocketServerSender rSocketServerSender = applicationContext.getBean(RSocketServerSender.class);
            long atTime = System.currentTimeMillis();
            if (RSocketSendType.SPECIFIED == sendDTO.getRSocketSendType()) {
                dto = rSocketServerSender.requestNonBlock(methodFullName, sendDTO.getWorkerSeqNumber(), args);
            } else {
                dto = rSocketServerSender.requestNonBlock(sendDTO.getClusterId(), methodFullName, args);
            }

            if (RSocketRespUtil.isFailed(dto)) {
                atTime = System.currentTimeMillis() - atTime;
                throw new ErrorMessageException(MessageUtils
                    .getRpcInvokeTimeoutErrorMessage(sendDTO.getRSocketSendType(), sendDTO.getClusterId(), sendDTO.getWorkerIP(), atTime, dto.getMsg(), dto.getRequestId()));
            }

            Type paramJavaType = method.getGenericReturnType();
            String paramJson = String.valueOf(dto.getData());
            RSocketParam obj = JsonUtils.toObj(paramJson, RSocketParam.class);
            return this.serialization.decode(obj.getSerializer(), obj.getData(), paramJavaType);
        } catch (Exception e) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            String errMsg = "Api proxy invoke failed with exception. Invoke method is " + methodFullName;
            if (this.dmConfig.isConsoleRsocketPrintArgs()) {
                errMsg += ", args is " + JsonUtils.toJson(args);
            }
            errMsg += ". Root cause is " + ExceptionUtils.getRootCauseMessage(e);

            log.error(errMsg, e);
            throw e;
        } finally {
            MDC.remove("module");
        }
    }

    public static <T> T registerApi(Class<T> rsocketApiInterface, ApplicationContext applicationContext) {
        ClassLoader classLoader = rsocketApiInterface.getClassLoader();
        Class<?>[] interfaces = new Class[] { rsocketApiInterface };
        DmServerRSocketApiProxy proxy = new DmServerRSocketApiProxy(rsocketApiInterface, applicationContext, RSocketSerializationImpl.DEFAULT);
        return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
    }
}
