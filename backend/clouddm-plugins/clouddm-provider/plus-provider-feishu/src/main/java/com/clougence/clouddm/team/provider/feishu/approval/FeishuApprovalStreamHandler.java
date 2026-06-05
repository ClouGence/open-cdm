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
package com.clougence.clouddm.team.provider.feishu.approval;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;

import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.service.approval.ApprovalIdentity;
import com.clougence.clouddm.sdk.service.approval.ApprovalRefreshService;
import com.clougence.clouddm.team.provider.feishu.client.FeishuClient;
import com.clougence.clouddm.team.provider.feishu.client.FeishuEventHandler;
import com.clougence.clouddm.team.provider.feishu.domain.ro.callback.InstanceCallback;
import com.clougence.clouddm.team.provider.feishu.domain.ro.callback.TaskCallback;
import com.clougence.utils.JsonUtils;
import com.lark.oapi.core.request.EventReq;

public class FeishuApprovalStreamHandler implements Closeable {

    private final Logger                 logger;
    private final ApprovalRefreshService approvalService;
    private FeishuClient                 feishuClient;

    public FeishuApprovalStreamHandler(FeishuClient feishuClient, ApprovalRefreshService approvalService){
        this.logger = feishuClient.getLogger();
        this.feishuClient = feishuClient;
        this.approvalService = approvalService;
    }

    public void start() {
        this.feishuClient.setApprovalInstanceEventHandler(this::customInstanceHandler);
        this.feishuClient.setApprovalTaskEventHandler(this::customTaskHandler);
        logger.info("feishuStreamHandler start.");
    }

    public void stop() {
        if (this.feishuClient != null) {
            this.feishuClient.setApprovalInstanceEventHandler(FeishuEventHandler.EMPTY);
            this.feishuClient.setApprovalTaskEventHandler(FeishuEventHandler.EMPTY);
            this.feishuClient = null;
            logger.info("feishuStreamHandler stop.");
        }
    }

    @Override
    public void close() throws IOException {
        this.stop();
    }

    private void customTaskHandler(EventReq eventReq) {
        logger.info("feishu received eventType：task_change，data： " + new String(eventReq.getBody(), StandardCharsets.UTF_8));
        TaskCallback callback = JsonUtils.toObj(new String(eventReq.getBody(), StandardCharsets.UTF_8), TaskCallback.class);
        TaskCallback.Event event = callback.getEvent();

        ApprovalIdentity approvalInstanceCallback = new ApprovalIdentity();
        approvalInstanceCallback.setApproIdentity(event.getInstanceCode());
        approvalInstanceCallback.setProviderType(ApprovalProvider.Feishu.name());
        if (event.getStatus().equals("PENDING")) {
            return;
        }
        approvalService.refreshTicket(approvalInstanceCallback);

    }

    private void customInstanceHandler(EventReq eventReq) {
        logger.info("feishu received eventType：instance_change，data： " + new String(eventReq.getBody(), StandardCharsets.UTF_8));
        InstanceCallback callback = JsonUtils.toObj(new String(eventReq.getBody(), StandardCharsets.UTF_8), InstanceCallback.class);
        InstanceCallback.Event event = callback.getEvent();

        ApprovalIdentity info = new ApprovalIdentity();
        info.setApproIdentity(event.getInstanceCode());
        info.setProviderType(ApprovalProvider.Feishu.name());

        approvalService.refreshTicket(info);
    }

}
