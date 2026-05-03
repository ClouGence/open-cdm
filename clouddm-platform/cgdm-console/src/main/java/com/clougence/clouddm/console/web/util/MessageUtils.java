package com.clougence.clouddm.console.web.util;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.constants.I18nDmLabelKeys;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.DmClusterMapper;
import com.clougence.clouddm.console.web.dal.model.DmClusterDO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/6 11:25
 */
@Slf4j
public class MessageUtils {

    private static DmClusterMapper dmClusterMapper;

    public static void initUtils(ApplicationContext spring) {
        dmClusterMapper = spring.getBean(DmClusterMapper.class);
    }

    public static String getClusterHaveNoWorksErrorMessage(Long clusterId) {
        return DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_HAVE_NO_WORKS_ERROR.name(), getClusterNameForI18n(clusterId));
    }

    public static String getClusterNameForI18n(Long clusterId) {
        if (clusterId == null) {
            return "ID:UNKNOWN";
        }
        if (dmClusterMapper == null) {
            return "ID:" + clusterId;
        }

        DmClusterDO clusterDO = dmClusterMapper.queryById(clusterId);
        if (clusterDO == null) {
            return "ID:" + clusterId + "(Deleted)";
        } else if (StringUtils.isBlank(clusterDO.getClusterDesc())) {
            return clusterDO.getClusterName();
        } else {
            return clusterDO.getClusterName() + "(" + clusterDO.getClusterDesc() + ")";
        }
    }

    public static String getRpcInvokeTimeoutErrorMessage(RSocketSendType sendType, Long clusterId, String workerIp, long passTime, String message, String requestId) {
        if (sendType == RSocketSendType.CLUSTER) {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.RPC_INVOKER_CLUSTER_ERROR.name(), String.valueOf(passTime), String.valueOf(clusterId), message);
        } else {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.RPC_INVOKER_WORKER_ERROR.name(), String.valueOf(passTime), String.valueOf(clusterId), workerIp, message);
        }
    }

    public static String genEnvNameByEnvId(String envId) {
        if (StringUtils.isBlank(envId)) {
            return DmI18nUtils.getMessage(I18nDmLabelKeys.RDB_ENV.name()) + " Unknown";
        } else {
            return DmI18nUtils.getMessage(I18nDmLabelKeys.RDB_ENV.name()) + " " + envId;
        }
    }

    public static String genDsNameByDsId(String dsId) {
        if (dsId == null) {
            return DmI18nUtils.getMessage(I18nDmLabelKeys.RDB_INSTANCE.name()) + " Unknown";
        } else {
            return DmI18nUtils.getMessage(I18nDmLabelKeys.RDB_INSTANCE.name()) + " " + dsId;
        }
    }
}
