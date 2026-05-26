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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultOperationRecords;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultTasks;
import com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesResponseBody.ListUserVisibleBpmsProcessesResponseBodyResultProcessList;
import com.aliyun.dingtalkworkflow_1_0.models.ProcessForecastResponseBody.ProcessForecastResponseBodyResult;
import com.aliyun.dingtalkworkflow_1_0.models.ProcessForecastResponseBody.ProcessForecastResponseBodyResultWorkflowActivityRules;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.LifeSpiResponse;
import com.clougence.clouddm.sdk.LifeSpiStatus;
import com.clougence.clouddm.sdk.LoggerUtil;
import com.clougence.clouddm.sdk.approval.*;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivityStatus;
import com.clougence.clouddm.sdk.service.approval.ApprovalRefreshService;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.team.provider.dingtalk.client.DingApi;
import com.clougence.clouddm.team.provider.dingtalk.client.DingClient;
import com.clougence.clouddm.team.provider.dingtalk.constants.DingConfigKey;
import com.clougence.clouddm.team.provider.dingtalk.constants.DingI18nKeys;
import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingTaskResult;
import com.clougence.clouddm.team.provider.dingtalk.constants.approval.DingTaskStatus;
import com.clougence.clouddm.team.provider.dingtalk.domain.ro.api.DingUserDetailRO;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import lombok.SneakyThrows;

public class DingApprovalProviderSpi implements ApprovalProviderSpi {

    private final static Logger                          logger           = LoggerUtil.getLoggerAppender();
    private final ApprovalRefreshService                 approvalService;
    private final ConsoleConfigService                   configService;
    private final ClassLoader                            pluginLoader;
    private final Map<String, DingApprovalStreamHandler> streamHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, DingClient>                clientMap        = new ConcurrentHashMap<>();

    public DingApprovalProviderSpi(ConsoleConfigService configService, ApprovalRefreshService approvalService, ClassLoader pluginLoader){
        this.approvalService = approvalService;
        this.configService = configService;
        this.pluginLoader = pluginLoader;
    }

    @Override
    public LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) throws Exception {
        // fetch config
        List<ConfigData> configList = configService.fetchSettings(ownerUid, Arrays.asList(//
                DingConfigKey.ApprovalEnable.getConfigKey(),//
                DingConfigKey.ApprovalClientId.getConfigKey(),//
                DingConfigKey.ApprovalClientSecret.getConfigKey()));
        Map<String, String> configMap = new HashMap<>();
        for (ConfigData c : configList) {
            configMap.put(c.getConfigName(), c.getConfigValue());
        }

        // enable is false.
        String enableCfg = configMap.getOrDefault(DingConfigKey.ApprovalEnable.getConfigKey(), "false");
        if (!StringUtils.equalsIgnoreCase(enableCfg, "true")) {
            logger.info("ignoreApproval[Ding] primaryUid：" + ownerUid + ", enable is false.");
            return new LifeSpiResponse();
        }

        // start and init.
        synchronized (this) {
            if (this.streamHandlerMap.containsKey(ownerUid) || this.clientMap.containsKey(ownerUid)) {
                logger.info("ignoreApproval[Ding] primaryUid：" + ownerUid + ", already started.");
                return new LifeSpiResponse();
            }

            String appKey = configMap.get(DingConfigKey.ApprovalClientId.getConfigKey());
            String appSecret = configMap.get(DingConfigKey.ApprovalClientSecret.getConfigKey());

            // start api client
            DingClient dingClient = new DingClient(logger, appKey, appSecret);
            this.clientMap.put(ownerUid, dingClient);

            // start streamHandler
            DingApprovalStreamHandler streamHandler = new DingApprovalStreamHandler(this.approvalService, this.pluginLoader);
            streamHandler.start(appKey, appSecret);
            this.streamHandlerMap.put(ownerUid, streamHandler);

            // finish
            logger.info("startApproval[Ding] primaryUid：" + ownerUid);
            return new LifeSpiResponse();
        }
    }

    @Override
    public LifeSpiResponse stop(String ownerUid, LifeSpiRequest requestDTO) {
        // stop stream handler.
        IOUtils.closeQuietly(this.streamHandlerMap.remove(ownerUid));

        // remove api client
        IOUtils.closeQuietly(this.clientMap.remove(ownerUid));

        logger.info("stopApproval[Ding] ownerUid：" + ownerUid);
        return new LifeSpiResponse();
    }

    @Override
    public LifeSpiResponse status(String ownerUid, LifeSpiRequest requestDTO) {
        LifeSpiStatus dto = new LifeSpiStatus();
        dto.setRunning(this.streamHandlerMap.containsKey(ownerUid) || this.clientMap.containsKey(ownerUid));
        dto.setNameKey(DingI18nKeys.DINGTALK_APPROVAL_SERVICES_NAME);
        return new LifeSpiResponse(JsonUtils.toJson(dto));
    }

    protected DingApi approvalApi(String primaryUid) {
        if (this.clientMap.containsKey(primaryUid)) {
            return this.clientMap.get(primaryUid).getBindApi();
        } else {
            throw new UnsupportedOperationException("dingApprovalService is was closed");
        }
    }

    @Override
    @SneakyThrows
    public ApprovalCreateInstanceResult createApprovalInstance(String ownerUid, ApprovalForm form) throws ThirdPartyApiException {
        DingApi approvalApi = this.approvalApi(ownerUid);

        String approvalIdentity = approvalApi.createInstance(form, form.getTicketUserPhone());

        ApprovalCreateInstanceResult result = new ApprovalCreateInstanceResult();
        result.setApprovalIdentity(approvalIdentity);
        result.setActivityList(getApprovalActivityList(ownerUid, form));
        ApprovalUrl approvalUrl = new ApprovalUrl();
        approvalUrl.setPcUrl(approvalApi.getApprovalUrl(approvalIdentity));
        result.setApprovalUrl(approvalUrl);
        return result;
    }

    @SneakyThrows
    private List<ApprovalActivityInfo> getApprovalActivityList(String ownerUid, ApprovalForm info) {
        DingApi approvalApi = this.approvalApi(ownerUid);

        ProcessForecastResponseBodyResult result = approvalApi.getApprovalActivities(info);

        int i = 1;
        List<ApprovalActivityInfo> aaList = new ArrayList<>();
        for (ProcessForecastResponseBodyResultWorkflowActivityRules workflowActivityRule : result.getWorkflowActivityRules()) {
            ApprovalActivityInfo aaObj = new ApprovalActivityInfo();
            aaObj.setActivityName(workflowActivityRule.getActivityName());
            aaObj.setActivityId(workflowActivityRule.getActivityId());
            aaObj.setOrder(i++);
            aaList.add(aaObj);
        }
        return aaList;
    }

    @Override
    @SneakyThrows
    public ApprovalInstanceInfo getLastInfo(String ownerUid, String identity) throws ThirdPartyApiException {
        if (StringUtils.isBlank(identity)) {
            throw ThirdPartyApiException.asRDP().with(DingI18nKeys.DINGTALK_APPROVAL_INSTANCE_IS_EMPTY);
        }

        DingApi approvalApi = this.approvalApi(ownerUid);

        GetProcessInstanceResponseBodyResult result = approvalApi.getApprovalDetail(identity);;
        List<GetProcessInstanceResponseBodyResultOperationRecords> operationRecords = result.getOperationRecords();
        List<GetProcessInstanceResponseBodyResultTasks> tasks = result.getTasks();

        Map<String, List<ApprovalActivity>> map = new HashMap<>();
        for (GetProcessInstanceResponseBodyResultTasks task : tasks) {
            // ignore
            if (task.getStatus().equalsIgnoreCase("CANCELED")) {
                continue;
            }

            List<ApprovalActivity> list = map.getOrDefault(task.getActivityId(), new ArrayList<>());
            ApprovalActivity dto = new ApprovalActivity();
            dto.setTaskId(task.getTaskId().toString());
            dto.setActivityId(task.getActivityId());
            try {
                DingUserDetailRO userDetailRO = approvalApi.getUserInfoByUid(task.getUserId());
                dto.setUserName(userDetailRO.getResultInfo().getUserName());
            } catch (Exception e) {
                if (e.getMessage().contains("找不到该用户")) {
                    dto.setUserName("[已离职用户]");
                    logger.warn("dingtalk not find user,user does not exist or has resigned, user id is " + task.getUserId(), e);
                } else {
                    throw e;
                }
            }

            DingTaskStatus status = DingTaskStatus.getByName(task.getStatus());
            DingTaskResult result1 = DingTaskResult.getByName(task.getResult());
            if (status == DingTaskStatus.COMPLETED) {
                if (result1 == DingTaskResult.AGREE) {
                    dto.setStatus(ApprovalActivityStatus.COMPLETED);
                } else if (result1 == DingTaskResult.REFUSE) {
                    dto.setStatus(ApprovalActivityStatus.REFUSE);
                }
            } else if (status == DingTaskStatus.RUNNING) {
                dto.setStatus(ApprovalActivityStatus.RUNNING);
            } else if (status == DingTaskStatus.TERMINATED) {
                dto.setStatus(ApprovalActivityStatus.CANCELED);
            } else {
                dto.setStatus(ApprovalActivityStatus.NEW);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            dto.setStartTime(dateFormat.parse(task.getCreateTime()));

            if (StringUtils.isNotEmpty(task.getFinishTime())) {

                Date date = dateFormat.parse(task.getFinishTime());
                dto.setFinishTime(date);

                for (GetProcessInstanceResponseBodyResultOperationRecords operationRecord : operationRecords) {
                    // instance start or end record, ignore
                    if (operationRecord.getActivityId() == null) {
                        continue;
                    }
                    if (operationRecord.getActivityId().equalsIgnoreCase(task.getActivityId()) && operationRecord.getUserId().equalsIgnoreCase(task.getUserId())) {
                        dto.setRemark(operationRecord.getRemark());
                        break;
                    }
                }
            }
            list.add(dto);
            map.put(task.getActivityId(), list);
        }

        ApprovalInstanceInfo approvalInstance = new ApprovalInstanceInfo();

        ApprovalInstanceStatus approvalInstanceStatus = ApprovalInstanceStatus.valueOfIgnoreCase(result.getStatus());
        if (approvalInstanceStatus == ApprovalInstanceStatus.COMPLETED && result.getResult().equalsIgnoreCase("REFUSE")) {
            approvalInstanceStatus = ApprovalInstanceStatus.REFUSE;
        }

        approvalInstance.setMap(map);
        approvalInstance.setStatus(approvalInstanceStatus);

        return approvalInstance;

    }

    @Override
    @SneakyThrows
    public List<ApprovalTemplate> getTemplates(String ownerUid) throws ThirdPartyApiException {
        DingApi approvalApi = this.approvalApi(ownerUid);

        List<ListUserVisibleBpmsProcessesResponseBodyResultProcessList> templates = approvalApi.getApprovalTemplates();

        List<ApprovalTemplate> templateDTOS = new ArrayList<>();
        for (ListUserVisibleBpmsProcessesResponseBodyResultProcessList t : templates) {
            ApprovalTemplate approvalTemplate = new ApprovalTemplate();

            approvalTemplate.setApproTemplateName(t.getName());
            approvalTemplate.setApproUrl(t.getUrl());
            approvalTemplate.setTemplateIdentity(t.getProcessCode());
            templateDTOS.add(approvalTemplate);
        }
        return templateDTOS;
    }

    @Override
    public void useTemplate(String ownerUid, String before, String current) throws ThirdPartyApiException {
        // do nothing.
    }

    @Override
    @SneakyThrows
    public ApprovalUserInfo getUserDetailByUid(String ownerUid, String targetUserId) throws ThirdPartyApiException {
        DingApi approvalApi = this.approvalApi(ownerUid);

        DingUserDetailRO info = approvalApi.getUserInfoByUid(targetUserId);;
        ApprovalUserInfo userInfo = new ApprovalUserInfo();
        userInfo.setUsername(info.getResultInfo().getUserName());
        return userInfo;
    }

    @Override
    @SneakyThrows
    public void cancelApprovalInst(String ownerUid, ApprovalInstanceCancelInfo info) throws ThirdPartyApiException {
        DingApi approvalApi = this.approvalApi(ownerUid);

        String uid = approvalApi.getDingUidByPhone(info.getTicketUserPhone());
        approvalApi.cancelApproval(uid, info.getApprovalInstanceIdentity());
    }
}
