package com.clougence.clouddm.comm.component.impl.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.RandomUtils;
import org.slf4j.MDC;
import org.springframework.messaging.rsocket.RSocketRequester;

import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.http.CanalHttpClient;
import com.clougence.clouddm.comm.component.impl.TestWorkerStatusMapper;
import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.constants.rsocket.RSocketLogNames;
import com.clougence.clouddm.comm.constants.rsocket.RSocketRouteNames;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.RSocketDirectionType;
import com.clougence.clouddm.comm.model.RSocketRequestWrapperDTO;
import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.clouddm.comm.model.RequestForwardDTO;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.comm.model.rsocket.AsyncRequestFuture;
import com.clougence.clouddm.comm.util.RSocketRespUtil;
import com.clougence.clouddm.model.TestWorkerStatusDO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HostUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/12
 **/
@Slf4j
public class TestRSocketServerSender implements RSocketServerSender {

    private static final String    URL_SUFFIX          = "/clouddm/console/api/v1/rsocket/forward";

    private final int              TIMEOUT_MS          = 5000;

    private final int              SERVER_FORWARD_PORT = 8222;

    private RSocketRequestManager  rSocketRequestManager;

    private TestWorkerStatusMapper workerStatusMapper;

    private ServerSideRegistry     serverSideRegistry;

    private ObjectMapper           objectMapper        = new ObjectMapper();

    private String                 jwtToken;

    public TestRSocketServerSender(RSocketRequestManager rSocketRequestManager, TestWorkerStatusMapper workerStatusMapper, ServerSideRegistry serverSideRegistry){
        this.rSocketRequestManager = rSocketRequestManager;
        this.workerStatusMapper = workerStatusMapper;
        this.serverSideRegistry = serverSideRegistry;
    }

    @SneakyThrows
    @Override
    public RSocketRespDTO<?> requestNonBlock(Long clusterId, String apiFullMethodName, Object... param) {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
            List<TestWorkerStatusDO> statusDOs = getTestWorkerStatusDOFromDb(clusterId);
            TestWorkerStatusDO workerStatus = chooseLocalRegisteredWorker(statusDOs);
            if (workerStatus == null) {
                TestWorkerStatusDO randomWorker = statusDOs.get(RandomUtils.nextInt(0, statusDOs.size()));
                log.info("forward rsocket request to random worker " + randomWorker.getSidecarIp() + ", api method name is " + apiFullMethodName);
                //  return forwardRequest(apiFullMethodName, randomWorker.getConsoleIp(), randomWorker.getWorkerSeqNumber(), apiFullMethodName, param);
                return null;
            } else {
                RSocketRequester requester = serverSideRegistry.getRequesterMap().get(workerStatus.getWorkerSeqNumber());
                if (requester == null) {
                    String errMsg = "local bind worker get requester from cache is empty.wsn: " + workerStatus.getWorkerSeqNumber();
                    return RSocketRespUtil.buildError(errMsg);
                }

                AsyncRequestFuture asyncRequestFuture = rSocketRequestManager.registerRsocketRequest(apiFullMethodName);
                RSocketRequestWrapperDTO requestWrapperDTO = new RSocketRequestWrapperDTO();
                requestWrapperDTO.setRequestId(asyncRequestFuture.getRequestId());
                requestWrapperDTO.setRSocketDirectionType(RSocketDirectionType.SERVER_TO_CLIENT);
                requestWrapperDTO.setWorkerIdentity(getIdentity());
                requestWrapperDTO.setApiFullMethodName(apiFullMethodName);
                List<String> jsonParamValues = new ArrayList<>();
                for (Object paramObj : param) {
                    jsonParamValues.add(objectMapper.writeValueAsString(paramObj));
                }
                requestWrapperDTO.setParamJsonValues(jsonParamValues);
                // Though wait result here. Actually, server side have change the spring route
                // handler and process all request as fire-and-forget mode
                requester.route(RSocketRouteNames.MAIN_REQUEST_DISPATCHER).data(requestWrapperDTO).retrieveMono(RSocketRespDTO.class).subscribe();
                RSocketRespDTO<String> responseData = getAsyncResult(asyncRequestFuture);
                stopwatch.stop();
                log.info("[server->client] send request to client, route name is " + apiFullMethodName + ".request_id:" + asyncRequestFuture.getRequestId() + ",elapse:"
                         + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
                return responseData;
            }
        } catch (Exception e) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            log.error("Send rsocket request failed with exception, target api is " + apiFullMethodName + ". Root cause is " + ExceptionUtils.getRootCauseMessage(e), e);
            throw e;
        } finally {
            MDC.remove("module");
        }
    }

    @SneakyThrows
    @Override
    public RSocketRespDTO<?> requestNonBlock(String apiFullMethodName, String specifiedWsn, Object[] param) {
        try {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
            Stopwatch stopwatch = Stopwatch.createStarted();
            TestWorkerStatusDO workerStatus = workerStatusMapper.queryByWsn(specifiedWsn);
            if (workerStatus == null || workerStatus.getWorkerConnStatus() != WorkerConnStatus.CONNECTED) {
                String errMsg = "worker is not connected. can't send request to it. wsn:" + specifiedWsn;
                log.error(errMsg);
                return RSocketRespUtil.buildError(errMsg);
            }

            if (isLocalRegisteredWorker(workerStatus)) {
                RSocketRequester requester = serverSideRegistry.getRequesterMap().get(workerStatus.getWorkerSeqNumber());
                if (requester == null) {
                    String errMsg = "local bind worker get requester from cache is empty.wsn: " + workerStatus.getWorkerSeqNumber();
                    return RSocketRespUtil.buildError(errMsg);
                }

                AsyncRequestFuture asyncRequestFuture = rSocketRequestManager.registerRsocketRequest(apiFullMethodName);
                RSocketRequestWrapperDTO requestWrapperDTO = new RSocketRequestWrapperDTO();
                requestWrapperDTO.setRequestId(asyncRequestFuture.getRequestId());
                requestWrapperDTO.setRSocketDirectionType(RSocketDirectionType.SERVER_TO_CLIENT);
                requestWrapperDTO.setWorkerIdentity(getIdentity());
                requestWrapperDTO.setApiFullMethodName(apiFullMethodName);
                List<String> jsonParamValues = new ArrayList<>();
                for (Object paramObj : param) {
                    jsonParamValues.add(objectMapper.writeValueAsString(paramObj));
                }
                requestWrapperDTO.setParamJsonValues(jsonParamValues);

                requester.route(RSocketRouteNames.MAIN_REQUEST_DISPATCHER).data(requestWrapperDTO).retrieveMono(RSocketRespDTO.class).doOnError(error -> {
                    log.error("Server send request failed with exception. Root cause is " + ExceptionUtils.getRootCauseMessage(error), error);
                }).subscribe();

                RSocketRespDTO<?> response = getAsyncResult(asyncRequestFuture);
                stopwatch.stop();
                log.info("[console->client] send request to client, route name is " + apiFullMethodName + ".request_id:" + asyncRequestFuture.getRequestId() + ",elapse:"
                         + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
                return response;
            } else {
                log.info("forward rsocket request to specified console (" + workerStatus.getConsoleIp() + "), method name:" + apiFullMethodName);
                return null;
                //return forwardRequest(apiFullMethodName, workerStatus.getConsoleIp(), workerStatus.getWorkerSeqNumber(), apiFullMethodName, param);
            }
        } catch (Exception e) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            log.error("Send rsocket request failed with exception. Specified worker wsn is " + specifiedWsn + ", target api is " + apiFullMethodName + ". Root cause is "
                      + ExceptionUtils.getRootCauseMessage(e), e);
            throw e;
        } finally {
            MDC.remove("module");
        }
    }

    @Override
    public RSocketRespDTO<?> requestNonBlockWithJsonParams(String apiFullMethodName, String specifiedWsn, String[] paramJson) {
        return null;
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

    protected List<TestWorkerStatusDO> getTestWorkerStatusDOFromDb(Long clusterId) {
        if (clusterId == null || clusterId <= 0) {
            String errMsg = "cluster id can not be empty.";
            log.error(errMsg);
            throw new RuntimeException(errMsg);
        }

        List<TestWorkerStatusDO> sidecarStatus;
        sidecarStatus = workerStatusMapper.queryByClusterIdAndStatus(clusterId, WorkerConnStatus.CONNECTED);

        if (CollectionUtils.isEmpty(sidecarStatus)) {
            String errMsg = "can't find available registered/connected workers.";
            log.error(errMsg);
            throw new RuntimeException(errMsg);
        }

        return sidecarStatus;
    }

    /**
     * get one worker of cluster in local
     */
    protected TestWorkerStatusDO chooseLocalRegisteredWorker(List<TestWorkerStatusDO> statusDOs) {
        String localIp = HostUtil.getHostIp();
        TestWorkerStatusDO localBindSidecar = null;
        for (TestWorkerStatusDO statusDO : statusDOs) {
            // only register console ip match and in local requester map worker can be
            // choose.
            if (statusDO.getConsoleIp().equals(localIp) && serverSideRegistry.getRequesterMap().get(statusDO.getWorkerSeqNumber()) != null) {
                localBindSidecar = statusDO;
                break;
            }
        }

        return localBindSidecar;
    }

    /**
     * check whether the sidecar is registered in the local console
     */
    private boolean isLocalRegisteredWorker(TestWorkerStatusDO sidecarStatusDO) {
        log.info("[TEST] Send request with local bind worker.....");
        String localIp = HostUtil.getHostIp();
        return sidecarStatusDO.getConsoleIp().equals(localIp) && serverSideRegistry.getRequesterMap().get(sidecarStatusDO.getWorkerSeqNumber()) != null;
    }

    @SneakyThrows
    protected RSocketRespDTO<?> forwardRequest(String apiMethodName, String consoleIp, String specifiedSidecarWsn, Object[] param) {
        String[] jsonParams = new String[param.length];
        for (int i = 0; i < param.length; i++) {
            jsonParams[i] = objectMapper.writeValueAsString(param[i]);
        }

        RequestForwardDTO forwardDTO = new RequestForwardDTO();
        forwardDTO.setApiMethodName(apiMethodName);
        forwardDTO.setJsonParams(jsonParams);
        forwardDTO.setTargetWsn(specifiedSidecarWsn);

        // todo fix test case later...
        String url = "http://" + consoleIp + ":" + SERVER_FORWARD_PORT + URL_SUFFIX;

        refreshJwtTokenIfNeed();

        Map<String, String> headers = new HashMap<>();
        headers.put("jwt_token", jwtToken);
        // headers.put(SessionManager.CSRF_TOKEN_NAME, jwtToken);

        log.info("[FORWARD] Send http request to worker api " + apiMethodName);

        return CanalHttpClient.forwardRSocketRequest(url, new ObjectMapper().writeValueAsString(forwardDTO), headers);
    }

    protected void refreshJwtTokenIfNeed() {
        // if (StringUtils.isBlank(jwtToken) || authService.verifyJwtToken(jwtToken) == null) {
        // DmUserDO userDO = systemService.getOrInitInnerUser();
        // this.jwtToken = authService.genJwtToken(userDO);
        // }
    }

    @Override
    public WorkerIdentity getIdentity() {
        WorkerIdentity identity = new WorkerIdentity();
        identity.setLocalIp(HostUtil.getHostIp());
        return identity;
    }

    @Override
    @PreDestroy
    public void shutdown() {
        try {
            log.info("begin to detach all clients...");

            for (Map.Entry<String, RSocketRequester> requester : serverSideRegistry.getRequesterMap().entrySet()) {
                TestWorkerStatusDO statusDO = new TestWorkerStatusDO();
                statusDO.setWorkerSeqNumber(requester.getKey());
                statusDO.setWorkerConnStatus(WorkerConnStatus.DISCONNECTED);
                workerStatusMapper.updateConnInfoByWsn(statusDO);
                requester.getValue().rsocket().dispose();
            }

            log.info("detach all clients done.");
        } catch (Exception e) {
            String msg = "shutdown RSocket requester error.bug ignore.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
        }
    }
}
