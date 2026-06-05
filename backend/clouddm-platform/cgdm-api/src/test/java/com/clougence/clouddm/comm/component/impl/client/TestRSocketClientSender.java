//package com.clougence.clouddm.comm.component.impl.client;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
//import org.slf4j.MDC;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.rsocket.RSocketRequester;
//import com.clougence.clouddm.base.utils.GlobalConfUtils;
//import com.clougence.clouddm.comm.WorkerRSocketClient;
//import com.clougence.clouddm.comm.component.RSocketRequestManager;
//import com.clougence.clouddm.comm.component.client.RSocketClientSender;
//import com.clougence.clouddm.comm.constants.rsocket.RSocketLogNames;
//import com.clougence.clouddm.comm.constants.rsocket.RSocketRouteNames;
//import com.clougence.clouddm.comm.model.RSocketDirectionType;
//import com.clougence.clouddm.comm.model.RSocketRequestWrapperDTO;
//import com.clougence.clouddm.comm.model.RSocketRespDTO;
//import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
//import com.clougence.clouddm.comm.model.rsocket.AsyncRequestFuture;
//import com.clougence.clouddm.comm.util.RSocketRespUtil;
//import com.clougence.utils.ExceptionUtils;
//import com.clougence.utils.HostUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.base.Stopwatch;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author wanshao create time is 2021/1/11
// **/
//
//@Slf4j
//public class TestRSocketClientSender implements RSocketClientSender {
//
//    private final int             TIMEOUT_MS = 5000;
//
//    private WorkerRSocketClient   workerRSocketClient;
//
//    private RSocketRequestManager rSocketRequestManager;
//
//    @Autowired
//    public TestRSocketClientSender(WorkerRSocketClient workerRSocketClient, RSocketRequestManager rSocketRequestManager){
//        this.workerRSocketClient = workerRSocketClient;
//        this.rSocketRequestManager = rSocketRequestManager;
//    }
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public RSocketRespDTO<?> requestNonBlock(String apiFullMethodName, Type[] paramTypes, Object... param) {
//        try {
//            Stopwatch stopwatch = Stopwatch.createStarted();
//            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
//
//            RSocketRequester requester = workerRSocketClient.getWorkingRequester();
//            if (requester == null) {
//                return RSocketRespUtil.buildError("rsocket requester is disposed,can not sent request.");
//            }
//
//            AsyncRequestFuture asyncRequestFuture = rSocketRequestManager.registerRsocketRequest(apiFullMethodName);
//            RSocketRequestWrapperDTO requestWrapperDTO = new RSocketRequestWrapperDTO();
//            requestWrapperDTO.setRequestId(asyncRequestFuture.getRequestId());
//            requestWrapperDTO.setRSocketDirectionType(RSocketDirectionType.CLIENT_TO_SERVER);
//            requestWrapperDTO.setWorkerIdentity(GlobalConfUtils.getIdentity());
//            requestWrapperDTO.setApiFullMethodName(apiFullMethodName);
//            List<String> jsonParamValues = new ArrayList<>();
//            for (Object paramObj : param) {
//                jsonParamValues.add(objectMapper.writeValueAsString(paramObj));
//            }
//            requestWrapperDTO.setParamJsonValues(jsonParamValues);
//            // Though wait result here. Actually, server side have change the spring route
//            // handler and process all request as fire-and-forget mode
//            requester.route(RSocketRouteNames.MAIN_REQUEST_DISPATCHER).data(requestWrapperDTO).retrieveMono(RSocketRespDTO.class).subscribe();
//            RSocketRespDTO<String> responseData = getAsyncResult(asyncRequestFuture);
//            stopwatch.stop();
//            log.info("[worker->console] send request to console, method name:" + requestWrapperDTO.getApiFullMethodName() + ".request id:" + asyncRequestFuture.getRequestId()
//                     + ",elapse:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
//            return responseData;
//        } catch (Exception exception) {
//            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
//            String errMsg = "Sidecar's request is disposed,return fail result immediately.msg:" + ExceptionUtils.getRootCauseMessage(exception) + ".route name:"
//                            + RSocketRouteNames.MAIN_REQUEST_DISPATCHER + ", api method name is " + apiFullMethodName;
//            log.error(errMsg);
//            return RSocketRespUtil.buildError(errMsg);
//        } finally {
//            MDC.remove("module");
//        }
//    }
//
//    private RSocketRespDTO<String> getAsyncResult(AsyncRequestFuture asyncRequestFuture) {
//        try {
//            return (RSocketRespDTO<String>) asyncRequestFuture.getSettableFuture().get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
//        } catch (TimeoutException e) {
//            rSocketRequestManager.removeTimeoutFutureResult(asyncRequestFuture);
//            String errMsg = "Fetch async result from future timeout. Current timeout time is " + TIMEOUT_MS + ". Route name is " + asyncRequestFuture.getRouteName()
//                            + ",requestId is " + asyncRequestFuture.getRequestId();
//            log.error(errMsg, e);
//            return RSocketRespUtil.buildError(errMsg);
//        } catch (Exception e) {
//            String errMsg = "Get async result failed with exception. Root cause is " + ExceptionUtils.getRootCauseMessage(e);
//            log.error(errMsg, e);
//            return RSocketRespUtil.buildError(errMsg);
//        }
//    }
//
//    @Override
//    public WorkerIdentity getIdentity() {
//        WorkerIdentity identity = new WorkerIdentity();
//        identity.setAccessKey(workerRSocketClient.getAccessKey());
//        identity.setWorkerSeqNumber(workerRSocketClient.getWorkerSequenceNumber());
//        identity.setLocalIp(HostUtil.getHostIp());
//        return identity;
//    }
//}
