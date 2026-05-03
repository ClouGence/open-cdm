package com.clougence.clouddm.worker.component.rsocket;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.comm.WorkerRSocketClient;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.client.RSocketClientSender;
import com.clougence.clouddm.comm.constants.rsocket.RSocketLogNames;
import com.clougence.clouddm.comm.constants.rsocket.RSocketRouteNames;
import com.clougence.clouddm.comm.model.RSocketDirectionType;
import com.clougence.clouddm.comm.model.RSocketParam;
import com.clougence.clouddm.comm.model.RSocketRequestWrapperDTO;
import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.comm.model.rsocket.AsyncRequestFuture;
import com.clougence.clouddm.comm.util.RSocketRespUtil;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.google.common.base.Stopwatch;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/11
 **/
@Slf4j
public class DmClientSender implements RSocketClientSender {

    private final int             TIMEOUT_MS = 60000;

    private WorkerIdentity        identity;
    private WorkerRSocketClient   workerRSocketClient;
    private RSocketRequestManager rSocketRequestManager;

    @Autowired
    public DmClientSender(WorkerRSocketClient workerRSocketClient, RSocketRequestManager rSocketRequestManager){
        this.workerRSocketClient = workerRSocketClient;
        this.rSocketRequestManager = rSocketRequestManager;
    }

    @Override
    public RSocketRespDTO<?> requestNonBlock(String apiFullMethodName, Type[] paramTypes, Object[] paramArrays) {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);

            RSocketRequester requester = workerRSocketClient.getWorkingRequester();
            if (requester == null) {
                return RSocketRespUtil.buildError("rsocket requester is disposed,can not sent request.");
            }

            AsyncRequestFuture asyncRequestFuture = rSocketRequestManager.registerRsocketRequest(apiFullMethodName);
            RSocketRequestWrapperDTO requestWrapperDTO = new RSocketRequestWrapperDTO();
            requestWrapperDTO.setRequestId(asyncRequestFuture.getRequestId());
            requestWrapperDTO.setRSocketDirectionType(RSocketDirectionType.CLIENT_TO_SERVER);
            requestWrapperDTO.setWorkerIdentity(this.getIdentity());
            requestWrapperDTO.setApiFullMethodName(apiFullMethodName);
            List<String> jsonParamValues = new ArrayList<>();
            for (int i = 0; i < paramTypes.length; i++) {
                Object paramObj = paramArrays[i];
                String encodeData = JsonUtils.toJson(paramObj);
                String finalData = JsonUtils.toJson(toRParam(paramObj, encodeData));
                jsonParamValues.add(finalData);
            }
            requestWrapperDTO.setParamJsonValues(jsonParamValues);
            // Though wait result here. Actually, server side have change the spring route
            // handler and process all request as fire-and-forget mode
            requester.route(RSocketRouteNames.MAIN_REQUEST_DISPATCHER).data(requestWrapperDTO).retrieveMono(RSocketRespDTO.class).subscribe();
            RSocketRespDTO<String> responseData = getAsyncResult(asyncRequestFuture);
            stopwatch.stop();
            log.debug("[worker->console] send request to console, method name:" + requestWrapperDTO.getApiFullMethodName() + ".request id:" + asyncRequestFuture.getRequestId()
                      + ",elapse:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
            return responseData;
        } catch (Exception exception) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            String errMsg = "Sidecar's request is disposed,return fail result immediately.msg:" + ExceptionUtils.getRootCauseMessage(exception) + ".route name:"
                            + RSocketRouteNames.MAIN_REQUEST_DISPATCHER + ", api method name is " + apiFullMethodName;
            log.error(errMsg);
            return RSocketRespUtil.buildError(errMsg);
        } finally {
            MDC.remove("module");
        }
    }

    private RSocketParam toRParam(Object arg, String encodeData) {
        if (arg != null) {
            Class<?> argClass = arg.getClass();
            Serialization annotation = argClass.getAnnotation(Serialization.class);
            if (annotation != null) {
                return new RSocketParam(annotation.provider(), encodeData);
            }
        }
        return new RSocketParam(null, encodeData);
    }

    private RSocketRespDTO<String> getAsyncResult(AsyncRequestFuture asyncRequestFuture) {
        try {
            return (RSocketRespDTO<String>) asyncRequestFuture.getSettableFuture().get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            rSocketRequestManager.removeTimeoutFutureResult(asyncRequestFuture);
            String errMsg = "Fetch async result from future timeout. Current timeout time is " + TIMEOUT_MS + ". Route name is " + asyncRequestFuture.getRouteName()
                            + ",requestId is " + asyncRequestFuture.getRequestId();
            log.error(errMsg, e);
            return RSocketRespUtil.buildError(errMsg);
        } catch (Exception e) {
            String errMsg = "Get async result failed with exception. Root cause is " + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            return RSocketRespUtil.buildError(errMsg);
        }
    }

    private WorkerIdentity getIdentity() throws IOException {
        if (this.identity != null) {
            return this.identity;
        }

        ConnAuthDTO authDto = GlobalConfUtils.loadGlobalConf();
        if (StringUtils.isBlank(authDto.getWsn()) || StringUtils.isBlank(authDto.getAk()) || StringUtils.isBlank(authDto.getSk())) {
            throw new IllegalArgumentException("properties in global config (" + authDto + ") are empty.");
        }

        WorkerIdentity identity = new WorkerIdentity();

        identity.setAccessKey(authDto.getAk());
        identity.setWorkerSeqNumber(authDto.getWsn());
        identity.setLocalIp(HostUtil.getHostIp());

        this.identity = identity;
        return this.identity;
    }
}
