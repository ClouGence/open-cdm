package com.clougence.clouddm.team.provider.feishu.approval;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;

import com.clougence.clouddm.team.provider.feishu.client.FeishuClient;
import com.clougence.clouddm.team.provider.feishu.client.FeishuEventHandler;
import com.clougence.clouddm.team.provider.feishu.domain.ro.callback.InstanceCallback;
import com.clougence.clouddm.team.provider.feishu.domain.ro.callback.TaskCallback;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalConsoleService;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalTicketInfo;
import com.clougence.utils.JsonUtils;
import com.lark.oapi.core.request.EventReq;

public class FeishuApprovalStreamHandler implements Closeable {

    private final Logger                    logger;
    private final RdpApprovalConsoleService approvalService;
    private FeishuClient                    feishuClient;

    public FeishuApprovalStreamHandler(FeishuClient feishuClient, RdpApprovalConsoleService approvalService){
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

        RdpApprovalTicketInfo approvalInstanceCallback = new RdpApprovalTicketInfo();
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

        RdpApprovalTicketInfo info = new RdpApprovalTicketInfo();
        info.setApproIdentity(event.getInstanceCode());
        info.setProviderType(ApprovalProvider.Feishu.name());

        approvalService.refreshTicket(info);
    }

}
