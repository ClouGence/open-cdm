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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author bucketli 2019-12-30 22:46
 * @since 1.1.3
 */
@Slf4j
public class CallUtils {

    private static final OkHttpClient client = new OkHttpClient.Builder().readTimeout(6, TimeUnit.SECONDS).build();
    private static SystemDal          systemDal;

    public static void initUtils(ApplicationContext spring) {
        systemDal = spring.getBean(SystemDal.class);
    }

    public static Response get(String callbackUrl) throws IOException {
        Request request = new Request.Builder().url(callbackUrl).build();
        return client.newCall(request).execute();
    }

    public static Response post(String callbackUrl, Map<String, String> formBody) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        if (formBody != null) {
            formBody.forEach((key, value) -> builder.addEncoded("client_id", URLEncoder.encode(value)));
        }

        Request request = new Request.Builder().url(callbackUrl) //
            .header("content-type", "application/x-www-form-urlencoded")
            .post(builder.build())
            .build();
        return client.newCall(request).execute();
    }

    public static RSocketSendDTO buildSendDTO(String workerSeqNumber) {
        DmSysWorkerDO worker;
        if (StringUtils.isNotBlank(workerSeqNumber)) {
            worker = systemDal.workerMapper().queryConnectedByWsn(workerSeqNumber);
        } else {
            List<DmSysWorkerDO> workers = systemDal.workerMapper().queryByConnStatus(WorkerConnStatus.CONNECTED);
            worker = workers == null || workers.isEmpty() ? null : workers.get(0);
        }

        if (worker == null) {
            String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), workerSeqNumber);
            throw new ErrorMessageException(msg);
        }

        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setUid(worker.getUid());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);
        return sendDTO;
    }

    public static RSocketSendDTO buildSendDTOByCluster(Long clusterId) {
        if (clusterId == null || clusterId <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SSH_CLUSTER_ID_REQUIRED_ERROR.name()));
        }

        List<DmSysWorkerDO> workers = systemDal.workerMapper().queryConnectedByClusterId(clusterId);
        DmSysWorkerDO worker = workers == null || workers.isEmpty() ? null : workers.get(0);
        if (worker == null) {
            String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), clusterId);
            throw new ErrorMessageException(msg);
        }

        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setUid(worker.getUid());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);
        return sendDTO;
    }
}
