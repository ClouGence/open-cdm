/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.util;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysClusterDO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/6 11:25
 */
@Slf4j
public class MessageUtils {

    private static SystemDal systemDal;

    public static void initUtils(ApplicationContext spring) {
        systemDal = spring.getBean(SystemDal.class);
    }

    public static String getClusterHaveNoWorksErrorMessage(Long clusterId) {
        return DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_HAVE_NO_WORKS_ERROR.name(), getClusterNameForI18n(clusterId));
    }

    public static String getClusterNameForI18n(Long clusterId) {
        if (clusterId == null) {
            return "ID:UNKNOWN";
        }
        if (systemDal == null) {
            return "ID:" + clusterId;
        }

        DmSysClusterDO clusterDO = systemDal.clusterMapper().queryById(clusterId);
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
