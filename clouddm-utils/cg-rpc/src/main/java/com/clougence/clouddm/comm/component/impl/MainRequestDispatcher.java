package com.clougence.clouddm.comm.component.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.MDC;
import org.springframework.messaging.handler.annotation.MessageMapping;

import com.clougence.clouddm.comm.RSocketSerialization;
import com.clougence.clouddm.comm.component.RSocketExceptionManager;
import com.clougence.clouddm.comm.component.RSocketRequestDispatcher;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.RSocketThreadFactory;
import com.clougence.clouddm.comm.constants.rsocket.RSocketLogNames;
import com.clougence.clouddm.comm.constants.rsocket.RSocketRouteNames;
import com.clougence.clouddm.comm.model.*;
import com.clougence.clouddm.comm.util.RSocketRespUtil;
import com.clougence.utils.ClassUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.reflect.Annotation;
import com.clougence.utils.reflect.Annotations;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/9
 **/
@Slf4j
public class MainRequestDispatcher implements RSocketRequestDispatcher {

    private final int                     CORE_SIZE           = 10;
    private final int                     MAX_SIZE            = 100;
    private final int                     WAIT_QUEUE_SIZE     = 1;
    private final String                  THREAD_NAME         = "clouddm-rsocket-async-worker-pool";

    private static final int              LOG_COUNT_THRESHOLD = 100;
    private final AtomicInteger           taskSubmitCount     = new AtomicInteger(0);

    private final RSocketRequestManager   rSocketRequestManager;
    private final RSocketExceptionManager rSocketExceptionManager;
    private final RSocketSerialization    serialization;

    public MainRequestDispatcher(RSocketRequestManager rSocketRequestManager, RSocketExceptionManager rSocketExceptionManager, RSocketSerialization serialization){
        this.rSocketRequestManager = rSocketRequestManager;
        this.rSocketExceptionManager = rSocketExceptionManager;
        this.serialization = serialization;
    }

    private final ThreadPoolExecutor executorService = new ThreadPoolExecutor(CORE_SIZE,
        MAX_SIZE,
        3,
        TimeUnit.MINUTES,
        new ArrayBlockingQueue<>(WAIT_QUEUE_SIZE),
        new RSocketThreadFactory(THREAD_NAME, true),
        new ThreadPoolExecutor.AbortPolicy());

    @MessageMapping(RSocketRouteNames.MAIN_REQUEST_DISPATCHER)
    @SneakyThrows
    @Override
    public RSocketRespDTO<?> dispatchByRouteName(RSocketRequestWrapperDTO paramWrapper) {
        submitToAsyncWorkerPool(() -> {
            RSocketRespDTO<?> result;
            try {
                String apiMethodName = paramWrapper.getApiFullMethodName();
                RSocketApiWrapper apiWrapper = RSocketApiManager.getWorkerDsApi(apiMethodName);
                if (apiWrapper == null) {
                    apiWrapper = RSocketApiManager.getWorkerDsApi(apiMethodName);
                }
                if (apiWrapper == null) {
                    String errMsg = "Can't find api method " + apiMethodName + " both in ds and sidecar api ...";
                    log.error(errMsg);
                    throw new IllegalArgumentException(errMsg);
                }

                Object apiObj = apiWrapper.getApiObj();
                Method method = apiWrapper.getApiMethod();

                Parameter[] parameters = method.getParameters();
                List<Object> deserializedParams = new ArrayList<>();

                for (int i = 0; i < parameters.length; i++) {
                    String paramJson = paramWrapper.getParamJsonValues().get(i);
                    RSocketParam obj = JsonUtils.toObj(paramJson, RSocketParam.class);
                    deserializedParams.add(this.serialization.decode(obj.getSerializer(), obj.getData(), parameters[i].getParameterizedType()));
                }

                Object[] paramObjs = deserializedParams.toArray(new Object[0]);
                Object respObj = method.invoke(apiObj, paramObjs);
                result = generateSuccessBackResult(method, respObj, paramWrapper);
            } catch (Throwable e) {
                MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
                String msg = "invoke api method " + paramWrapper.getApiFullMethodName() + " failed.msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.error(msg, e);

                Throwable rootCause = ExceptionUtils.getRootCause(e);
                result = generateFailBackResult(paramWrapper, rootCause);
                rSocketExceptionManager.saveExcOrAlert(rootCause);
            } finally {
                MDC.remove("module");
            }

            rSocketRequestManager.sendAsyncResultBack(result);
        });

        RSocketDirectionType directionType;
        if (paramWrapper.getRSocketDirectionType() == null) {
            log.error("Direction type is null for sendback request " + paramWrapper.getRequestId());
            directionType = null;
        } else if (paramWrapper.getRSocketDirectionType() == RSocketDirectionType.SERVER_TO_CLIENT) {
            directionType = RSocketDirectionType.CLIENT_TO_SERVER;
        } else {
            directionType = RSocketDirectionType.SERVER_TO_CLIENT;
        }

        String msg = "Dispatch request " + paramWrapper.getRequestId() + " to apiMethod " + paramWrapper.getApiFullMethodName() + " has been accepted. It will execute async.";
        log.debug(msg);

        return RSocketRespUtil.buildSyncSuccess(msg, directionType);
    }

    @SneakyThrows
    private RSocketRespDTO<?> generateSuccessBackResult(Method method, Object invokeResult, RSocketRequestWrapperDTO requestWrapperDTO) {
        String finalData = JsonUtils.toJson(toRParam(invokeResult));

        RSocketRespDTO<?> result = RSocketRespUtil.buildSuccess(finalData);
        fillBackInfo(result, requestWrapperDTO);
        return result;
    }

    private static final Map<Class<?>, String> providerCache = new ConcurrentHashMap<>();

    private RSocketParam toRParam(Object arg) {
        String provider = null;
        if (arg != null) {
            Class<?> argClass = arg.getClass();
            provider = providerCache.computeIfAbsent(argClass, aClass -> {
                try {
                    Annotations all = Annotations.ofClass(argClass);
                    Annotation serialization = all.getAnnotation("com.clougence.clouddm.sdk.config.Serialization");
                    if (serialization != null) {
                        return serialization.getString("provider", null);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    throw ExceptionUtils.toRuntime(e);
                }
            });
        }

        String encode = this.serialization.encode(provider, arg);
        return new RSocketParam(provider, encode);
    }

    @SneakyThrows
    private RSocketRespDTO<?> generateFailBackResult(RSocketRequestWrapperDTO requestWrapperDTO, Throwable e) {
        Throwable realErr = ExceptionUtils.getRootCause(e);
        String fullMethodName = requestWrapperDTO.getApiFullMethodName();
        String shortMethodName = ClassUtils.getShortClassName(fullMethodName);

        //ExceptionUtils.getStackTrace(realErr);
        RSocketRespDTO<?> result = RSocketRespUtil.buildError(RSocketRespCode.ERROR.getCode(), "Api " + shortMethodName + " failed, " + ExceptionUtils.getRootCauseMessage(e));
        fillBackInfo(result, requestWrapperDTO);
        return result;
    }

    protected void fillBackInfo(RSocketRespDTO<?> result, RSocketRequestWrapperDTO requestWrapperDTO) {
        result.setWorkerIdentity(requestWrapperDTO.getWorkerIdentity());
        result.setRequestId(requestWrapperDTO.getRequestId());

        if (RSocketDirectionType.SERVER_TO_CLIENT == requestWrapperDTO.getRSocketDirectionType()) {
            result.setRSocketDirectionType(RSocketDirectionType.CLIENT_TO_SERVER);
        } else {
            result.setRSocketDirectionType(RSocketDirectionType.SERVER_TO_CLIENT);
        }

        result.setWorkerIdentity(requestWrapperDTO.getWorkerIdentity());
        result.setApiFullMethodName(requestWrapperDTO.getApiFullMethodName());
    }

    private void submitToAsyncWorkerPool(Runnable processLogic) {
        try {
            MDC.put("module", "rsocket_async_pool");
            executorService.execute(processLogic);
            taskSubmitCount.addAndGet(1);
            if (taskSubmitCount.get() == LOG_COUNT_THRESHOLD) {
                taskSubmitCount.set(0);
                log.info("=========================");
                log.info("Pool Size: {}", executorService.getPoolSize());
                log.info("Active Threads: {}", executorService.getActiveCount());
                log.info("Number of Tasks Completed: {}", executorService.getCompletedTaskCount());
                log.info("Number of Tasks in Queue: {}", executorService.getQueue().size());
                log.info("=========================");
            }
        } finally {
            MDC.remove("module");
        }
    }
}
