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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.LifeSpiResponse;
import com.clougence.clouddm.sdk.LifeSpiStatus;
import com.clougence.clouddm.sdk.LoggerUtil;
import com.clougence.clouddm.sdk.approval.*;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.service.approval.ApprovalRefreshService;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.team.provider.feishu.client.FeishuApi;
import com.clougence.clouddm.team.provider.feishu.client.FeishuClient;
import com.clougence.clouddm.team.provider.feishu.constants.FeishuConfigKey;
import com.clougence.clouddm.team.provider.feishu.constants.FeishuI18nKeys2;
import com.clougence.clouddm.team.provider.feishu.constants.approval.FeishuInstanceStatus;
import com.clougence.clouddm.team.provider.feishu.constants.approval.FeishuTaskStatus;
import com.clougence.clouddm.team.provider.feishu.domain.mo.FeishuTemplateInfo;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;
import com.lark.oapi.service.approval.v4.model.ApprovalNodeInfo;
import com.lark.oapi.service.approval.v4.model.GetInstanceRespBody;
import com.lark.oapi.service.approval.v4.model.InstanceTask;
import com.lark.oapi.service.approval.v4.model.InstanceTimeline;

public class FeishuApprovalProviderSpi implements ApprovalProviderSpi {

    private final static Logger                            logger           = LoggerUtil.getLoggerAppender();
    private final ApprovalRefreshService                   approvalService;
    private final ConsoleConfigService                     configService;
    private final Map<String, FeishuApprovalStreamHandler> streamHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, FeishuClient>                clientMap        = new ConcurrentHashMap<>();

    public FeishuApprovalProviderSpi(ConsoleConfigService configService, ApprovalRefreshService approvalService){
        this.approvalService = approvalService;
        this.configService = configService;
    }

    @Override
    public LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) {
        // fetch config
        List<ConfigData> configList = configService.fetchSettings(ownerUid, Arrays.asList(//
                FeishuConfigKey.ApprovalEnable.getConfigKey(),//
                FeishuConfigKey.ApprovalAppID.getConfigKey(),//
                FeishuConfigKey.ApprovalAppSecret.getConfigKey(),//
                FeishuConfigKey.ApprovalApiTimeoutSec.getConfigKey()));
        Map<String, String> configMap = new HashMap<>();
        for (ConfigData c : configList) {
            configMap.put(c.getConfigName(), c.getConfigValue());
        }

        // enable is false.
        String enableCfg = configMap.getOrDefault(FeishuConfigKey.ApprovalEnable.getConfigKey(), "false");
        if (!StringUtils.equalsIgnoreCase(enableCfg, "true")) {
            logger.info("[FeishuApprovalProviderSpi] IgnoreApproval[Feishu] primaryUid：" + ownerUid + ", enable is false.");
            return new LifeSpiResponse();
        }

        // start and init.
        synchronized (this) {
            if (this.streamHandlerMap.containsKey(ownerUid) || this.clientMap.containsKey(ownerUid)) {
                logger.info("[FeishuApprovalProviderSpi] IgnoreApproval[Feishu] primaryUid：" + ownerUid + ", already started.");
                return new LifeSpiResponse();
            }

            String appKey = configMap.get(FeishuConfigKey.ApprovalAppID.getConfigKey());
            String appSecret = configMap.get(FeishuConfigKey.ApprovalAppSecret.getConfigKey());
            String apiTimeoutSec = configMap.get(FeishuConfigKey.ApprovalApiTimeoutSec.getConfigKey());

            // start api client
            FeishuClient feishuClient = new FeishuClient(logger, appKey, appSecret, Integer.parseInt(apiTimeoutSec), null);
            feishuClient.start(true);
            this.clientMap.put(ownerUid, feishuClient);

            // start streamHandler
            FeishuApprovalStreamHandler streamHandler = new FeishuApprovalStreamHandler(feishuClient, this.approvalService);
            streamHandler.start();
            this.streamHandlerMap.put(ownerUid, streamHandler);

            // finish
            logger.info("[FeishuApprovalProviderSpi] StartApproval[Feishu] primaryUid：" + ownerUid);
            return new LifeSpiResponse();
        }
    }

    @Override
    public LifeSpiResponse stop(String ownerUid, LifeSpiRequest requestDTO) {
        // stop stream handler.
        IOUtils.closeQuietly(this.streamHandlerMap.remove(ownerUid));

        // remove api client
        IOUtils.closeQuietly(this.clientMap.remove(ownerUid));

        logger.info("[FeishuApprovalProviderSpi] StopApproval[Feishu] ownerUid：" + ownerUid);
        return new LifeSpiResponse();
    }

    @Override
    public LifeSpiResponse status(String ownerUid, LifeSpiRequest requestDTO) {
        LifeSpiStatus dto = new LifeSpiStatus();
        dto.setRunning(this.streamHandlerMap.containsKey(ownerUid) || this.clientMap.containsKey(ownerUid));
        dto.setNameKey(FeishuI18nKeys2.FEISHU_APPROVAL_SERVICES_NAME);
        return new LifeSpiResponse(JsonUtils.toJson(dto));
    }

    protected FeishuApi approvalApi(String primaryUid) {
        if (this.clientMap.containsKey(primaryUid)) {
            return this.clientMap.get(primaryUid).getBindApi();
        } else {
            throw new UnsupportedOperationException("feishuApprovalService is was closed");
        }
    }

    //

    @Override
    public ApprovalCreateInstanceResult createApprovalInstance(String ownerUid, ApprovalForm info) throws ThirdPartyApiException {
        FeishuApi approvalApi = this.approvalApi(ownerUid);

        FeishuTemplateInfo approvalActivities = approvalApi.getApprovalActivities(info.getTemplateIdentity());
        String userIdByPhone = approvalApi.getUserIdByPhone(info.getTicketUserPhone());

        String form = FeishuApiUtils.getFormParams(approvalActivities.getWidgets(), info);
        ApprovalCreateInstanceResult approvalCreateInstanceResult = new ApprovalCreateInstanceResult();
        String instance = approvalApi.createInstance(info, form, userIdByPhone);
        approvalCreateInstanceResult.setApprovalIdentity(instance);
        approvalCreateInstanceResult.setActivityList(convertToApprovalActivityList(approvalActivities.getNodeInfoList()));
        ApprovalUrl approvalUrl = new ApprovalUrl();
        approvalUrl.setPcUrl(approvalApi.getPcUrl(instance));
        approvalCreateInstanceResult.setApprovalUrl(approvalUrl);
        return approvalCreateInstanceResult;
    }

    private static List<ApprovalActivityInfo> convertToApprovalActivityList(List<ApprovalNodeInfo> list) {
        List<ApprovalActivityInfo> approvalActivities = new ArrayList<>();
        for (ApprovalNodeInfo approvalNodeInfo : list) {
            ApprovalActivityInfo aaObj = new ApprovalActivityInfo();
            aaObj.setActivityId(approvalNodeInfo.getNodeId());
            aaObj.setActivityName(approvalNodeInfo.getName());
            aaObj.setOrder(-1);
            approvalActivities.add(aaObj);
        }
        return approvalActivities;
    }

    @Override
    public ApprovalInstanceInfo getLastInfo(String ownerUid, String identity) throws ThirdPartyApiException {
        if (StringUtils.isBlank(identity)) {
            throw ThirdPartyApiException.asRDP().with(FeishuI18nKeys2.FEISHU_APPROVAL_INSTANCE_IS_EMPTY);
        }

        FeishuApi approvalApi = this.approvalApi(ownerUid);

        GetInstanceRespBody info = approvalApi.getLastInfo(identity);
        ApprovalInstanceInfo approvalInstance = new ApprovalInstanceInfo();
        FeishuInstanceStatus status = FeishuInstanceStatus.getByName(info.getStatus());
        approvalInstance.setStatus(status.convertStatus());
        Map<String, String> commentMap = new HashMap<>();
        for (InstanceTimeline instanceTimeline : info.getTimeline()) {
            if (StringUtils.isNotEmpty(instanceTimeline.getComment())) {
                commentMap.put(instanceTimeline.getTaskId(), instanceTimeline.getComment());
            }
        }

        Map<String, List<ApprovalActivity>> map = new HashMap<>();
        if (info.getTaskList() != null) {
            for (InstanceTask instanceTask : info.getTaskList()) {
                FeishuTaskStatus taskStatus = FeishuTaskStatus.getByName(instanceTask.getStatus());
                if (taskStatus == FeishuTaskStatus.TRANSFERRED) {
                    continue;
                }

                List<ApprovalActivity> list = map.getOrDefault(instanceTask.getNodeId(), new ArrayList<>());
                ApprovalActivity aaObj = new ApprovalActivity();
                aaObj.setTaskId(instanceTask.getId());
                aaObj.setActivityId(instanceTask.getNodeId());
                aaObj.setUserId(instanceTask.getUserId());
                aaObj.setStartTime(new Date(Long.parseLong(instanceTask.getStartTime())));
                if (StringUtils.isNotBlank(instanceTask.getUserId())) {
                    aaObj.setUserName(approvalApi.getUserInfoById(instanceTask.getUserId()));
                } else {
                    aaObj.setUserName("");
                }
                aaObj.setRemark(commentMap.get(instanceTask.getId()));
                if (!instanceTask.getEndTime().equals("0")) {
                    aaObj.setFinishTime(new Date(Long.parseLong(instanceTask.getEndTime())));
                }
                aaObj.setStatus(taskStatus.convertStatus());
                list.add(aaObj);
                map.put(instanceTask.getNodeId(), list);
            }
        }

        approvalInstance.setMap(map);

        return approvalInstance;
    }

    @Override
    public List<ApprovalTemplate> getTemplates(String ownerUid) throws ThirdPartyApiException {
        FeishuApi approvalApi = this.approvalApi(ownerUid);
        List<ConfigData> configList = this.configService.fetchSettings(ownerUid, Collections.singletonList(FeishuConfigKey.ApprovalTemplateList.getConfigKey()));
        if (configList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return approvalApi.getTemplates(configList.get(0).getConfigValue());
        }
    }

    @Override
    public void useTemplate(String ownerUid, String before, String current) throws ThirdPartyApiException {
        FeishuApi approvalApi = this.approvalApi(ownerUid);
        if (StringUtils.isNotBlank(before)) {
            approvalApi.unSubscribeTemplate(before);
        }
        if (StringUtils.isNotBlank(current)) {
            approvalApi.subscribeTemplate(current);
        }
    }

    @Override
    public ApprovalUserInfo getUserDetailByUid(String ownerUid, String userId) throws ThirdPartyApiException {
        return null;// There is no need to implement Feishu
    }

    @Override
    public void cancelApprovalInst(String ownerUid, ApprovalInstanceCancelInfo info) throws ThirdPartyApiException {
        FeishuApi approvalApi = this.approvalApi(ownerUid);

        String userIdByPhone = approvalApi.getUserIdByPhone(info.getTicketUserPhone());
        approvalApi.cancelInstance(info, userIdByPhone);
    }
}
