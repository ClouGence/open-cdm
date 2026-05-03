//package com.clougence.clouddm.console.web.service.system.impl;
//
//import java.util.*;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.support.TransactionTemplate;
//
//import com.alibaba.fastjson.JSON;
//import com.clougence.clouddm.api.console.status.CpuStats;
//import com.clougence.clouddm.api.console.status.DiskStats;
//import com.clougence.clouddm.api.console.status.MemStats;
//import com.clougence.clouddm.console.web.component.monitor.PhysicalStatCollect;
//import com.clougence.clouddm.console.web.dal.mapper.DmConsoleHeartbeatMapper;
//import com.clougence.clouddm.console.web.dal.model.DmConsoleHeartbeatDO;
//import com.clougence.clouddm.console.web.util.DmConsoleHostUtil;
//import com.clougence.clouddm.console.web.util.DmConsoleMacAddressUtil;
//import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
//import com.clougence.clouddm.api.common.boot.UnifiedPostConstructOrder;
//import com.clougence.rdp.util.NamedThreadFactory;
//import com.clougence.utils.CollectionUtils;
//import com.clougence.utils.ExceptionUtils;
//import com.clougence.utils.HostUtil;
//import com.clougence.utils.StringUtils;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//// add by pika, because apply code need console heartbeat
//@UnifiedPostConstructOrder(1)
//public class DmConsoleHeartbeatService implements UnifiedPostConstruct {
//
//    private final int                SCAN_PERIOD_SEC              = 15;
//
//    private final int                CONSOLE_OFFLINE_THRESHOLD_MS = 60 * 1000;
//
//    private ScheduledExecutorService consoleHbService;
//
//    @Resource
//    private DsVersionsServiceImpl    dsVersionsService;
//
//    @Resource
//    private DmConsoleHeartbeatMapper consoleHeartbeatMapper;
//
//    @Override
//    public void init() throws Exception {
//        // first init
//        sendConsoleHeartbeat();
//
//        consoleHbService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("console-heartbeat", true));
//        consoleHbService.scheduleAtFixedRate(this::sendConsoleHeartbeat, 30, SCAN_PERIOD_SEC, TimeUnit.SECONDS);
//    }
//
//    private void sendConsoleHeartbeat() {
//        try {
//            log.info("[Dm Console HeartBeat Service] Begin console heartbeat service");
//            refreshConsoleInfo();
//            log.info("[Dm Console HeartBeat Service] End console heartbeat service");
//        } catch (Throwable e) {
//            String msg = "[Dm Console HeartBeat Service] Console heartbeat service work with exception.msg: " + ExceptionUtils.getRootCauseMessage(e);
//            log.error(msg, e);
//        }
//    }
//
//    private void refreshConsoleInfo() {
//        String localIp = DmConsoleHostUtil.getHostIp(false);
//        String macAddress = DmConsoleMacAddressUtil.getLocalMacAddress(localIp);
//        if (StringUtils.isBlank(macAddress)) {
//            // online -> offline -> online ,the ip maybe change
//            localIp = DmConsoleHostUtil.getHostIp(true);
//            macAddress = DmConsoleMacAddressUtil.getLocalMacAddress(localIp);
//        }
//
//        MemStats memStat = PhysicalStatCollect.getMemStat();
//        CpuStats cpuStat = PhysicalStatCollect.getCpuStat();
//        DiskStats diskStat = PhysicalStatCollect.getDiskStat();
//        String version = fetchDmConsoleVersion();
//        DmConsoleHeartbeatDO curHbDO = consoleHeartbeatMapper.queryByConsoleInfo(localIp, macAddress, hardwareUuid);
//        refreshCurConsoleToDB(curHbDO, version, hardwareUuid, localIp, macAddress, cpuStat, diskStat, memStat);
//
//        List<DmConsoleHeartbeatDO> consoleHbs = consoleHeartbeatMapper.queryActive();
//
//        List<DmConsoleHeartbeatDO> inactiveConsoles = consoleHbs.stream()
//            .filter(hb -> hb.getConsoleSendTime() == null || System.currentTimeMillis() - hb.getConsoleSendTime().getTime() > CONSOLE_OFFLINE_THRESHOLD_MS)
//            .peek(hb -> {
//                hb.setActive(false);
//                hb.setDiskStat(null);
//                hb.setMemStat(null);
//                hb.setCpuStat(null);
//            })
//            .collect(Collectors.toList());
//
//        if (CollectionUtils.isNotEmpty(inactiveConsoles)) {
//            consoleHeartbeatMapper.updateHeartbeatByIds(inactiveConsoles);
//        }
//    }
//
//    private void refreshCurConsoleToDB(DmConsoleHeartbeatDO curHbDO, String version, String hardwareUuid, String hostIp, String macAddress, CpuStats cpuStat, DiskStats diskStat,
//                                       MemStats memStat) {
//        final boolean isNew = (curHbDO == null);
//        final DmConsoleHeartbeatDO hbDO = isNew ? new DmConsoleHeartbeatDO() : curHbDO;
//
//        hbDO.setActive(true);
//        hbDO.setMacAddress(macAddress);
//        hbDO.setHardwareUuid(hardwareUuid);
//        hbDO.setConsoleIp(hostIp);
//        hbDO.setVersion(version);
//        hbDO.setCpuStat(JSON.toJSONString(cpuStat));
//        hbDO.setDiskStat(JSON.toJSONString(diskStat));
//        hbDO.setMemStat(JSON.toJSONString(memStat));
//        hbDO.setConsoleSendTime(new Date());
//
//        if (isNew) {
//            consoleHeartbeatMapper.insert(hbDO);
//        } else {
//            consoleHeartbeatMapper.updateHeartbeatByIds(Collections.singletonList(hbDO));
//        }
//    }
//
//    private String fetchMacAddress() {
//        String localIp = HostUtil.getHostIp();
//        String macAddress = DmConsoleMacAddressUtil.getLocalMacAddress(localIp);
//        if (macAddress == null) {
//            log.error("[Dm Console HeartBeat Service] Local console ip " + localIp + ", not have valid ip address that have mac address. Will empty mac address value.");
//            macAddress = "";
//        } else {
//            macAddress = macAddress.toLowerCase();
//        }
//        return macAddress;
//    }
//
//    public String fetchDmConsoleVersion() {
//        Map<String, Object> dsVersions = dsVersionsService.fetchDsVersions();
//        Object versionInfo = dsVersions.get("buildInfo");
//        String version = "unknow";
//        if (versionInfo instanceof Properties) {
//            version = ((Properties) versionInfo).getProperty("mainVersion");
//        }
//        return version;
//    }
//}
