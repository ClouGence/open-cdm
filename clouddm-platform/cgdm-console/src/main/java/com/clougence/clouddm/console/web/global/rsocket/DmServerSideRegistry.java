package com.clougence.clouddm.console.web.global.rsocket;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.rsocket.RSocketRequester;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.comm.component.RSocketExceptionManager;
import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.rsocket.RSocketRegisterInfo;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerStatusMapper;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerStatusDO;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.global.notify.DmWorkerRegisterNotify;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.HostUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class DmServerSideRegistry implements ServerSideRegistry {

    private final DmWorkerStatusMapper         statusMapper;
    private final RdpUserMapper                userMapper;
    private final DmWorkerMapper               workerMapper;
    private final RSocketExceptionManager      exceptionManager;
    private final List<DmWorkerRegisterNotify> notifyServices;

    public DmServerSideRegistry(DmWorkerStatusMapper statusMapper, RdpUserMapper userMapper, DmWorkerMapper workerMapper, List<DmWorkerRegisterNotify> notifyServices,
                                RSocketExceptionManager exceptionManager){
        this.statusMapper = statusMapper;
        this.userMapper = userMapper;
        this.workerMapper = workerMapper;
        this.notifyServices = notifyServices;
        this.exceptionManager = exceptionManager;
    }

    /**
     * key is wsn. Every wsn related to a unique user sidecar machine. todo need add metric here later
     */
    private final Map<String, RSocketRequester> workerRequesterMap = new ConcurrentHashMap<>();

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void register(RSocketRegisterInfo registerInfo) {
        try {
            String workerSeqNumber = registerInfo.getWorkerSeqNumber();
            DmWorkerStatusDO workerStatusDO = statusMapper.queryByWsn(workerSeqNumber);
            workerStatusDO.setConsoleIp(HostUtil.getHostIp());
            workerStatusDO.setWorkerConnStatus(WorkerConnStatus.CONNECTED);
            workerStatusDO.setWorkerIp(registerInfo.getWorkerIp());
            workerStatusDO.setWorkerSeqNumber(workerSeqNumber);
            statusMapper.updateConnInfoByWsn(workerStatusDO);
            workerRequesterMap.put(registerInfo.getWorkerSeqNumber(), registerInfo.getRequester());
            log.info("register successfully for worker (" + workerSeqNumber + "," + workerStatusDO.getWorkerSeqNumber() + ")");
            this.notifyServices.forEach(s -> s.notifyRegister(workerSeqNumber));
        } catch (Exception e) {
            String errMsg = "register rsocket request failed. msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            exceptionManager.saveExcOrAlert(e);
            throw e;
        }
    }

    @Override
    public void checkAuth(ConnAuthDTO authInfo) {
        try {
            RdpUserDO userDO = userMapper.queryByAccessKey(authInfo.getAk());
            if (userDO == null) {
                throw new IllegalArgumentException("no such user,ak:" + authInfo.getAk() + ".remote ip:" + authInfo.getIp());
            }

            String skInDb = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userDO.getSecretKey());
            if (!skInDb.equals(authInfo.getSk())) {
                throw new IllegalArgumentException("user sk is not correct.remote ip:" + authInfo.getIp());
            }

            DmWorkerDO workerDO = workerMapper.getByWsn(authInfo.getWsn());
            if (workerDO == null) {
                throw new IllegalArgumentException("worker metadata is not exist.remote ip:" + authInfo.getIp());
            }

            DmWorkerStatusDO statusDO = this.statusMapper.queryByWsn(authInfo.getWsn());
            if (statusDO != null && statusDO.getWorkerConnStatus() == WorkerConnStatus.CONNECTED && this.workerRequesterMap.containsKey(authInfo.getWsn())) {
                // must throw refuse conn exception to make sure the sidecar can exit normally
                String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(authInfo.getSendAuthTimeMs());
                throw new RuntimeException("reject worker (" + authInfo.getWsn() + ") 's registration. Connection start time is " + str
                                           + ". Maybe the wsn is wrong or another connection use the same wsn is working. Old resume connection will also be rejected after console restart.");
            }
        } catch (Exception e) {
            String errMsg = "try to accept a connection error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.warn(errMsg, e);
            exceptionManager.saveExcOrAlert(e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void unRegister(String workerSeqNumber) {
        try {
            DmWorkerStatusDO workerStatusDO = new DmWorkerStatusDO();
            workerStatusDO.setWorkerSeqNumber(workerSeqNumber);
            workerStatusDO.setWorkerConnStatus(WorkerConnStatus.DISCONNECTED);
            statusMapper.updateConnInfoByWsn(workerStatusDO);
            workerRequesterMap.remove(workerSeqNumber);
            log.info("unregister sidecar with worker sequence number " + workerSeqNumber + " and its ip is " + workerStatusDO.getWorkerIp());
        } catch (Exception e) {
            String errMsg = "unregister rsocket request failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            exceptionManager.saveExcOrAlert(e);
            throw e;
        }
    }

    @Override
    public RSocketRequester getRSocketRequester(String workerSeqNumber) {
        return workerRequesterMap.get(workerSeqNumber);
    }

    @Override
    public Map<String, RSocketRequester> getRequesterMap() { return workerRequesterMap; }
}
