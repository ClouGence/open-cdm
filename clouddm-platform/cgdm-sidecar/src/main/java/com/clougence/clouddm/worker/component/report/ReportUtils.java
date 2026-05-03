package com.clougence.clouddm.worker.component.report;

import java.io.IOException;
import java.util.HashMap;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.comm.component.http.CanalHttpClient;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Execute shell to get local system's info or using oshi library to collect system stat info
 *
 * @author wanshao create time is 2020/1/14
 **/
@Slf4j
public class ReportUtils {

    public static WorkerIdentity getIdentity() throws IOException {
        ConnAuthDTO authDto = GlobalConfUtils.loadGlobalConf();
        if (StringUtils.isBlank(authDto.getWsn()) || StringUtils.isBlank(authDto.getAk()) || StringUtils.isBlank(authDto.getSk())) {
            throw new IllegalArgumentException("properties in global config (" + authDto + ") are empty.");
        }

        WorkerIdentity identity = new WorkerIdentity();

        identity.setAccessKey(authDto.getAk());
        identity.setWorkerSeqNumber(authDto.getWsn());
        identity.setLocalIp(tryFetchLocalIp());
        return identity;
    }

    public static String tryFetchLocalIp() {
        return HostUtil.getHostIp();
    }

    public static String tryFetchExternalIp() {
        try {
            String externalIp = CanalHttpClient.getWithString("http://getip.clougence.com/", new HashMap<>());
            if (StringUtils.isNotBlank(externalIp)) {
                return externalIp.trim();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("fetch external ip failed, msg:" + ExceptionUtils.getRootCauseMessage(e));
            return null;
        }
    }

    public static void existSystem() {
        Thread thread = new Thread(() -> {
            log.error("try to exist.");
            System.exit(1);
        });
        thread.start();
    }
}
