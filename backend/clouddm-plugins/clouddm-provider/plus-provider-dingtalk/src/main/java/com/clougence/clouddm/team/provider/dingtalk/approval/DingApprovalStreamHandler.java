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
package com.clougence.clouddm.team.provider.dingtalk.approval;

import java.io.Closeable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.clougence.clouddm.sdk.LoggerUtil;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivityStatus;
import com.clougence.clouddm.sdk.service.approval.ApprovalIdentity;
import com.clougence.clouddm.sdk.service.approval.ApprovalRefreshService;
import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingActivityResult;
import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingActivityType;
import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingCallbackEvent;
import com.clougence.clouddm.team.provider.dingtalk.domain.ro.callback.DingInstanceCallbackRO;
import com.clougence.clouddm.team.provider.dingtalk.domain.ro.callback.DingTaskCallbackRO;
import com.clougence.utils.RandomUtils;
import com.clougence.utils.ThreadUtils;
import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;

import lombok.SneakyThrows;
import shade.com.alibaba.fastjson2.JSONObject;

public class DingApprovalStreamHandler implements Closeable {

    private final static Logger          logger = LoggerUtil.getLoggerAppender();
    private final ClassLoader            pluginLoader;
    private final ApprovalRefreshService approvalService;
    private OpenDingTalkClient           client;

    public DingApprovalStreamHandler(ApprovalRefreshService approvalService, ClassLoader pluginLoader){
        this.approvalService = approvalService;
        this.pluginLoader = pluginLoader;
    }

    public void start(String appKey, String appSecret) throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.pluginLoader);
            this.client = OpenDingTalkStreamClientBuilder.custom()
                .credential(new AuthClientCredential(appKey, appSecret))
                //event listeners
                .registerAllEventListener(event -> {
                    try {
                        handler(event);
                        return EventAckStatus.SUCCESS;// when success.
                    } catch (Exception e) {
                        return EventAckStatus.LATER;// when fail.
                    }
                })
                .build();
            this.client.start();
            logger.info("DingTalkClient started");
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    public void stop() throws Exception {
        if (this.client != null) {
            this.client.stop();
            this.client = null;
            logger.info("DingTalkClient stop");
        }
    }

    @SneakyThrows
    @Override
    public void close() {
        this.stop();
    }

    private void handler(GenericOpenDingTalkEvent event) {
        String eventType = event.getEventType();
        JSONObject data = event.getData();

        logger.info("DingTalkClient received eventType: " + eventType + ", data: " + data);

        DingCallbackEvent callbackEvent = DingCallbackEvent.getByName(eventType);
        if (callbackEvent == null) {
            logger.error("Unknown event type: {}", eventType);
            return;
        }

        switch (callbackEvent) {
            case BPMS_TASK_CHANGE: {
                handleTaskChange(data);
                break;
            }
            case BPMS_INSTANCE_CHANGE: {
                handleInstanceChange(data);
                break;
            }
            default: {
                logger.error("Unknown event type: {}", eventType);
                break;
            }
        }

    }

    @SneakyThrows
    private void handleInstanceChange(JSONObject data) {
        ApprovalIdentity dto = new ApprovalIdentity();
        DingInstanceCallbackRO ro = data.toJavaObject(DingInstanceCallbackRO.class);

        dto.setApproIdentity(ro.getProcessInstanceId());
        dto.setProviderType(ApprovalProvider.DingTalk.name());

        this.approvalService.refreshTicket(dto);
    }

    @SneakyThrows
    private void handleTaskChange(JSONObject data) {
        // wait sometime, reduce conflict
        ThreadUtils.sleep(RandomUtils.nextInt(100, 500), TimeUnit.MILLISECONDS);
        ApprovalActivity approvalTask = new ApprovalActivity();

        DingTaskCallbackRO ro = data.toJavaObject(DingTaskCallbackRO.class);

        if (ro.getFinishTime() != null) {
            approvalTask.setFinishTime(new Date(ro.getFinishTime()));
        }

        approvalTask.setStartTime(new Date(ro.getCreateTime()));
        approvalTask.setRemark(ro.getRemark());
        approvalTask.setUserId(ro.getStaffId());
        approvalTask.setActivityId(ro.getActivityId());
        approvalTask.setTaskId(ro.getTaskId().toString());
        approvalTask.setApprovalIdentity(ro.getProcessInstanceId());
        approvalTask.setPlatform(ApprovalProvider.DingTalk.name());

        ApprovalActivityStatus approvalTaskStatus = parseTaskStatus(ro);
        approvalTask.setStatus(approvalTaskStatus);

        this.approvalService.updateActivity(approvalTask);
    }

    private static ApprovalActivityStatus parseTaskStatus(DingTaskCallbackRO ro) {
        DingActivityType type = ro.getType();
        DingActivityResult result = ro.getResult();
        if (type == DingActivityType.FINISH) {
            if (result == DingActivityResult.AGREE) {
                return ApprovalActivityStatus.COMPLETED;
            } else if (result == DingActivityResult.REFUSE) {
                return ApprovalActivityStatus.REFUSE;
            }
        } else if (type == DingActivityType.CANCEL) {
            return ApprovalActivityStatus.CLOSE;
        } else {
            return ApprovalActivityStatus.RUNNING;
        }
        return ApprovalActivityStatus.NEW;
    }
}
