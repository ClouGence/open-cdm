package com.clougence.clouddm.console.web.global.rsocket;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import org.slf4j.MDC;
import org.springframework.messaging.rsocket.RSocketRequester;

import com.clougence.clouddm.comm.RSocketSerialization;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.http.CanalHttpClient;
import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.constants.rsocket.RSocketLogNames;
import com.clougence.clouddm.comm.constants.rsocket.RSocketRouteNames;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.*;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.comm.model.rsocket.AsyncRequestFuture;
import com.clougence.clouddm.comm.util.RSocketRespUtil;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.DmErrorCode;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerStatusMapper;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerStatusDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.rdp.component.jwtsession.RdpJwtManager;
import com.clougence.rdp.component.jwtsession.RdpJwtService;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/12
 **/
@Slf4j
public class DmServerSender implements RSocketServerSender {

    private static final String         URL_SUFFIX = DmControllerUrlPrefix.CONSOLE_PREFIX + "/rsocket/forward";
    private final int                   TIMEOUT_MS = 60000;
    private final RSocketRequestManager requestManager;
    private final DmWorkerMapper        workerMapper;
    private final DmWorkerStatusMapper  statusMapper;
    private final ServerSideRegistry    registry;
    private final RdpJwtService         jwtService;
    private final RdpUserService        userService;
    private final RSocketSerialization  serialization;

    public DmServerSender(RSocketRequestManager requestManager, DmWorkerMapper workerMapper, DmWorkerStatusMapper statusMapper, ServerSideRegistry registry, RdpJwtService jwtService,
                          RdpUserService userService, RSocketSerialization serialization){
        this.requestManager = requestManager;
        this.workerMapper = workerMapper;
        this.statusMapper = statusMapper;
        this.registry = registry;
        this.jwtService = jwtService;
        this.userService = userService;
        this.serialization = serialization;
    }

    private final int SERVER_FORWARD_PORT = 8222;
    private String    jwtToken;

    @SneakyThrows
    @Override
    public RSocketRespDTO<?> requestNonBlock(Long clusterId, String apiFullMethodName, Object[] param) {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
            List<DmWorkerStatusDO> statusDOs = getWorkerStatusDOFromDb(clusterId);
            DmWorkerStatusDO workerStatus = chooseLocalRegisteredWorker(statusDOs);
            if (workerStatus == null) {
                DmWorkerStatusDO randomWorker = statusDOs.get(RandomUtils.nextInt(0, statusDOs.size()));
                log.info("forward rsocket request to random worker " + randomWorker.getWorkerIp() + ", api method name is " + apiFullMethodName);
                return forwardRequest(apiFullMethodName, randomWorker.getConsoleIp(), randomWorker.getWorkerSeqNumber(), param);
            } else {
                RSocketRequester requester = registry.getRequesterMap().get(workerStatus.getWorkerSeqNumber());
                if (requester == null) {
                    String errMsg = "local bind worker get requester from cache is empty.wsn: " + workerStatus.getWorkerSeqNumber();
                    return RSocketRespUtil.buildError(errMsg);
                }

                AsyncRequestFuture asyncRequestFuture = requestManager.registerRsocketRequest(apiFullMethodName);
                RSocketRequestWrapperDTO requestWrapperDTO = new RSocketRequestWrapperDTO();
                requestWrapperDTO.setRequestId(asyncRequestFuture.getRequestId());
                requestWrapperDTO.setRSocketDirectionType(RSocketDirectionType.SERVER_TO_CLIENT);
                requestWrapperDTO.setWorkerIdentity(getIdentity());
                requestWrapperDTO.setApiFullMethodName(apiFullMethodName);
                List<String> jsonParamValues = new ArrayList<>();
                for (Object paramObj : param) {
                    jsonParamValues.add(JsonUtils.toJson(toRParam(paramObj)));
                }
                requestWrapperDTO.setParamJsonValues(jsonParamValues);
                // Though wait result here. Actually, server side have change the spring route
                // handler and process all request as fire-and-forget mode
                requester.route(RSocketRouteNames.MAIN_REQUEST_DISPATCHER).data(requestWrapperDTO).retrieveMono(RSocketRespDTO.class).subscribe();
                RSocketRespDTO<String> responseData = getAsyncResult(asyncRequestFuture);
                stopwatch.stop();
                log.info("[console->worker] send request to sidecar, route name is " + apiFullMethodName + ".request_id:" + asyncRequestFuture.getRequestId() + ",elapse:"
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
    public RSocketRespDTO<?> requestNonBlock(String apiFullMethodName, String specifiedWsn, Object[] param) {
        List<String> jsonParamValues = new ArrayList<>();
        for (Object paramObj : param) {
            jsonParamValues.add(JsonUtils.toJson(toRParam(paramObj)));
        }
        return this.requestNonBlockWithJsonParams(apiFullMethodName, specifiedWsn, jsonParamValues.toArray(new String[0]));
    }

    private RSocketParam toRParam(Object arg) {
        String provider = null;
        if (arg != null) {
            Class<?> argClass = arg.getClass();
            Serialization annotation = argClass.getAnnotation(Serialization.class);
            if (annotation != null) {
                provider = annotation.provider();
            }
        }

        String encode = this.serialization.encode(provider, arg);
        return new RSocketParam(provider, encode);
    }

    @SneakyThrows
    @Override
    public RSocketRespDTO<?> requestNonBlockWithJsonParams(String apiFullMethodName, String specifiedWsn, String[] paramJson) {
        try {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
            Stopwatch stopwatch = Stopwatch.createStarted();
            DmWorkerStatusDO workerStatus = statusMapper.queryOnlineByWsn(specifiedWsn);
            if (workerStatus == null) {
                String errMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), specifiedWsn);
                log.error(errMsg);
                return RSocketRespUtil.buildError(errMsg);
            }

            if (isLocalRegisteredWorker(workerStatus)) {
                RSocketRequester requester = registry.getRequesterMap().get(workerStatus.getWorkerSeqNumber());
                if (requester == null) {
                    String errMsg = "local bind worker get requester from cache is empty.wsn: " + workerStatus.getWorkerSeqNumber();
                    return RSocketRespUtil.buildError(errMsg);
                }

                AsyncRequestFuture asyncRequestFuture = requestManager.registerRsocketRequest(apiFullMethodName);
                RSocketRequestWrapperDTO requestWrapperDTO = new RSocketRequestWrapperDTO();
                requestWrapperDTO.setRequestId(asyncRequestFuture.getRequestId());
                requestWrapperDTO.setRSocketDirectionType(RSocketDirectionType.SERVER_TO_CLIENT);
                requestWrapperDTO.setWorkerIdentity(getIdentity());
                requestWrapperDTO.setApiFullMethodName(apiFullMethodName);
                requestWrapperDTO.setParamJsonValues(new ArrayList<>(Arrays.asList(paramJson)));

                requester.route(RSocketRouteNames.MAIN_REQUEST_DISPATCHER).data(requestWrapperDTO).retrieveMono(RSocketRespDTO.class).subscribe();

                RSocketRespDTO<?> response = getAsyncResult(asyncRequestFuture);
                stopwatch.stop();
                log.info("[console->sidecar] send request to sidecar, route name is " + apiFullMethodName + ".request_id:" + asyncRequestFuture.getRequestId() + ",elapse:"
                         + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
                return response;
            } else {
                log.info("forward rsocket request to specified console (" + workerStatus.getConsoleIp() + "), method name:" + apiFullMethodName);
                return forwardRequestWithJsonParams(apiFullMethodName, workerStatus.getConsoleIp(), workerStatus.getWorkerSeqNumber(), paramJson);
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

    private RSocketRespDTO<String> getAsyncResult(AsyncRequestFuture asyncRequestFuture) {
        try {
            return (RSocketRespDTO<String>) asyncRequestFuture.getSettableFuture().get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            requestManager.removeTimeoutFutureResult(asyncRequestFuture);
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

    protected List<DmWorkerStatusDO> getWorkerStatusDOFromDb(Long clusterId) {
        if (clusterId == null || clusterId <= 0) {
            String errMsg = "cluster id can not be empty.";
            log.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        List<DmWorkerStatusDO> sidecarStatus = this.statusMapper.queryByClusterIdAndStatus(clusterId, WorkerConnStatus.CONNECTED);
        if (CollectionUtils.isNotEmpty(sidecarStatus)) {
            return sidecarStatus;
        }

        List<DmWorkerDO> workerDOs = this.workerMapper.queryByCluster(clusterId);
        if (CollectionUtils.isNotEmpty(workerDOs)) {
            List<DmWorkerStatusDO> fallbackStatus = new ArrayList<>();
            for (DmWorkerDO workerDO : workerDOs) {
                if (workerDO == null || StringUtils.isBlank(workerDO.getWorkerSeqNumber())) {
                    continue;
                }

                DmWorkerStatusDO statusDO = this.statusMapper.queryOnlineByWsn(workerDO.getWorkerSeqNumber());
                if (statusDO != null) {
                    fallbackStatus.add(statusDO);
                }
            }

            if (CollectionUtils.isNotEmpty(fallbackStatus)) {
                log.warn("query worker status by clusterId returned empty, fallback to worker wsn lookup. clusterId={}, workerCount={}, connectedCount={}", clusterId,
                    workerDOs.size(), fallbackStatus.size());
                return fallbackStatus;
            }
        }

        throw new ErrorMessageException(DmErrorCode.CLUSTER_HAVE_NO_WORKS_ERROR.code(), MessageUtils.getClusterHaveNoWorksErrorMessage(clusterId));
    }

    /** get one worker of cluster in local */
    protected DmWorkerStatusDO chooseLocalRegisteredWorker(List<DmWorkerStatusDO> statusDOs) {
        String localIp = HostUtil.getHostIp();
        DmWorkerStatusDO localBindSidecar = null;
        for (DmWorkerStatusDO statusDO : statusDOs) {
            // only register console ip match and in local requester map worker can be choose.
            if (statusDO.getConsoleIp().equals(localIp) && registry.getRequesterMap().get(statusDO.getWorkerSeqNumber()) != null) {
                localBindSidecar = statusDO;
                break;
            }
        }

        return localBindSidecar;
    }

    /** check whether the sidecar is registered in the local console */
    private boolean isLocalRegisteredWorker(DmWorkerStatusDO sidecarStatusDO) {
        String localIp = HostUtil.getHostIp();
        return sidecarStatusDO.getConsoleIp().equals(localIp) && registry.getRequesterMap().get(sidecarStatusDO.getWorkerSeqNumber()) != null;
    }

    @SneakyThrows
    protected RSocketRespDTO<?> forwardRequest(String apiMethodName, String consoleIp, String specifiedSidecarWsn, Object[] param) {
        String[] jsonParams = new String[param.length];
        for (int i = 0; i < param.length; i++) {
            jsonParams[i] = JsonUtils.toJson(param[i]);
        }
        return forwardRequestWithJsonParams(apiMethodName, consoleIp, specifiedSidecarWsn, jsonParams);
    }

    @SneakyThrows
    protected RSocketRespDTO<?> forwardRequestWithJsonParams(String apiMethodName, String consoleIp, String specifiedSidecarWsn, String[] jsonParams) {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = "http://" + consoleIp + ":" + SERVER_FORWARD_PORT + URL_SUFFIX;

        refreshJwtTokenIfNeed();

        RequestForwardDTO forwardDTO = new RequestForwardDTO();
        forwardDTO.setApiMethodName(apiMethodName);
        forwardDTO.setJsonParams(jsonParams);
        forwardDTO.setTargetWsn(specifiedSidecarWsn);

        Map<String, String> headers = new HashMap<>();
        headers.put("jwt_token", jwtToken);
        headers.put(RdpJwtManager.CSRF_TOKEN_NAME, jwtToken);

        log.info("[FORWARD] Send http request to worker api " + apiMethodName);

        return CanalHttpClient.forwardRSocketRequest(url, objectMapper.writeValueAsString(forwardDTO), headers);
    }

    protected void refreshJwtTokenIfNeed() {
        if (StringUtils.isBlank(jwtToken) || jwtService.verifyJwtToken(jwtToken) == null) {
            this.jwtToken = jwtService.genJwtToken(userService.getInnerUser());
        }
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

            for (Map.Entry<String, RSocketRequester> requester : registry.getRequesterMap().entrySet()) {
                DmWorkerStatusDO statusDO = new DmWorkerStatusDO();
                statusDO.setWorkerSeqNumber(requester.getKey());
                statusDO.setWorkerConnStatus(WorkerConnStatus.DISCONNECTED);
                statusMapper.updateConnInfoByWsn(statusDO);
                requester.getValue().rsocket().dispose();
            }

            log.info("detach all clients done.");
        } catch (Exception e) {
            String msg = "shutdown RSocket requester error.bug ignore.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
        }
    }
}
